package com.cnkvha.uuol.net.protocol;

public abstract class ClientPacket extends GamePacket {
	
	public final static int PONG = 0x00000000;
	
	
	//Logging in
	public final static int HANDSHAKE = 0x000000A0;
	
	
	public ClientPacket(byte[] data){
		super(data);
	}
	
	public ClientPacket() {
	}
	
}
