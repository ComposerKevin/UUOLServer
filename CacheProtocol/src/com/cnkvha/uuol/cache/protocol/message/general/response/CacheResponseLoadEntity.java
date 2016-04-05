package com.cnkvha.uuol.cache.protocol.message.general.response;

import com.cnkvha.uuol.cache.protocol.message.CacheMessageResponse;

public class CacheResponseLoadEntity extends CacheMessageResponse {

	private static final long serialVersionUID = 1309884604469241573L;

	public final static int STATUS_LOADED = 0;
	public final static int STATUS_ALREADY_LOADED = 1;
	public final static int STATUS_FAILD_LOAD = 2;
	public final static int STATUS_LOADED_BY_OTHER = 3; 
	
	//Reasons only have meanings when status is STATUS_FAILD_LOAD. 
	public final static int REASON_CHUNK = 1;	//Faild loading needed chunks
	public final static int REASON_ENTITY = 2;	//Faild loading entity
	
	
	public int status;
	
	public int faildReason;

	public CacheResponseLoadEntity() {
	}
	
	public CacheResponseLoadEntity(int status, int faildReason) {
		this.status = status;
		this.faildReason = faildReason;
	}
	
}
