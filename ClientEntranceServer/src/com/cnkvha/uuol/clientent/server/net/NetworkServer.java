package com.cnkvha.uuol.clientent.server.net;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.cnkvha.uuol.clientent.server.EntranceServer;
import com.cnkvha.uuol.clientent.server.net.codec.PacketDecoder;
import com.cnkvha.uuol.clientent.server.net.codec.PacketEncoder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public final class NetworkServer {
	
	public final static int BIND_PORT = 35723;
	
	
	private final EntranceServer server;
	
	private ClientHandler cliHandler;
	
	private final EventLoopGroup bosselg = new NioEventLoopGroup();
	private final EventLoopGroup workerelg = new NioEventLoopGroup();
	
	private final ServerBootstrap bs = new ServerBootstrap();
	
	
	private final ExecutorService thPool = Executors.newFixedThreadPool(128);
	
	
	public NetworkServer(EntranceServer server) throws IOException {
		this.server = server;
		cliHandler = new ClientHandler(NetworkServer.this);
		bs.group(bosselg, workerelg)
		  .channel(NioServerSocketChannel.class)
		  .childHandler(new ChannelInitializer<SocketChannel>() {
			  protected void initChannel(SocketChannel ch) throws Exception {
				  ch.pipeline().addLast(new PacketDecoder());
				  ch.pipeline().addLast(new PacketEncoder());
				  ch.pipeline().addLast(cliHandler);
			  };
		  })
		.option(ChannelOption.SO_BACKLOG, 128)
		.childOption(ChannelOption.SO_KEEPALIVE, true);
		
		bs.bind("0.0.0.0", BIND_PORT);
	}
	
	public void shutdown(){
		try {
			bosselg.shutdownGracefully().sync();
			workerelg.shutdownGracefully().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public ExecutorService getThreadPool() {
		return thPool;
	}
	
	public EntranceServer getServer() {
		return server;
	}
	
	public ClientHandler getClientHandler() {
		return cliHandler;
	}
}
