package com.huiyun.ixhuiyunaxtion.crypt;

import java.io.UnsupportedEncodingException;

/**   
 * @Description: RC4的JAVA版，无需调用JNI 
 * @date 2015-3-12 下午4:03:52 
 * @version V1.0   
 */
public class RC4JAVA {
	
	private static String key = "huiyunix5201314";
	
	private static RC4JAVA instance;
	public static RC4JAVA getInstance(){
		synchronized (RC4JAVA.class) {
			if(instance == null){
				instance = new RC4JAVA();
			}
			
			return instance;
		}
	}
	
	private byte[] keyAfterEncrypt;
	
	private RC4JAVA(){
		try {
			keyAfterEncrypt = initKey(key.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public byte[] crypt(byte[] val, int len){
		return crypt(val,keyAfterEncrypt,len);
	}
	
	// 初始化key
	private byte[] initKey(byte[] key){
		int i=0, j=0;
		char[] k = new char[256];
		char[] s = new char[256];
		char tmp = 0;
		for(i=0; i<256; i++){
			s[i] = (char) i;
			k[i] = (char)key[i%key.length];
		}
		for(i=0; i<256; i++){
			j=(j+s[i]+k[i])%256;
			tmp = s[i];
			s[i] = s[j];
			s[j] = tmp;
		}
		
		byte[] sForByte = new byte[256];
		for(i=0;i<256;i++){
			sForByte[i] = (byte) s[i];
		}
		return sForByte;
	}
	
	 /**
	 *  Function:加密 & 解密
	 *  @return
	 */
	public byte[] crypt(byte[] data, byte[] s, int len){
		int i = 0, j = 0, t = 0;
		char tmp = 0;
		
		char[] sForChar = new char[s.length];
		for(int k=0; k<sForChar.length; k++){
			sForChar[k] = (char) s[k];
		}
		char[] dataCrypt = new char[len];
		for(int k=0; k<len; k++){
			dataCrypt[k] = (char) data[k];
		}
		
		for(int k=0; k<len; k++){
			i = (i+1)%256;
			j = (j+sForChar[i])%256;
			tmp = sForChar[i];
			sForChar[i] = sForChar[j];
			sForChar[j] = tmp;
			t = (sForChar[i] + sForChar[j])%256;
			dataCrypt[k] ^= sForChar[t];
		}
		
		byte[] dataCryptForByte = new byte[len];
		for(int k=0; k<len; k++){
			dataCryptForByte[k] = (byte) dataCrypt[k];
		}
		
		return dataCryptForByte;
	}
	
	/** 字符串转byte数组 */
	@SuppressWarnings("unused")
	private static byte[] transStringToByteArray(String str){
		char[] c = str.toCharArray();
		byte[] b = new byte[c.length*2];
		for (int i = 0; i < b.length; i=i+2){
			b[i] = (byte) (c[i/2]>>8 & 0xff);
			b[i+1] = (byte) (c[i/2] & 0xff);
		}
		return b;
	}
	
	/** byte数组转字符串 */
	@SuppressWarnings("unused")
	private static String transByteArrayToString(byte[] b){
		char[] c = new char[b.length/2];
		for(int i=0; i<c.length; i++){
			c[i] = (char) ((((char)b[i*2])<<8 & 0xff00)
					| ((char)b[i*2+1] & 0xff));
		}
		String str = String.valueOf(c);
		return str;
	}
}
