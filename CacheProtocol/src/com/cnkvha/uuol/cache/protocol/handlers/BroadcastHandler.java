package com.cnkvha.uuol.cache.protocol.handlers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.cnkvha.uuol.cache.protocol.message.CacheMessageBroadcast;

public abstract class BroadcastHandler<M extends CacheMessageBroadcast> {
	public abstract void handle(M msg);
	
	public static class BroadcastHandlerRegister{
		private final Map<Class<? extends CacheMessageBroadcast>, BroadcastHandler<? extends CacheMessageBroadcast>> handlers = new ConcurrentHashMap<>();

		public BroadcastHandlerRegister register(Class<? extends CacheMessageBroadcast> clazz, BroadcastHandler<? extends CacheMessageBroadcast> handler){
			if(clazz == null || handler == null) return this;
			handlers.put(clazz, handler);
			return this;
		}

		public void process(CacheMessageBroadcast bc) {
			if (!handlers.containsKey(bc.getClass()))
				return;
			@SuppressWarnings("unchecked")
			BroadcastHandler<CacheMessageBroadcast> h = (BroadcastHandler<CacheMessageBroadcast>) handlers
					.get(bc);
			h.handle(bc);
		}
	}
}
