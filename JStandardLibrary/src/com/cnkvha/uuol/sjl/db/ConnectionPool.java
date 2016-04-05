package com.cnkvha.uuol.sjl.db;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;

public final class ConnectionPool {
	private final static BasicDataSource ds = new BasicDataSource();

	private final static String DB_DRIVER = "oracle.jdbc.driver.OracleDriver";
	
	private final static String DB_ADDRESS = "127.0.0.1:1521";
	
	static {
		ds.setDriverClassName(DB_DRIVER);
		ds.setUrl("jdbc:oracle:thin:@" + DB_ADDRESS + ":ORCL");
		ds.setUsername("C##UUOL");
		ds.setPassword("123456");
		ds.setInitialSize(16);
		ds.setMinIdle(8);
		ds.setMaxIdle(32);
		ds.setDefaultAutoCommit(false);
		ds.setEnableAutoCommitOnReturn(true);
		ds.setMaxWaitMillis(2000);
		ds.setMaxTotal(64);
	}
	
	public static boolean loadDriver(){
		try{
			Class.forName(DB_DRIVER);
			Class.forName(DB_DRIVER).newInstance();
			return true;
		} catch (Exception e){
			return false;
		}
	}

	public synchronized static Connection getConnection() {
		try {
			return ds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static boolean test() {
		try {
			Connection conn = getConnection();
			if (conn == null || conn.isClosed()) {
				return false;
			} else {
				conn.close();
				return true;
			}
		} catch (Exception e) {
			return false;
		}
	}
}
