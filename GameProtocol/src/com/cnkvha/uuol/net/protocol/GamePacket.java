package com.cnkvha.uuol.net.protocol;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.cnkvha.uuol.sjl.data.ByteArrayDataOutput;


public abstract class GamePacket {
	protected byte[] buff;
	
	public GamePacket(byte[] data) {
		buff = data;
	}
	
	public GamePacket() {
	}
	
	public abstract int pid();
	
	public void encode(){
		try{
			ByteArrayDataOutput d = new ByteArrayDataOutput();
			_encode(d.getOutput());
			this.buff = d.getBytes();
		}catch(Exception e){
			this.buff = null;
		}
	}
	
	protected abstract void _encode(DataOutputStream o) throws IOException;
	
	public boolean decode(){
		try{
			if(this.buff == null) return false;
			DataInputStream dis = new DataInputStream(new ByteArrayInputStream(this.buff));
			_decode(dis);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	protected abstract void _decode(DataInputStream i) throws IOException;

	
	public byte[] getData(){
		return buff;
	}
	
	public void setData(byte[] buff) {
		this.buff = buff;
	}
}
