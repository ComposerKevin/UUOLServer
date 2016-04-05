package com.cnkvha.uuol.cache.protocol.data.meta.type;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.cnkvha.uuol.cache.protocol.data.meta.MetaDataElement;
import com.cnkvha.uuol.cache.protocol.data.meta.MetaDataType;
import com.cnkvha.uuol.sjl.math.Vector3Double;

public class MetaDataVec3DElement implements MetaDataElement {

	private Vector3Double data;
	
	public MetaDataVec3DElement() {
	}
	
	public MetaDataVec3DElement(Vector3Double data){
		this.data = data;
	}
	
	@Override
	public MetaDataType dataType() {
		return MetaDataType.VEC3_DOUBLE;
	}

	@Override
	public Object getData() {
		return data;
	}

	@Override
	public void setData(Object data) {
		if(!Vector3Double.class.isInstance(data)){
			throw new IllegalArgumentException("Got non-int type for CacheTypeVector3D entity data. ");
		}
		this.data = (Vector3Double)data;
	}
	
	@Override
	public MetaDataElement clone() {
		return new MetaDataVec3DElement(data);
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(data);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		data = (Vector3Double) in.readObject();
	}

}
