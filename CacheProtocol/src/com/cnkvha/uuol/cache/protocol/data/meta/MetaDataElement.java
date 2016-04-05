package com.cnkvha.uuol.cache.protocol.data.meta;

import java.io.Externalizable;

public interface MetaDataElement extends Externalizable {
	public MetaDataType dataType();
	
	public Object getData();
	
	public void setData(Object obj);

	public MetaDataElement clone();
}
