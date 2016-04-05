package com.cnkvha.uuol.sjl.math;

import java.io.Serializable;

public final class Vector3Double implements Serializable {
	private static final long serialVersionUID = -4123785530122449293L;
	
	public double x;
	public double y;
	public double z;
	
	public Vector3Double() {
	}
	
	public Vector3Double(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public Vector3Double clone() {
		return new Vector3Double(x, y, z);
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	@Override
	public String toString() {
		return "{Vector3Double(" + x + "," + y + "," + z + ")}";
	}
}
