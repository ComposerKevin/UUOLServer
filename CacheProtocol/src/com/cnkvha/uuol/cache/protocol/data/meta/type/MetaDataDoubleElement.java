package com.cnkvha.uuol.cache.protocol.data.meta.type;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.cnkvha.uuol.cache.protocol.data.meta.MetaDataElement;
import com.cnkvha.uuol.cache.protocol.data.meta.MetaDataType;

public class MetaDataDoubleElement implements MetaDataElement {

	private double data;
	
	public MetaDataDoubleElement() {
	}
	
	public MetaDataDoubleElement(double data){
		this.data = data;
	}
	
	@Override
	public MetaDataType dataType() {
		return MetaDataType.DOUBLE;
	}

	@Override
	public Object getData() {
		return data;
	}

	@Override
	public void setData(Object data) {
		if(!double.class.isInstance(data)){
			throw new IllegalArgumentException("Got non-int type for double entity data. ");
		}
		this.data = (double)data;
	}
	
	@Override
	public MetaDataElement clone() {
		return new MetaDataDoubleElement(data);
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeDouble(data);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		data = in.readDouble();
	}

}
