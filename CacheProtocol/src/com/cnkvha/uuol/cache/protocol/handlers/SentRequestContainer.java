package com.cnkvha.uuol.cache.protocol.handlers;

import java.io.Closeable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.cnkvha.uuol.cache.protocol.data.RequestIdentifier;
import com.cnkvha.uuol.cache.protocol.message.CacheMessageResponse;

public class SentRequestContainer extends Thread implements Closeable {
	private final Map<RequestIdentifier, SentRequest<? extends CacheMessageResponse>> handlers;
	
	private ClusterList clusterList;
	
	private boolean running;
	
	public SentRequestContainer(ClusterList clusterList) {
		handlers = new ConcurrentHashMap<>();
		this.clusterList = clusterList;
		running = true;
	}
	
	@SuppressWarnings("unchecked")
	public void register(List<String> currentClusters, RequestIdentifier id, ResponseHandler<? extends CacheMessageResponse> handler){
		if(currentClusters.size() <= 1){
			return; //Only ourself, do not save this because we will never get a response. 
		}
		handlers.put(id, new SentRequest<CacheMessageResponse>(currentClusters, id, (ResponseHandler<CacheMessageResponse>) handler));
	}
	
	public void call(String sender, CacheMessageResponse msg, List<String> onlineClusters){
		if(msg == null) return;
		if(!available(msg.reqid)) return;

		@SuppressWarnings("unchecked")
		SentRequest<CacheMessageResponse> h = (SentRequest<CacheMessageResponse>) handlers.get(msg.reqid);
		
		if(h.onResponse(sender, msg, onlineClusters)){
			handlers.remove(msg.reqid);
		}
	}
	
	public int autoCleanUp(List<String> onlineClusters){
		int count = 0;
		Iterator<Map.Entry<RequestIdentifier, SentRequest<? extends CacheMessageResponse>>> it = handlers.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<RequestIdentifier, SentRequest<? extends CacheMessageResponse>> entry = it.next();
			if(entry.getValue().tryExecute(onlineClusters)){
				it.remove();
				count++;
			}
		}
		return count;
	}
	
	public boolean available(RequestIdentifier id){
		if(id == null) return false;
		if(id.id == null || id.sender == null) return false;
		return handlers.containsKey(id);
	}
	
	public final static long REQUEST_CLEAN_INTERVAL = 5000;
	
	
	@Override
	public void run() {
		long startTime = -1;
		while(running){
			startTime = System.currentTimeMillis();
			List<String> clusters = clusterList.getCurrentClusters();
			if(clusters == null){
				long timeCosts = System.currentTimeMillis() - startTime;
				if(timeCosts > REQUEST_CLEAN_INTERVAL){
					continue;
				} else {
					try {
						Thread.sleep(REQUEST_CLEAN_INTERVAL - timeCosts);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					continue;
				}
			}
			int count = autoCleanUp(clusters);
			if (count > 0) {
				long timeCosts = System.currentTimeMillis() - startTime;
				if(timeCosts > REQUEST_CLEAN_INTERVAL){
					continue;
				} else {
					try {
						Thread.sleep(REQUEST_CLEAN_INTERVAL - timeCosts);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}else{
				try {
					Thread.sleep(REQUEST_CLEAN_INTERVAL);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public void close() {
		running = false;
		interrupt();
	}
}
