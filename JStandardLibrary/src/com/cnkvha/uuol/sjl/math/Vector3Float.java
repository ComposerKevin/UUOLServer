package com.cnkvha.uuol.sjl.math;

import java.io.Serializable;

public final class Vector3Float implements Serializable {
	private static final long serialVersionUID = -104831723679304278L;
	
	public float x;
	public float y;
	public float z;
	
	public Vector3Float() {
	}
	
	public Vector3Float(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public Vector3Float clone() {
		return new Vector3Float(x, y, z);
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	@Override
	public String toString() {
		return "{Vector3Float(" + x + "," + y + "," + z + ")}";
	}
}
