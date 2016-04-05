package com.cnkvha.uuol.sjl.data;

import java.util.HashSet;

public class KeyHashSet<TARGET, LOCK> extends HashSet<LOCK> {
	private static final long serialVersionUID = 4298223694941562542L;

	private TARGET key;
	
	public KeyHashSet(TARGET key) {
		this.key = key;
	}
	
	public TARGET getKey() {
		return key;
	}
	
	@Override
	public int hashCode() {
		return key.hashCode();
	}
}
