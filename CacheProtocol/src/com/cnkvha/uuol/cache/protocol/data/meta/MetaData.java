package com.cnkvha.uuol.cache.protocol.data.meta;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MetaData implements Serializable{
	private static final long serialVersionUID = -6493612904910795770L;
	
	private final Map<String, MetaDataElement> entries;
	
	public MetaData() {
		entries = new ConcurrentHashMap<>();
	}
	
	public boolean hasKey(String key){
		return entries.containsKey(key);
	}
	
	public MetaDataElement get(String key, MetaDataElement def){
		if(!entries.containsKey(key)) return def.clone();
		return entries.get(key);
	}
	
	public void set(String key, MetaDataElement ele){
		entries.put(key, ele);
	}
	

}
