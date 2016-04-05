package com.cnkvha.uuol.cache.protocol.handlers;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.cnkvha.uuol.cache.protocol.data.RequestIdentifier;
import com.cnkvha.uuol.cache.protocol.message.CacheMessageResponse;

public final class SentRequest<RESPONSE extends CacheMessageResponse> {
	public final Map<String, RESPONSE> responses = new ConcurrentHashMap<>();
	
	public final RequestIdentifier id;
	
	public final ResponseHandler<RESPONSE> handler;
	
	public final List<String> initialClusters;
	
	public SentRequest(List<String> initialClusters, RequestIdentifier id, ResponseHandler<RESPONSE> handler) {
		this.id = id;
		this.handler = handler;
		this.initialClusters = initialClusters;
	}
	
	public synchronized boolean onResponse(String sender, RESPONSE resp, List<String> onlineClusters){
		//Remove disconnected clusters' response
		Iterator<String> it = initialClusters.iterator();
		while(it.hasNext()){
			String n = it.next();
			if(!onlineClusters.contains(n)){
				//Cluster went offline
				it.remove();
				responses.remove(sender);
			}
		}
		System.out.println("onResponse(): Sender=" + sender + " , clusters online: " + onlineClusters.size());
		if(!responses.containsKey(sender) && onlineClusters.contains(sender)){
			System.out.println("RESPONSE SAVED! ");
			responses.put(sender, resp);
		}
		return tryExecute(onlineClusters);
	}
	
	public synchronized boolean tryExecute(List<String> onlineClusters){
		// Substract one to prevent from counting ourself in. 
		if((handler.needAllResponses() && responses.size() < onlineClusters.size() - 1) || 
				(!handler.needAllResponses() && responses.size() == 0)){
			return false;
		}
		handler.handle(responses);
		return true;
	}
}
