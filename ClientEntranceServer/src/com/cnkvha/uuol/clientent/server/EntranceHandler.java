package com.cnkvha.uuol.clientent.server;

import java.util.List;

import com.cnkvha.uuol.cache.protocol.UUOLComponent;
import com.cnkvha.uuol.cache.protocol.handlers.BroadcastHandler;
import com.cnkvha.uuol.cache.protocol.handlers.ClusterList;
import com.cnkvha.uuol.cache.protocol.handlers.RequestHandler;
import com.cnkvha.uuol.cache.protocol.handlers.SentRequestContainer;
import com.cnkvha.uuol.cache.protocol.message.CacheAbstractMessage;
import com.cnkvha.uuol.cache.protocol.message.CacheMessageBroadcast;
import com.cnkvha.uuol.cache.protocol.message.CacheMessageRequest;
import com.cnkvha.uuol.cache.protocol.message.CacheMessageResponse;
import com.cnkvha.uuol.sjl.data.SerializationTool;
import com.sun.enterprise.ee.cms.core.CallBack;
import com.sun.enterprise.ee.cms.core.MessageSignal;
import com.sun.enterprise.ee.cms.core.Signal;
import com.sun.enterprise.ee.cms.core.SignalAcquireException;
import com.sun.enterprise.ee.cms.core.SignalReleaseException;

public class EntranceHandler implements CallBack, ClusterList {

	private final EntranceServer server;
	
	private final SentRequestContainer requests;
	private final RequestHandler.RequestHandlerRegister requestRegister;
	
	private final BroadcastHandler.BroadcastHandlerRegister broadcastRegister;
	
	public EntranceHandler(EntranceServer server) {
		this.server = server;
		
		requests = new SentRequestContainer(this);
		requestRegister = new RequestHandler.RequestHandlerRegister();

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
		if(!MessageSignal.class.isAssignableFrom(sig.getClass())){
			try {
				sig.release();
			} catch (SignalReleaseException e) {
			}
			return;
		}
		MessageSignal msig = (MessageSignal)sig;

		Object obj = null;
		try {
			obj = SerializationTool.decode(msig.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			server.getLogger().error("Packet decode error from " + msig.getMemberToken() + "! ");
			try {
				sig.release();
			} catch (SignalReleaseException e1) {
			}
			return;
		}
		if(obj == null) {
			try {
				sig.release();
			} catch (SignalReleaseException e) {
			}
			return;
		}
		
		if(!CacheAbstractMessage.class.isAssignableFrom(obj.getClass())) return;
		
		if(CacheMessageResponse.class.isAssignableFrom(obj.getClass())){
			//Detect response
			CacheMessageResponse r = (CacheMessageResponse)obj;
			if(requests.available(r.reqid)){
				requests.call(msig.getMemberToken(), r, getCurrentClusters());
				try {
					sig.release();
				} catch (SignalReleaseException e) {
				}
				return;
			}
		} else if(CacheMessageRequest.class.isAssignableFrom(obj.getClass())){
			//Detect request
			CacheMessageRequest r = (CacheMessageRequest)obj;
			CacheMessageResponse ret = requestRegister.process(r);
			if(ret != null){
				server.sendPacket(ret, UUOLComponent.GENERAL);
				try {
					sig.release();
				} catch (SignalReleaseException e) {
				}
				return;
			}
		} else if(CacheMessageBroadcast.class.isAssignableFrom(obj.getClass())){
			//Detect broadcast
			CacheMessageBroadcast bc = (CacheMessageBroadcast)obj;
			broadcastRegister.process(bc);
		}
	}
	
	
	
	
	
	public EntranceServer getServer() {
		return server;
	}

	@Override
	public List<String> getCurrentClusters() {
		return server.getGms().getGroupHandle().getCurrentCoreMembers();
	}
	
	public void stop(){
		requests.close();
	}
	
	public void cleanUpRequests(){
		requests.autoCleanUp(getCurrentClusters());
	}
	
	public BroadcastHandler.BroadcastHandlerRegister getBroadcastRegister() {
		return broadcastRegister;
	}
	
	public RequestHandler.RequestHandlerRegister getRequestRegister() {
		return requestRegister;
	}
	
	public SentRequestContainer getRequests() {
		return requests;
	}
}
