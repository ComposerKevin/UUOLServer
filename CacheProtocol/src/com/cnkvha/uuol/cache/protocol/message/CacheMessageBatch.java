package com.cnkvha.uuol.cache.protocol.message;

import java.util.Collection;

public class CacheMessageBatch extends CacheAbstractMessage {
	private static final long serialVersionUID = 3592243449943594929L;
	
	public CacheAbstractMessage[] messages;

	public CacheMessageBatch() {
	}
	
	public CacheMessageBatch(CacheAbstractMessage[] messages) {
		this.messages = messages;
	}

	public static CacheMessageBatch fromList(CacheAbstractMessage... msg){
		return new CacheMessageBatch(msg);
	}
	
	public static CacheMessageBatch fromCollection(Collection<? extends CacheAbstractMessage> msg){
		return new CacheMessageBatch(msg.toArray(new CacheAbstractMessage[0]));
	}
}
