package com.cnkvha.uuol.cache.protocol.handlers;

import java.util.Map;

import com.cnkvha.uuol.cache.protocol.message.CacheMessageResponse;

public interface ResponseHandler<T extends CacheMessageResponse> {
	public boolean needAllResponses();
	
	public void handle(Map<String, T> resp);
}
