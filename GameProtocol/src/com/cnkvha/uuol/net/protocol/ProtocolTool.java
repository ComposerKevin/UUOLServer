package com.cnkvha.uuol.net.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

public class ProtocolTool {
	public static String readString(DataInputStream buff) throws IOException {
		int len = buff.readInt();
		byte[] d = new byte[len];
		buff.read(d);
		String s = new String(d, Charset.forName("UTF-8"));
		return s;
	}
	
	public static void writeString(DataOutputStream buff, String str) throws IOException {
		if(str == null){
			buff.writeInt(0);
			return;
		}
		byte[] d = str.getBytes(Charset.forName("UTF-8"));
		buff.writeInt(d.length);
		buff.write(d);
	}
}
