package com.cnkvha.uuol.sjl.world;

import com.cnkvha.uuol.sjl.math.Vector3Long;

public final class ChunkUtils {
	public static int positionToIndex(int x, int y, int z){
		int target = 0;
		target |= (x & 0xFF) << 16;
		target |= (y & 0xFF) << 8;
		target |= (z & 0xFF);
		return target;
	}
	
	public static Vector3Long globalToChunkPos(long x, long y, long z){
		return new Vector3Long(x >> 7, y >> 7 , z >> 7);
	}
	
	public static Vector3Long globalToChunkPos(Vector3Long pos){
		return new Vector3Long(pos.x >> 7, pos.y >> 7, pos.z >> 7);
	}
	
	public static long globalToChunkPos(long n){
		return n >> 7;
	}
}
