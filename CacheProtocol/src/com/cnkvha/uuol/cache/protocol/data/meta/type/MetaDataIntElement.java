package com.cnkvha.uuol.cache.protocol.data.meta.type;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.cnkvha.uuol.cache.protocol.data.meta.MetaDataElement;
import com.cnkvha.uuol.cache.protocol.data.meta.MetaDataType;

public class MetaDataIntElement implements MetaDataElement {

	private int data;
	
	public MetaDataIntElement() {
	}
	
	public MetaDataIntElement(int data){
		this.data = data;
	}
	
	@Override
	public MetaDataType dataType() {
		return MetaDataType.INT;
	}

	@Override
	public Object getData() {
		return data;
	}

	@Override
	public void setData(Object data) {
		if(!int.class.isInstance(data)){
			throw new IllegalArgumentException("Got non-int type for int entity data. ");
		}
		this.data = (int)data;
	}
	
	@Override
	public MetaDataElement clone() {
		return new MetaDataIntElement(data);
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeInt(data);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		data = in.readInt();
	}

}
