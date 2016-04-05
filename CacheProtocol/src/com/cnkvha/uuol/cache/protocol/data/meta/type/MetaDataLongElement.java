package com.cnkvha.uuol.cache.protocol.data.meta.type;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.cnkvha.uuol.cache.protocol.data.meta.MetaDataElement;
import com.cnkvha.uuol.cache.protocol.data.meta.MetaDataType;

public class MetaDataLongElement implements MetaDataElement {

	private long data;
	
	public MetaDataLongElement() {
	}
	
	public MetaDataLongElement(long data){
		this.data = data;
	}
	
	@Override
	public MetaDataType dataType() {
		return MetaDataType.LONG;
	}

	@Override
	public Object getData() {
		return data;
	}

	@Override
	public void setData(Object data) {
		if(!long.class.isInstance(data)){
			throw new IllegalArgumentException("Got non-int type for long entity data. ");
		}
		this.data = (long)data;
	}
	
	@Override
	public MetaDataElement clone() {
		return new MetaDataLongElement(data);
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeLong(data);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		data = in.readLong();
	}

}
