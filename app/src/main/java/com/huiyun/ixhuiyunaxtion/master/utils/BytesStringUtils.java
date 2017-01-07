package com.huiyun.ixhuiyunaxtion.master.utils;

public class BytesStringUtils {
	public static boolean udpHexShow = false;
	public static boolean tcpHexShow = true;
	
	
	/** char数组转byte数组 */
	public static byte[] transCAtoBA(char[] c, int length){
		byte[] b = new byte[length];
		for(int i=0; i<b.length; i++)
			b[i] = (byte) c[i];
		return b;
	}
	
	/** byte数组转char数组 */
	public static char[] transBAtoCA(byte[] b, int length){
		char[] c = new char[length];
		for(int i=0; i<c.length; i++)
			c[i] = (char) b[i];
		return c;
	}
	
	/** byte数组转字符串 */
	public static String transByteArrayToString(byte[] b){
		char[] c = new char[b.length/2];
		for(int i=0; i<c.length; i++){
			c[i] = (char) ((((char)b[i*2])<<8 & 0xff00)
					| ((char)b[i*2+1] & 0xff));
		}
		String str = String.valueOf(c);
		return str;
	}
	
	
	// 将ip地址转换成int类型，0表示失败
	public static int changeIpAddrToInteger(String s){
		try {
			String[] s2 = s.split("\\.");
			if(s2.length != 4) return 0;
			int i, a, b, c, d;
			a = Integer.parseInt(s2[0]);
			b = Integer.parseInt(s2[1]);
			c = Integer.parseInt(s2[2]);
			d = Integer.parseInt(s2[3]);
			i = ((a<<24) & 0xff000000) | ((b<<16) & 0xff0000) | ((c<<8) & 0xff00) | (d & 0xff);
			return i;
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	
	
	/** 把带空格的如12 23 42 23 11转成byte数组
	 * @param s
	 * @return
	 */
	public static byte[] toArrayByteShow(String s){
		try {
			String[] s2 = s.trim().split(" ");
			byte[] b = new byte[s2.length];
			for(int i=0; i<b.length; i++){
				b[i] = (byte) Integer.parseInt(s2[i],16);
			}
			return b;
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	/**
	 * 把byte数组转成12 23 44 21 11格式的字符串
	 * @param ba
	 * @return
	 */
	public static String toStringShow(byte[] ba){
		String s = "";
		for(byte b:ba){
			s += String.format("%02X ", b);
		}
		return s;
	}
	
	/** 把byte数组转成1223442111格式的字符串
	 * @param ba
	 * @return
	 */
	public static String toStringShowWithoutSpace(byte[] ba, int start, int length){
		String s = "";
		for(int i=start; i<start+length; i++){
			s += String.format("%02X", ba[i]);
		}
		return s;
	}
	
	/**
     * 从一个byte[]数组中截取一部分
     * @param src
     * @param begin
     * @param count
     * @return
     */
    public static byte[] subBytes(byte[] src, int begin, int count) {
        byte[] bs = new byte[count];
        for (int i=begin; i<begin+count; i++) bs[i-begin] = src[i];
        return bs;
    }
	
    /** 比较两个byte数组是否相同
     * @param a
     * @param b
     * @return
     */
    public static boolean compareByteArray(byte[] a, byte[] b){
    	if(a.length == b.length){
    		for(int i=0; i<a.length; i++){
    			if(a[i] != b[i]){
    				return false;
    			}
    		}
    		return true;
    	}
    	return false;
    }
    
	/**
	 * DPCI校验码计算
	 * 
	 * @param end_index
	 */
	public static char caCrc(byte[] buf, int start_index, int end_index) {
		int crc1 = 0x00;
		char crc = 0x00;
		for (int i = start_index; i <end_index+1; i++) {
			crc1 = (buf[i]^0x86)+crc;
		    crc = (char)(crc1&0xff);
		}
		return crc;
	}
	/** 将byte转换成无符号的int型 */
	public static int toUnsignedInt(byte b){
		int i = b;
		if(i < 0) i+=256;
		return i;
	}
}
