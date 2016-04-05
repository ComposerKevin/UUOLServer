package com.cnkvha.uuol.cache.protocol.message;

import com.cnkvha.uuol.cache.protocol.data.RequestIdentifier;

@SuppressWarnings("serial")
public abstract class CacheMessageResponse extends CacheAbstractMessage {
	public RequestIdentifier reqid;
}
