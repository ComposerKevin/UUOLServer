package com.cnkvha.uuol.cache.protocol.message.general.request;

import java.util.ArrayList;
import java.util.UUID;

import com.cnkvha.uuol.cache.protocol.data.RequestIdentifier;
import com.cnkvha.uuol.cache.protocol.message.CacheMessageBatch;
import com.cnkvha.uuol.cache.protocol.message.CacheMessageRequest;
import com.cnkvha.uuol.sjl.math.Vector3Long;

/**
 * We should not use this in normal situations. 
 * Since entity transmission are chunk to chunk, we better use LoadSection request! 
 */
public class CacheRequestLoadChunk extends CacheMessageRequest {
	private static final long serialVersionUID = 4524060927061838713L;
	
	
	/**
	 * Set this to false when you want to load entities in the universe.  
	 */
	public UUID planet;
	
	public Vector3Long chunkLocation;
	
	public CacheRequestLoadChunk(RequestIdentifier reqId, UUID planet, Vector3Long chunkLocation) {
		super(reqId);
		this.planet = planet;
		this.chunkLocation = chunkLocation;
	}
	
	public static CacheMessageBatch getForRadius(String sender, UUID planet, Vector3Long center, int radius){
		ArrayList<CacheRequestLoadChunk> lst = new ArrayList<>();
		int radius_squared = radius * radius;
		for(long x = - radius; x < radius; x++){
			for(long y = - radius; y < radius; y++){
				for(long z = - radius; z < radius; z++){
					if(x * x + y * y + z * z > radius_squared) continue;
					CacheRequestLoadChunk req = new CacheRequestLoadChunk(
							new RequestIdentifier(sender, UUID.randomUUID()), planet, new Vector3Long(center.x + x, center.y + y, center.z + z));
					lst.add(req);
				}
			}
		}
		return CacheMessageBatch.fromCollection(lst);
	}
}

