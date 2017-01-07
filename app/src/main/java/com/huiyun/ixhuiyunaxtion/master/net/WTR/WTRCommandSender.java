package com.huiyun.ixhuiyunaxtion.master.net.WTR;


import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;
import com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer;
import com.huiyun.ixhuiyunaxtion.master.cmd.command.Data;
import com.huiyun.ixhuiyunaxtion.master.cmd.command.Pack;
import com.huiyun.ixhuiyunaxtion.master.net.tcp.TcpConnectionManager;

/**
 * @Description: 负责向射频中转器发送命令
 * @date 2015-3-24 上午11:30:58 
 * @version V1.0   
 */
public class WTRCommandSender implements WTR {
	
	 /**
	 *  Function: 获取设备信息
	 *  @author lzn 
	 *  2015-3-24 下午2:48:34
	 *  @param socketId
	 */
	public static void getDeviceInfo(int socketId){
		byte[] cmd = new byte[1];
		cmd[P_FUNC_CODE] = FUNC_CODE_DEVINFO;
		
		TcpConnectionManager.getInstance().sendWTRCmd(
				socketId, Pack.Data_Pack(CUSTOM_CODE_SET, cmd));
	}
	
	
	 /**
	 *  Function: 让设备闪烁
	 *  @author lzn 
	 *  2015-3-24 下午2:50:59
	 *  @param socketId
	 */
	public static void blink(int socketId){
		byte[] cmd = new byte[1];
		cmd[P_FUNC_CODE] = FUNC_CODE_BLINK;
		
		TcpConnectionManager.getInstance().sendWTRCmd(
				socketId, Pack.Data_Pack(CUSTOM_CODE_SET, cmd));
	}
	
	
	 /**
	 *  Function: 写入设定
	 *  @author lzn 
	 *  2015-3-24 下午2:51:10
	 *  @param socketId
	 *  @param addr
	 *  @param key
	 */
	public static void writeSetting(int socketId, int addr, int key){
		byte[] cmd = new byte[3];
		cmd[P_FUNC_CODE] = FUNC_CODE_WRITE;
		cmd[1] = (byte) addr;
		cmd[2] = (byte) key;
		
		TcpConnectionManager.getInstance().sendWTRCmd(
				socketId, Pack.Data_Pack(CUSTOM_CODE_SET, cmd));
		
		MainActivity.showString("发送写入设定命令,地址为" + addr + "，key值为" + key,
				MultiTextBuffer.TYPE_OTHER);
	}
	
	
	 /**
	 *  Function: 读出设定
	 *  @author lzn 
	 *  2015-3-24 下午2:51:23
	 *  @param socketId
	 */
	public static void readSetting(int socketId){
		byte[] cmd = new byte[1];
		cmd[P_FUNC_CODE] = FUNC_CODE_READ;
		
		TcpConnectionManager.getInstance().sendWTRCmd(
				socketId, Pack.Data_Pack(CUSTOM_CODE_SET, cmd));
		
		MainActivity.showString("发送读出设定命令",MultiTextBuffer.TYPE_OTHER);
	}
	
	
	 /**
	 *  Function: 向所有射频中转器发送命令
	 *  @author lzn
	 *  2015-3-24 下午2:51:33
	 *  @param cmd
	 */
	public static void sendCommand(byte[] cmd){
		cmd[Data.P_LOCAL_ADDR] = ADDR_WTR;
		TcpConnectionManager.getInstance().sendWTRCmdAll(
				Pack.Data_Pack(CUSTOM_CODE_DATA, cmd));
	}
	
	
	 /** 心跳包确认反馈
	 *  Function:
	 *  @author lzn 
	 *  2015-4-22 上午9:25:54
	 *  @param socketId
	 *  @param cmd
	 */
	public static void heartBeat(int socketId){
		byte[] cmd = new byte[1];
		cmd[P_FUNC_CODE] = FUNC_CODE_BEAT_RE;
		
		TcpConnectionManager.getInstance().sendWTRCmd(
				socketId, Pack.Data_Pack(CUSTOM_CODE_SET, cmd));
		
		MainActivity.showString("心跳返回",MultiTextBuffer.TYPE_OTHER);
	}
}
