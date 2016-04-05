package com.cnkvha.uuol.cache.protocol.message.general.request;

import java.util.UUID;

import com.cnkvha.uuol.cache.protocol.data.RequestIdentifier;
import com.cnkvha.uuol.cache.protocol.message.CacheMessageRequest;

public class CacheRequestLoadEntity extends CacheMessageRequest {
	private static final long serialVersionUID = -8514684744999340550L;

	public UUID planet;
	
	public UUID entityId;
	
	public CacheRequestLoadEntity(RequestIdentifier reqid) {
		super(reqid);
	}

}
