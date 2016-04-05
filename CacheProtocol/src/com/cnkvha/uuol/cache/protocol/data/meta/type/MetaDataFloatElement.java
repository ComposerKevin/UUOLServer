package com.cnkvha.uuol.cache.protocol.data.meta.type;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.cnkvha.uuol.cache.protocol.data.meta.MetaDataElement;
import com.cnkvha.uuol.cache.protocol.data.meta.MetaDataType;

public class MetaDataFloatElement implements MetaDataElement {

	private float data;
	
	public MetaDataFloatElement() {
	}
	
	public MetaDataFloatElement(float data){
		this.data = data;
	}
	
	@Override
	public MetaDataType dataType() {
		return MetaDataType.FLOAT;
	}

	@Override
	public Object getData() {
		return data;
	}

	@Override
	public void setData(Object data) {
		if(!float.class.isInstance(data)){
			throw new IllegalArgumentException("Got non-int type for int entity data. ");
		}
		this.data = (float)data;
	}
	
	@Override
	public MetaDataElement clone() {
		return new MetaDataFloatElement(data);
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeFloat(data);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		data = in.readFloat();
	}

}
