package com.cnkvha.uuol.sjl.math;

public final class CoordinateConverter {
	public static Vector3Long pos2chunk(Vector3Double pos){
		Vector3Long c = new Vector3Long();
		c.x = (long)(((long) pos.x) >> 7); //  pos / 128
		c.y = (long)(((long) pos.y) >> 7);
		c.z = (long)(((long) pos.z) >> 7);
		return c;
	}
	
	public static Vector3Long chunk2section(Vector3Long chunkLocation){
		Vector3Long r = chunkLocation.clone();
		r.x /= 32;
		r.y /= 32;
		r.z /= 32;
		return r;
	}
	
	public static Vector3Long pos2section(Vector3Double pos){
		return chunk2section(pos2chunk(pos));
	}
}
