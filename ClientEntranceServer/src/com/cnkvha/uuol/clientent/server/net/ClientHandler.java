package com.cnkvha.uuol.clientent.server.net;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.cnkvha.uuol.clientent.server.net.client.LoginTask;
import com.cnkvha.uuol.clientent.server.net.client.NetworkClient;
import com.cnkvha.uuol.clientent.server.net.handler.ClientPacketHandler;
import com.cnkvha.uuol.net.protocol.ClientPacket;
import com.cnkvha.uuol.net.protocol.LanguageBindings;
import com.cnkvha.uuol.net.protocol.server.ServerDisconnectPacket;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public class ClientHandler extends ChannelHandlerAdapter {
	
	private final NetworkServer network;
	
	private final Map<String, NetworkClient> clients = new ConcurrentHashMap<>();
	
	public ClientHandler(NetworkServer network) {
		this.network = network;
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg){
		if(msg == null || !ClientPacket.class.isAssignableFrom(msg.getClass())){
			return;
		}
		if(!clients.containsKey(ctx.channel().id().asLongText())) return;
		NetworkClient cli = clients.get(ctx.channel().id().asLongText());
		ClientPacketHandler.Register.handle(cli, (ClientPacket)msg);
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		if(LoginTask.LoginCounter > 1024){		//Too many clients logging in. 
			ctx.writeAndFlush(new ServerDisconnectPacket(LanguageBindings.DISCONNECT_TOO_MANY_LOGINS)).addListener(new GenericFutureListener<Future<? super Void>>() {
				public void operationComplete(Future<? super Void> future) throws Exception {
					ctx.close();
				};
			});
		}
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		if(!clients.containsKey(ctx.channel().id().asLongText())) return;
		clients.get(ctx.channel().id().asLongText()).close(LanguageBindings.DISCONNECT_BY_USER);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		//Invalid client
		cause.printStackTrace();
		ctx.close();
	}
	
	
	
	
	public void registerClient(NetworkClient cli){
		if(cli.isRequestedClose()) return;
		if(!clients.containsKey(cli.getUniqueID())){
			clients.put(cli.getUniqueID(), cli);
		}
	}
	
	public void unregisterClient(NetworkClient cli){
		if(!cli.isRequestedClose()) return;
		if(clients.containsKey(cli.getUniqueID())){
			clients.remove(cli.getUniqueID());
		}
	}
	
	
	
	
	
	
	
	public NetworkServer getNetwork() {
		return network;
	}
}
