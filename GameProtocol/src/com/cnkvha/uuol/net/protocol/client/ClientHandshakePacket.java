package com.cnkvha.uuol.net.protocol.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.cnkvha.uuol.net.protocol.ClientPacket;
import com.cnkvha.uuol.net.protocol.ProtocolTool;

public class ClientHandshakePacket extends ClientPacket {

	public String username;
	public String session;
	
	public long protocolVersion;
	
	public ClientHandshakePacket(byte[] data) {
		super(data);
	}
	
	public ClientHandshakePacket() {
	}

	@Override
	public int pid() {
		return ClientPacket.HANDSHAKE;
	}

	@Override
	protected void _encode(DataOutputStream o) throws IOException {
		ProtocolTool.writeString(o, username);
		ProtocolTool.writeString(o, session);
		o.writeLong(protocolVersion);
	}

	@Override
	public void _decode(DataInputStream d) throws IOException {
		username = ProtocolTool.readString(d);
		session = ProtocolTool.readString(d);
		protocolVersion = d.readLong();
	}

}
