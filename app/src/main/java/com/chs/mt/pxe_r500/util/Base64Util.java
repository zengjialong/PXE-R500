package com.chs.mt.pxe_r500.util;

import java.nio.*;

public class Base64Util {
	static private char[] base64Chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
	static public String encode(byte[] data) {
		String ret = "";
		for(int i = 0; i < data.length/3; i++)
		{
			ret = String.format("%s%c%c%c%c", ret, base64Chars[(data[3*i]>>2)&0x3f]
																					 , base64Chars[((data[3*i]<<4)&0x30)+((data[3*i+1]>>4)&0x0f)]
																					 , base64Chars[((data[3*i+1]<<2)&0x3c)+((data[3*i+2]>>6)&0x03)]
																					 , base64Chars[data[3*i+2]&0x3f]);
		}
		if(data.length%3==1){
			ret = String.format("%s%c%c==", ret, base64Chars[(data[data.length-1]>>2)&0x3f], base64Chars[(data[data.length-1]<<4)&0x30]);
		}
		else if(data.length%3==2) {
			ret = String.format("%s%c%c%c=", ret, base64Chars[(data[data.length-2]>>2)&0x3f]
																					, base64Chars[((data[data.length-2]<<4)&0x30)+((data[data.length-1]>>4)&0x0f)]
																					, base64Chars[(data[data.length-1]<<2)&0x3c]);
		}
		return ret;
	}
	
	static private byte getPos(char ch) {
		byte ret = 0;
		for(byte i = 0; i < base64Chars.length; i++) {
			if (base64Chars[i]==ch) {
				ret = i;
				break;
			}
		}
		
		return ret;
	}
	
	static public byte[] decode(String encodedStr) {
		ByteBuffer ret = ByteBuffer.allocate(0);
		for(int i = 0; i < encodedStr.length();i++){
			byte pos1 = getPos(encodedStr.charAt(4*i));
			byte pos2 = getPos(encodedStr.charAt(4*i+1));
			ret = ByteUtil.appendBytes(ret, new byte[]{(byte)((pos1<<2)+(pos2>>4))});
			if( encodedStr.charAt(4*i+2)!='=') {	
				byte pos3 = getPos(encodedStr.charAt(4*i+2));
				ret = ByteUtil.appendBytes(ret, new byte[]{(byte)((pos2<<4)+(pos3>>2))});
				if(encodedStr.charAt(4*i+3)!='=') {
					byte pos4 = getPos(encodedStr.charAt(4*i+3));
					ret = ByteUtil.appendBytes(ret, new byte[]{(byte)((pos3<<6)+pos4)});
				}
				else
					break;
			}
			else
				break;
			
		}
		return ret.array();
	}
}
