package com.cnkvha.uuol.cache.protocol.data.meta;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.cnkvha.uuol.cache.protocol.data.meta.type.*;

public enum MetaDataType {
	INT(0, MetaDataIntElement.class),
	LONG(1, MetaDataLongElement.class),
	FLOAT(2, MetaDataFloatElement.class),
	DOUBLE(3, MetaDataDoubleElement.class),
	VEC3_LONG(4, MetaDataVec3LElement.class),
	VEC3_DOUBLE(5, MetaDataVec3DElement.class),
	STRING(6, MetaDataStringElement.class),
	BYTE_ARRAY(7, MetaDataByteArrayElement.class);
	
	
	
	//Reverser start
	private final static Map<Integer, MetaDataType> reversed = new ConcurrentHashMap<>();
	
	static{
		for(MetaDataType t : MetaDataType.values()){
			reversed.put(new Integer(t.getTypeId()), t);
		}
	}
	
	public static MetaDataType getByType(byte type) throws IllegalArgumentException {
		Integer t = new Integer(type & 0xFF);
		if(!reversed.containsKey(t)){
			throw new IllegalArgumentException("Unsupported meta data type ID! ");
		}
		return reversed.get(t);
	}
	//Reverser end
	
	
	
	
	
	private byte typeId;
	private Class<? extends MetaDataElement> cls;
	
	private MetaDataType(int typeId, Class<? extends MetaDataElement> cls){
		this.typeId = (byte)(typeId & 0xFF);
		this.cls = cls;
	}
	
	public byte getTypeId() {
		return typeId;
	}
	
	public Class<? extends MetaDataElement> getDataClass() {
		return cls;
	}
}
