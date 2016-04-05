package com.cnkvha.uuol.cache.protocol.data;

import java.io.Serializable;
import java.util.UUID;

public final class RequestIdentifier implements Serializable {
	private static final long serialVersionUID = 6696998124450903541L;
	
	
	public String sender;
	
	public UUID id;
	
	public RequestIdentifier() {
		this.id = UUID.randomUUID();
	}
	
	public RequestIdentifier(String sender){
		this(sender, UUID.randomUUID());
	}
	
	public RequestIdentifier(String sender, UUID id) {
		this.sender = sender;
		this.id = id;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(!RequestIdentifier.class.isInstance(obj)) return false;
		RequestIdentifier id = (RequestIdentifier)obj;
		if(this.id.equals(id.id) && this.sender.equals(id.sender)){
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return (sender + id.toString()).hashCode();
	}
	
	@Override
	public String toString() {
		return id.toString();
	}
}
