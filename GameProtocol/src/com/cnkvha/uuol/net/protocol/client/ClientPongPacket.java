package com.cnkvha.uuol.net.protocol.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.cnkvha.uuol.net.protocol.ClientPacket;

public class ClientPongPacket extends ClientPacket {

	public ClientPongPacket(byte[] data) {
		super(data);
	}
	
	public ClientPongPacket() {
	}

	@Override
	public int pid() {
		return ClientPacket.PONG;
	}

	@Override
	protected void _encode(DataOutputStream o) throws IOException {
	}

	@Override
	public void _decode(DataInputStream i) {
	}

}
