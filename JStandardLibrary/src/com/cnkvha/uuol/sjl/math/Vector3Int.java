package com.cnkvha.uuol.sjl.math;

import java.io.Serializable;

public final class Vector3Int implements Serializable {
	private static final long serialVersionUID = 144642519702411482L;
	
	public int x;
	public int y;
	public int z;
	
	public Vector3Int() {
	}
	
	public Vector3Int(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public Vector3Int clone() {
		return new Vector3Int(x, y, z);
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	@Override
	public String toString() {
		return "{Vector3Int(" + x + "," + y + "," + z + ")}";
	}
}
