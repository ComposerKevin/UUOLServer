package com.cnkvha.uuol.sjl.db;

public final class SQLHelper {
	public static String intEq(String field, int num){
		return "\"" + field + "\"=" + num;
	}
	
	public static String longEq(String field, long num){
		return "\"" + field + "\"=" + num;
	}
	
	public static String strEq(String field, String str){
		return "\"" + field + "\"='" + (str != null ? str : "") + "'";
	}
}
