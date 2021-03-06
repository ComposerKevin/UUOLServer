package com.cnkvha.uuol.cache.server;

import java.util.List;

import com.cnkvha.uuol.cache.protocol.UUOLComponent;
import com.cnkvha.uuol.cache.protocol.handlers.BroadcastHandler;
import com.cnkvha.uuol.cache.protocol.handlers.ClusterList;
import com.cnkvha.uuol.cache.protocol.handlers.RequestHandler;
import com.cnkvha.uuol.cache.protocol.handlers.SentRequestContainer;
import com.cnkvha.uuol.cache.protocol.message.CacheAbstractMessage;
import com.cnkvha.uuol.cache.protocol.message.CacheMessageBatch;
import com.cnkvha.uuol.cache.protocol.message.CacheMessageBroadcast;
import com.cnkvha.uuol.cache.protocol.message.CacheMessageRequest;
import com.cnkvha.uuol.cache.protocol.message.CacheMessageResponse;
import com.cnkvha.uuol.cache.protocol.message.general.request.CacheRequestLoadChunk;
import com.cnkvha.uuol.cache.protocol.message.general.request.CacheRequestLoadEntity;
import com.cnkvha.uuol.cache.server.handlers.request.general.*;
import com.cnkvha.uuol.sjl.data.SerializationTool;
import com.sun.enterprise.ee.cms.core.CallBack;
import com.sun.enterprise.ee.cms.core.MessageSignal;
import com.sun.enterprise.ee.cms.core.Signal;
import com.sun.enterprise.ee.cms.core.SignalAcquireException;
import com.sun.enterprise.ee.cms.core.SignalReleaseException;

public class CacheClientHandler implements CallBack, ClusterList {
	private CacheServer server;

	private final SentRequestContainer requests;
	private final RequestHandler.RequestHandlerRegister requestRegister;

	private final BroadcastHandler.BroadcastHandlerRegister broadcastRegister;

	public CacheClientHandler(CacheServer server) {
		this.server = server;
		requests = new SentRequestContainer(this);
		requestRegister = new RequestHandler.RequestHandlerRegister();

		// Register request handlers, REMEMBER: Only GENERAL component can send
		// requests
		requestRegister.register(CacheRequestLoadChunk.class,
				new RequestHandlerLoadChunk());
		requestRegister.register(CacheRequestLoadEntity.class,
				new RequestHandlerLoadEntity());

		broadcastRegister = new BroadcastHandler.BroadcastHandlerRegister();
	}

	@Override
	public void processNotification(Signal sig) {
		try {
			sig.acquire();
		} catch (SignalAcquireException e1) {
			e1.printStackTrace();
			return;
		}
		if (!MessageSignal.class.isAssignableFrom(sig.getClass())) {
			try {
				sig.release();
			} catch (SignalReleaseException e) {
			}
			return;
		}
		MessageSignal msig = (MessageSignal) sig;

		Object obj = null;
		try {
			obj = SerializationTool.decode(msig.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			server.getLogger().error(
					"Packet decode error from " + msig.getMemberToken() + "! ");
			try {
				sig.release();
			} catch (SignalReleaseException e1) {
			}
			return;
		}
		
		String sender = msig.getMemberToken();
		
		try {
			sig.release();
		} catch (SignalReleaseException e) {
		}

		System.out.println("Got packet: " + obj.getClass().getSimpleName() + " and message?=" + CacheAbstractMessage.class.isAssignableFrom(obj.getClass()));
		
		if (!CacheAbstractMessage.class.isAssignableFrom(obj.getClass()))
			return;

		if (CacheMessageBatch.class.isAssignableFrom(obj.getClass())) {
			for (CacheAbstractMessage sub : ((CacheMessageBatch) obj).messages) {
				processMessage(sender, sub);
			}
		} else {
			processMessage(sender, (CacheAbstractMessage) obj);
		}
	}

	private void processMessage(String sender, CacheAbstractMessage obj) {
		if (sender == null || obj == null)
			return;
		if (CacheMessageResponse.class.isAssignableFrom(obj.getClass())) {
			// Detect response
			CacheMessageResponse r = (CacheMessageResponse) obj;
			server.getLogger().info("Response message REQ: " + r.reqid.toString());
			if (requests.available(r.reqid)) {
				requests.call(sender, r, getCurrentClusters());
				return;
			}
		} else if (CacheMessageRequest.class.isAssignableFrom(obj.getClass())) {
			// Detect request
			CacheMessageRequest r = (CacheMessageRequest) obj;
			CacheMessageResponse ret = requestRegister.process(r);
			if (ret != null) {
				server.broadcastPacket(ret, UUOLComponent.GENERAL);
				return;
			}
		} else if (CacheMessageBroadcast.class.isAssignableFrom(obj.getClass())) {
			// Detect broadcast
			CacheMessageBroadcast bc = (CacheMessageBroadcast) obj;
			server.getThreadPool().execute( () -> broadcastRegister.process(bc));
		}
	}

	public SentRequestContainer getRequests() {
		return requests;
	}

	public int cleanUpRequests() {
		return requests.autoCleanUp(getCurrentClusters());
	}

	public CacheServer getServer() {
		return server;
	}

	public int getClusterSize() {
		return getCurrentClusters().size();
	}

	@Override
	public List<String> getCurrentClusters() {
		return server.getGms().getGroupHandle().getCurrentCoreMembers();
	}

	public void stop() {
		requests.close();
	}
}
