package com.cnkvha.uuol.cache.protocol.handlers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.cnkvha.uuol.cache.protocol.message.CacheMessageRequest;
import com.cnkvha.uuol.cache.protocol.message.CacheMessageResponse;

public abstract class RequestHandler<REQUEST extends CacheMessageRequest, RESPONSE extends CacheMessageResponse> {

	public abstract RESPONSE handle(REQUEST req);

	/* ================== REGISTER ================== */

	public static class RequestHandlerRegister {

		private final Map<Class<? extends CacheMessageRequest>, RequestHandler<? extends CacheMessageRequest, ? extends CacheMessageResponse>> handlers = new ConcurrentHashMap<>();

		public RequestHandlerRegister register(Class<? extends CacheMessageRequest> clazz, RequestHandler<? extends CacheMessageRequest, ? extends CacheMessageResponse> handler){
			if(clazz == null || handler == null) return this;
			handlers.put(clazz, handler);
			return this;
		}

		public CacheMessageResponse process(CacheMessageRequest req) {
			if (!handlers.containsKey(req.getClass()))
				return null;
			@SuppressWarnings("unchecked")
			RequestHandler<CacheMessageRequest, CacheMessageResponse> h = (RequestHandler<CacheMessageRequest, CacheMessageResponse>) handlers
					.get(req.getClass());
			if(h == null) {
				//CacheServer.getInstance().getLogger().warn("Unhandled request " + req.getClass().getSimpleName() + ", request id: [" + req.reqid.toString() + "]. ");
				return null;
			}
			CacheMessageResponse response = h.handle(req);
			if(response == null){
				return null;
			}
			response.reqid = req.reqid; // Override them
			return response;
		}
	}
}
