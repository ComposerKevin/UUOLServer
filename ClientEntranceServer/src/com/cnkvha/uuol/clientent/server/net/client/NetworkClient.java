package com.cnkvha.uuol.clientent.server.net.client;

import java.io.Closeable;

import com.cnkvha.uuol.clientent.server.net.ClientHandler;
import com.cnkvha.uuol.net.protocol.LanguageBindings;
import com.cnkvha.uuol.net.protocol.server.ServerDisconnectPacket;

import io.netty.channel.Channel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public class NetworkClient implements Closeable {
	
	public final static long LOGIN_TIMEOUT = 1500;
	
	private final ClientHandler handler;
	
	private final Channel chn;
	
	private PlayStage playStage;
	
	private String username;
	private String session;
	
	private GameClient game;
	
	private long timeConnected;
	
	private boolean requestedClose;

	public NetworkClient(ClientHandler handler, Channel chn) {
		this.handler = handler;
		this.chn = chn;
		timeConnected = System.currentTimeMillis();
		playStage = PlayStage.LOGIN;
		this.handler.registerClient(this);
	}
	
	public GameClient getGame() {
		return game;
	}
	
	public void setGame(GameClient game) {
		if(this.game != null){
			throw new IllegalStateException("Already set game! ");
		}
		this.game = game;
	}
	
	public String getUniqueID(){
		return chn.id().asLongText();
	}
	
	public String getUsername() {
		return username;
	}
	
	public Channel getChannel() {
		return chn;
	}
	
	public String getSession() {
		return session;
	}
	
	@Override
	public int hashCode() {
		return chn.id().asLongText().hashCode();
	}
	
	@Override
	public void close() {
		close(LanguageBindings.DISCONNECT_UNKNOWN);
	}
	
	public void close(String reason){
		//TODO: Clean up (world data, entities, ..., etc. 
		//...
		
		getChannel().writeAndFlush(new ServerDisconnectPacket(reason == null ? "" : reason)).addListener(new GenericFutureListener<Future<? super Void>>() {
			@Override
			public void operationComplete(Future<? super Void> future) {
				getChannel().close();
			}
		});
		requestedClose = true;
		handler.unregisterClient(this);
	}
	
	public boolean isRequestedClose() {
		return requestedClose;
	}
	
	public void setLoggedIn(){
		playStage = PlayStage.PLAY;
	}
	
	public PlayStage getPlayStage() {
		return playStage;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setSession(String session) {
		this.session = session;
	}
	
	public boolean isLoginTimeout(){
		if(playStage.equals(PlayStage.LOGIN) && System.currentTimeMillis() > timeConnected + LOGIN_TIMEOUT){
			return true;
		}else{
			return false;
		}
	}
}
