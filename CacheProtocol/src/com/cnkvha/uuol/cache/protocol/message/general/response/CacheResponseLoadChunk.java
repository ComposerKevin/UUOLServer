package com.cnkvha.uuol.cache.protocol.message.general.response;

import com.cnkvha.uuol.cache.protocol.message.CacheMessageResponse;

public class CacheResponseLoadChunk extends CacheMessageResponse {
	private static final long serialVersionUID = -4366998649859438892L;
	
	public final static int STATUS_LOADED = 0;
	public final static int STATUS_ALREADY_LOADED = 1;
	public final static int FAILD_LOAD = 2;
	public final static int LOADED_BY_OTHER = 3; 
	public final static int NOT_EXIST = 4;
	
	
	public int status;
	
	public CacheResponseLoadChunk() {
	}
	
	public CacheResponseLoadChunk(int status){
		this.status = status;
	}
}
