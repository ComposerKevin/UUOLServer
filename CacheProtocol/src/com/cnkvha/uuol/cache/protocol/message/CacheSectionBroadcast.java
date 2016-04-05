package com.cnkvha.uuol.cache.protocol.message;

import java.util.UUID;

import com.cnkvha.uuol.sjl.math.Vector3Long;

@SuppressWarnings("serial")
public abstract class CacheSectionBroadcast extends CacheMessageBroadcast {
	
	/**
	 * Null for universe. 
	 */
	public UUID planet;
	
	public Vector3Long section;
	
}
