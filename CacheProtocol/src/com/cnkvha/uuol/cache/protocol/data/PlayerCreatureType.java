package com.cnkvha.uuol.cache.protocol.data;

import java.util.HashMap;
import java.util.Map;

/**
 * This data should be put in player entity meta. 
 *
 */
public enum PlayerCreatureType {
	HUMAN(0);
	
	
	/**
	 * Use this on player entities. 
	 */
	public final static String META_KEY = "playerCreatureType";
	
	
	
	private long type;
	
	private PlayerCreatureType(long type) {
		this.type = type;
	}
	
	
	public long getType() {
		return type;
	}
	
	private final static Map<Long, PlayerCreatureType> reverse = new HashMap<>();
	
	static {
		for(PlayerCreatureType type : PlayerCreatureType.values()){
			reverse.put(type.getType(), type);
		}
	}
	
	public static PlayerCreatureType get(long type){
		if(!reverse.containsKey(type)) return null;
		return reverse.get(type);
	}
}
