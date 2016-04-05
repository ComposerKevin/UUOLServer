package com.cnkvha.uuol.cache.protocol.message;

import com.cnkvha.uuol.cache.protocol.data.RequestIdentifier;

@SuppressWarnings("serial")
public abstract class CacheMessageRequest extends CacheAbstractMessage {
	public RequestIdentifier reqid;

	public CacheMessageRequest(RequestIdentifier reqid) {
		this.reqid = reqid;
	}
}
