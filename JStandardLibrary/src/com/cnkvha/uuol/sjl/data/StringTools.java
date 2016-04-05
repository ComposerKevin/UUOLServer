package com.cnkvha.uuol.sjl.data;

import java.util.Random;

public final class StringTools {
	public static long rndLong(){
		Random rnd = new Random(System.currentTimeMillis());
		return rnd.nextLong();
	}
	
	public static String rndString(){
		return rndString(8);
	}
	
	private static char[] PRINTABLE_CHARS = new char[]{
		'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
		'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
		'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7',
		'8', '9', '_'
	};
	
	public static String rndString(int len){
		StringBuilder b = new StringBuilder();
		Random rnd = new Random(System.currentTimeMillis());
		for(int i = 0; i < len; i++){
			b.append(PRINTABLE_CHARS[rnd.nextInt(PRINTABLE_CHARS.length)]);
		}
		return b.toString();
	}
}
