package com.cnkvha.uuol.net.protocol;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.cnkvha.uuol.net.protocol.client.ClientHandshakePacket;
import com.cnkvha.uuol.net.protocol.client.ClientPongPacket;

public final class Protocol {
	
	//Packets from clients
	protected static final Map<Integer, Class<? extends ClientPacket>> map_cli = new ConcurrentHashMap<>();
	
	//Packets from server
	protected static final Map<Integer, Class<? extends ServerPacket>> map_svr = new ConcurrentHashMap<>();
	
	static{
		regCli();
		regSvr();
	}
	
	/**
	 * Registers packets from client
	 */
	private static void regCli(){
		map_cli.put(ClientPacket.HANDSHAKE, ClientHandshakePacket.class);
		map_cli.put(ClientPacket.PONG, ClientPongPacket.class);
	}
	
	/**
	 * Registers packets from server
	 */
	private static void regSvr(){
		
	}
	
	public static ClientPacket decodeClient(int pid, byte[] data){
		if(!map_cli.containsKey(pid)){
			return null;
		}
		Class<? extends ClientPacket> cpk = map_cli.get(new Integer(pid));
		ClientPacket pk = null;
		try {
			pk = cpk.getConstructor(byte[].class).newInstance(data);
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			return null;
		}
		if(!pk.decode()){
			return null;
		}
		return pk;
	}
}
