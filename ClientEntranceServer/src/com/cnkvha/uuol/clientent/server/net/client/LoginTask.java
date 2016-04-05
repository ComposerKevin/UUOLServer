package com.cnkvha.uuol.clientent.server.net.client;

import java.sql.Connection;

import com.cnkvha.uuol.clientent.server.net.client.login.GameInitializer;
import com.cnkvha.uuol.net.protocol.LanguageBindings;
import com.cnkvha.uuol.sjl.db.ConnectionPool;

public final class LoginTask implements Runnable {

	public static long LoginCounter;
	
	private final NetworkClient cli;
	
	public LoginTask(NetworkClient cli) {
		this.cli = cli;
	}

	@Override
	public void run() {
		LoginCounter++;
		long userID = -1;
		try{
			userID = go();
			if(userID == -1){
				throw new Exception("Can not fetch user data! ");
			}
		}catch(Exception e){
			cli.close(LanguageBindings.DISCONNECT_SERVER_ERROR);
			return;
		}finally{
			LoginCounter--;
		}
		GameClient game = new GameClient(cli, userID);
		cli.setGame(game);
		GameInitializer.initialize(game);
	}
	
	
	private long go() throws Exception{
		Connection conn = ConnectionPool.getConnection();
		if(conn == null){
			throw new Exception();
		}
		
	}
}
