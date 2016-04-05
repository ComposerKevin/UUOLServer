package com.cnkvha.uuol.cache.protocol.data.meta.type;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.cnkvha.uuol.cache.protocol.data.meta.MetaDataElement;
import com.cnkvha.uuol.cache.protocol.data.meta.MetaDataType;

public class MetaDataStringElement implements MetaDataElement {

	private String data;
	
	public MetaDataStringElement() {
	}
	
	public MetaDataStringElement(String data){
		this.data = data;
	}
	
	@Override
	public MetaDataType dataType() {
		return MetaDataType.STRING;
	}

	@Override
	public Object getData() {
		return data;
	}

	@Override
	public void setData(Object data) {
		if(!String.class.isInstance(data)){
			throw new IllegalArgumentException("Got non-int type for String entity data. ");
		}
		this.data = (String)data;
	}
	
	@Override
	public MetaDataElement clone() {
		return new MetaDataStringElement(data);
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeUTF(data);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		data = in.readUTF();
	}

}
