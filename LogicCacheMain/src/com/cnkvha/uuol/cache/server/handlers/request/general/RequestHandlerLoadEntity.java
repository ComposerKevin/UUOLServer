package com.cnkvha.uuol.cache.server.handlers.request.general;

import com.cnkvha.uuol.cache.protocol.handlers.RequestHandler;
import com.cnkvha.uuol.cache.protocol.message.general.request.CacheRequestLoadEntity;
import com.cnkvha.uuol.cache.protocol.message.general.response.CacheResponseLoadEntity;

public class RequestHandlerLoadEntity extends RequestHandler<CacheRequestLoadEntity, CacheResponseLoadEntity> {

	@Override
	public CacheResponseLoadEntity handle(CacheRequestLoadEntity req) {
		//Connection conn = ConnectionPool.getConnection();
		return new CacheResponseLoadEntity(0, 0);
	}
	


}
