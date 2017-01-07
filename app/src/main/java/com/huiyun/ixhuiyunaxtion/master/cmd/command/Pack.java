package com.huiyun.ixhuiyunaxtion.master.cmd.command;


import com.huiyun.ixhuiyunaxtion.master.utils.BytesStringUtils;

/**
 * 负责对CMD进行打包，或者把已经打包的数据解包成CMD
 *
 */
public class Pack {
	//
	// 数据打包
	//
	// 起始码0xFE
	// custom_code  由其他协议自定义的
	// data_length  输入数据的长度
	// in_buf       要打包的数据
	// out_buf      打包后的数据
	//
	public static byte[] Data_Pack(int custom_code, int data_length, byte[] in_buf)
	{
		byte[] out_buf = new byte[Data.PACK_EXTRA_LENGTH + data_length];
		
	    int crc_temp;
	    out_buf[0]=(byte) 0xfe;
	    out_buf[1]=(byte) custom_code;
	    out_buf[2]=(byte) data_length;
	    for(int i=0; i<data_length; i++)
	    	out_buf[3+i] = in_buf[i];
	    crc_temp=CRC.caCrc16(out_buf, 0, data_length+2);
	    out_buf[3+data_length]=(byte) ((crc_temp >> 8) & 0xff);
	    out_buf[4+data_length]=(byte) (crc_temp & 0xff);
	    
	    return out_buf;
	}
	
	public static byte[] Data_Pack(int custom_code, byte[] in_buf)
	{
		return Data_Pack(custom_code, in_buf.length, in_buf);
	}

	//
	// 数据解包
	//
	// 起始码0xFE
	// custom_code    由其他协议定义的
	// in_buf         解包前的数据
	// out_buf        解包后的数据
	//
	public static byte[] Data_Unpack(int length, byte[] in_buf)
	{
		if(length <= Data.PACK_EXTRA_LENGTH) return null;
		byte[] out_buf = new byte[length-Data.PACK_EXTRA_LENGTH];
	
	    int crc_temp,lenData = BytesStringUtils.toUnsignedInt(in_buf[2]);
	    crc_temp=CRC.caCrc16(in_buf, 0, lenData+2);
	    if(in_buf[0] == (byte) 0xfe &&
	    		(byte)((crc_temp>>8) & 0xff) == (byte)in_buf[lenData+3] && 
	    		(byte)(crc_temp & 0xff) == (byte)in_buf[lenData+4] &&
	    		length-Data.PACK_EXTRA_LENGTH == lenData)
	    {
	        for(int i=0; i<length-5; i++)
	        	out_buf[i] = in_buf[i+3];
	        return out_buf;
	    }
	    else
	    {
	        return null;
	    }
	}
	
	public static byte[] Data_Unpack(byte[] in_buf){
		return Data_Unpack(in_buf.length, in_buf);
	}
	
	public static boolean Check_CustomCode(byte[] buf, int custom_code){
		if(buf.length <= Data.PACK_EXTRA_LENGTH)
			return false;
		if(buf[1] == (byte)custom_code)
			return true;
		return false;
	}
	
	public static int Get_CustomCode(byte[] buf){
		return BytesStringUtils.toUnsignedInt(buf[1]);
	}
}
