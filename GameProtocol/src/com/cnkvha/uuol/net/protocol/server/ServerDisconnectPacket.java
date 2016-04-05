package com.cnkvha.uuol.net.protocol.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.cnkvha.uuol.net.protocol.ProtocolTool;
import com.cnkvha.uuol.net.protocol.ServerPacket;

public class ServerDisconnectPacket extends ServerPacket {

	public String reason;
	
	public ServerDisconnectPacket() {
	}
	
	public ServerDisconnectPacket(String reason){
		this.reason = reason == null ? "" : reason;
	}
	
	public ServerDisconnectPacket(byte[] data){
		super(data);
	}
	
	@Override
	public int pid() {
		return ServerPacket.DISCONNECT;
	}

	@Override
	protected void _encode(DataOutputStream o) throws IOException {
		ProtocolTool.writeString(o, reason);
	}

	@Override
	protected void _decode(DataInputStream i) throws IOException {
		reason = ProtocolTool.readString(i);
	}

}
