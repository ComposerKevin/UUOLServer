package com.cnkvha.uuol.clientent.server.net.handler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.cnkvha.uuol.clientent.server.net.client.NetworkClient;
import com.cnkvha.uuol.clientent.server.net.handler.login.HandlerHandshake;
import com.cnkvha.uuol.net.protocol.ClientPacket;
import com.cnkvha.uuol.net.protocol.LanguageBindings;

public abstract class ClientPacketHandler<P extends ClientPacket> {
	
	public abstract void handle(NetworkClient client, P pk);
	
	
	
	
	
	
	
	
	public static class Register{
		
		private final static Map<Integer, ClientPacketHandler<? extends ClientPacket>> handlers = new ConcurrentHashMap<>();
		
		static{
			register(ClientPacket.HANDSHAKE, new HandlerHandshake());
		}
		
		private static void register(Integer pid, ClientPacketHandler<? extends ClientPacket> hdl){
			handlers.put(pid, hdl);
		}
		
		public static void handle(NetworkClient cli, ClientPacket pk){
			if(!handlers.containsKey(pk.pid())){
				cli.close(LanguageBindings.DISCONNECT_CLIENT_ERROR);
				return;
			}
			@SuppressWarnings("unchecked")
			ClientPacketHandler<ClientPacket> hdl = (ClientPacketHandler<ClientPacket>) handlers.get(cli.getUniqueID());
			hdl.handle(cli, pk);
		}
	}
	
	
}
