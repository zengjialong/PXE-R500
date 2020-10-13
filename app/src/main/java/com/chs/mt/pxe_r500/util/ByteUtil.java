package com.chs.mt.pxe_r500.util;

import java.nio.ByteBuffer;

public class ByteUtil {
	static public ByteBuffer appendBytes(ByteBuffer byteBuffer, byte[] bytes){
		ByteBuffer ret = ByteBuffer.allocate(byteBuffer.capacity()+bytes.length);
		ret.position(0);
		ret.put(byteBuffer.array());
		ret.position(byteBuffer.capacity());
		ret.put(bytes);
		return ret;
	}
	
	static public byte[] int2Bytes(int n){
		byte[] tmp = {(byte)(n&0xff), (byte)((n>>8)&0xff), (byte)((n>>16)&0xff), (byte)((n>>24)&0xff)};
		return tmp;
	}
	
	static public int bytes2int(byte[] data){
		return ((data[3]&0xff)<<24)+((data[2]&0xff)<<16) + ((data[1]&0xff)<<8) + (data[0]&0xff);
	}
	
	static public byte[] short2Bytes(short s){
		byte[] tmp = {(byte)(s&0xff), (byte)((s>>8)&0xff)};
		return tmp;
	}
	
	static public short bytes2Short(byte[] data){
		return (short)(((data[1]&0xff)<<8)+(data[0]&0xff));
	}
	
	static public byte[] range(byte[] data, int startIndex, int length){
		byte[] tmp = new byte[length];
		for(int i = 0; i < length; i++){
			tmp[i] = data[startIndex+i];
		}
		return tmp;
	}
	public static String ByteArrToHex(byte[] inBytArr){
		StringBuilder strBuilder=new StringBuilder();
		int j=inBytArr.length;
		for (int i = 0; i < j; i++) {
			strBuilder.append(Byte2Hex(inBytArr[i]));
			strBuilder.append(" ");
		}
		return strBuilder.toString();
	}
	public static String Byte2Hex(Byte inByte){
		return String.format("%02x", inByte).toUpperCase();
	}

}
