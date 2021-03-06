package com.cnkvha.uuol.sjl.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public final class SerializationTool {
	
	/*
	 * We have a customized data structure here
	 * Original Size(INT) | Compressed Serialized Data
	 */
	
	public static Object decode(byte[] data) throws Exception{
		//Decompress
		Inflater inf = new Inflater();
		inf.setInput(data, 4, data.length - 4);
		
		int originSize = ((data[0] & 0xFF) << 24) | ((data[1] & 0xFF) << 16) | ((data[2] & 0xFF) << 8) | (data[3] & 0xFF);
	
		byte[] buff = new byte[originSize];
		inf.inflate(buff);
		inf.end();
		
		//Decode
		ObjectInputStream obj = new ObjectInputStream(new ByteArrayInputStream(buff));
		Object target = obj.readObject();
		obj.close();
		return target;
	}
	
	public static byte[] encode(Serializable msg, int bufferSize) throws IOException {
		//Encode
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oup = new ObjectOutputStream(bos);
		oup.writeObject(msg);
		oup.flush();
		byte[] data = bos.toByteArray();
		oup.close();
		bos.close();
		//Compress
		Deflater def = new Deflater(4);
		def.setInput(data);
		byte[] buff = new byte[4 + bufferSize];
		def.finish();
		int size = def.deflate(buff, 4, buff.length - 4);
		def.end();
		buff[0] = (byte)((data.length >> 24) & 0xFF);
		buff[1] = (byte)((data.length >> 16) & 0xFF);
		buff[2] = (byte)((data.length >> 8) & 0xFF);
		buff[3] = (byte)(data.length & 0xFF);
		buff = Arrays.copyOfRange(buff, 0, size + 4);
		return buff;
	}
	
	public static byte[] encode(Serializable msg) throws IOException {
		return encode(msg, 32768);
	}
}
