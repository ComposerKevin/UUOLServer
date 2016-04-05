package com.cnkvha.uuol.clientent.server.net.codec;

import com.cnkvha.uuol.net.protocol.GamePacket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<GamePacket> {

	@Override
	protected void encode(ChannelHandlerContext ctx, GamePacket msg, ByteBuf out)
			throws Exception {
		msg.encode();
		if(msg.getData() == null || msg.getData().length == 0){
			return;
		}
		out.writeInt(msg.getData().length);
		out.writeInt(msg.pid());
		out.writeBytes(msg.getData());
	}

}
