package com.cnkvha.uuol.sjl.math;

import java.io.Serializable;

public final class Vector3Long implements Serializable {
	private static final long serialVersionUID = 6941436897726486299L;
	
	public long x;
	public long y;
	public long z;
	
	public Vector3Long() {
	}
	
	public Vector3Long(long x, long y, long z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public Vector3Long clone() {
		return new Vector3Long(x, y, z);
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	@Override
	public String toString() {
		return "{Vector3Long(" + x + "," + y + "," + z + ")}";
	}
}
