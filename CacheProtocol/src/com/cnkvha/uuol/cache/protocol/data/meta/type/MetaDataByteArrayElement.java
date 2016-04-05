package com.cnkvha.uuol.cache.protocol.data.meta.type;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.cnkvha.uuol.cache.protocol.data.meta.MetaDataElement;
import com.cnkvha.uuol.cache.protocol.data.meta.MetaDataType;

public class MetaDataByteArrayElement implements MetaDataElement {
	private byte[] data;
	
	public MetaDataByteArrayElement() {
	}
	
	public MetaDataByteArrayElement(byte[] data){
		this.data = data;
	}
	
	@Override
	public MetaDataType dataType() {
		return MetaDataType.BYTE_ARRAY;
	}

	@Override
	public Object getData() {
		return data;
	}

	@Override
	public void setData(Object data) {
		if(!byte[].class.isInstance(data)){
			throw new IllegalArgumentException("Got non-int type for double entity data. ");
		}
		this.data = (byte[])data;
	}
	
	@Override
	public MetaDataElement clone() {
		return new MetaDataByteArrayElement(data);
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeInt(data.length);
		out.write(data);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		data = new byte[in.readInt()];
		in.readFully(data);
	}

}
