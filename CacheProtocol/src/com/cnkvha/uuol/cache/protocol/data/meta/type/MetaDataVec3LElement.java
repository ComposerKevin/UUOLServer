package com.cnkvha.uuol.cache.protocol.data.meta.type;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.cnkvha.uuol.cache.protocol.data.meta.MetaDataElement;
import com.cnkvha.uuol.cache.protocol.data.meta.MetaDataType;
import com.cnkvha.uuol.sjl.math.Vector3Long;

public class MetaDataVec3LElement implements MetaDataElement {

	private Vector3Long data;
	
	public MetaDataVec3LElement() {
	}
	
	public MetaDataVec3LElement(Vector3Long data){
		this.data = data;
	}
	
	@Override
	public MetaDataType dataType() {
		return MetaDataType.VEC3_LONG;
	}

	@Override
	public Object getData() {
		return data;
	}

	@Override
	public void setData(Object data) {
		if(!Vector3Long.class.isInstance(data)){
			throw new IllegalArgumentException("Got non-int type for CacheTypeVector3L entity data. ");
		}
		this.data = (Vector3Long)data;
	}
	
	@Override
	public MetaDataElement clone() {
		return new MetaDataVec3LElement(data);
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(data);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		data = (Vector3Long) in.readObject();
	}

}
