package com.cnkvha.uuol.cache.protocol.data;

import java.util.HashMap;
import java.util.Map;

public enum EntityType {
	PLAYER(0);
	
	
	
	
	
	private long type;
	
	private EntityType(long type) {
		this.type = type;
	}
	
	
	public long getType() {
		return type;
	}
	
	
	private final static Map<Long, EntityType> reverse = new HashMap<>();
	
	static {
		for(EntityType type : EntityType.values()){
			reverse.put(type.getType(), type);
		}
	}
	
	public static EntityType get(long type){
		if(!reverse.containsKey(type)) return null;
		return reverse.get(type);
	}
}
