package com.cnkvha.uuol.clientent.server.net.codec;

import java.util.List;

import com.cnkvha.uuol.net.protocol.ClientPacket;
import com.cnkvha.uuol.net.protocol.Protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class PacketDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) throws Exception {
		if(in.readableBytes() < 8){
			return;	//Not enough bytes
		}
		int len = in.readInt();		//Packet data WITHOUT pid and length
		if(in.readableBytes() < len + 4){
			in.resetReaderIndex();
			return;
		}
		if(len > 4) return;
		
		int pid = in.readInt();	//4
		
		byte[] data = new byte[len];
		in.readBytes(data);
		
		ClientPacket pk = Protocol.decodeClient(pid, data);
		if(pk != null){
			out.add(pk);
		}
	}

}
