package com.huiyun.ixhuiyunaxtion.master.net.WTR;


import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;
import com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer;
import com.huiyun.ixhuiyunaxtion.master.cmd.CommandUtil;
import com.huiyun.ixhuiyunaxtion.master.cmd.command.Data;
import com.huiyun.ixhuiyunaxtion.master.cmd.process.SerialCmdProcess;
import com.huiyun.ixhuiyunaxtion.master.utils.BytesStringUtils;

/**
 * @Description: 处理从WTR收到的命令 
 * @date 2015-3-24 下午2:55:55 
 * @version V1.0   
 */
public class WTRCommandHandler implements WTR{
	
	 /**
	 *  Function: 处理接收到的命令
	 *  @author lzn 
	 *  2015-3-24 下午4:01:49
	 *  @param socketId
	 *  @param customCode 自定义码
	 *  @param cmd
	 */
	public static void handleWTRCommand(int socketId, int customCode, byte[] cmd){
		switch(customCode){
		case CUSTOM_CODE_DATA:
			handleDataCommand(socketId,cmd);
			break;
		case CUSTOM_CODE_SET:
			handleSettingCommand(socketId,cmd);
			break;
		}
	}
	
	private static void handleDataCommand(int socketId, byte[] cmd){
		// 处理射频中转器转发的命令
		cmd[Data.P_TARGET_ADDR] = (byte) CommandUtil.getLocalAddr();
		SerialCmdProcess.getInstance().handleRFCommand(cmd);
	}
	
	private static void handleSettingCommand(int socketId, byte[] cmd){
		// 处理射频中转器相关的设置命令
		switch(cmd[P_FUNC_CODE]){
		case FUNC_CODE_READ_RE:
			if(cmd.length < 3){
				return;
			}
			
			// 检查地址和key是否正确，如果不正确发送写入设定命令进行修正
			int addr = BytesStringUtils.toUnsignedInt(cmd[1]),
			    key = BytesStringUtils.toUnsignedInt(cmd[2]);
			
			MainActivity.showString(
					"接收到读取设定的反馈命令，地址为" + addr + "，key为" + key,
					MultiTextBuffer.TYPE_OTHER);
			
			if(addr != ADDR_WTR || key != CommandUtil.getPanKey()){
				WTRCommandSender.writeSetting(
					socketId, ADDR_WTR, CommandUtil.getPanKey());
			}
			break;
		case FUNC_CODE_BEAT:
			// 心跳包
			WTRCommandSender.heartBeat(socketId);
			break;
		}
	}
}
