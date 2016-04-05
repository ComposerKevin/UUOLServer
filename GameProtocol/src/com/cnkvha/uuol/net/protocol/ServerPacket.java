package com.cnkvha.uuol.net.protocol;

public abstract class ServerPacket extends GamePacket {
	
	public final static int PING = 0x00000000;
	
	
	//Logging in
	public final static int DISCONNECT = 0xFF0000FF;
	public final static int HANDSHAKE_REPLY = 0x000000A0;
	
	
	
	
	
	
	
	public ServerPacket(byte[] data){
		super(data);
	}
	
	public ServerPacket() {
	}
	
}
