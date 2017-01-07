package com.huiyun.ixhuiyunaxtion.master.cmd;


import com.huiyun.ixhuiyunaxtion.axtion.serialport.SerialPortConnect;
import com.huiyun.ixhuiyunaxtion.master.bean.table.Device;
import com.huiyun.ixhuiyunaxtion.master.cmd.command.CMD;
import com.huiyun.ixhuiyunaxtion.master.cmd.command.Data;
import com.huiyun.ixhuiyunaxtion.master.cmd.command.ICMD;
import com.huiyun.ixhuiyunaxtion.master.cmd.process.ColorState;
import com.huiyun.ixhuiyunaxtion.master.cmd.process.SerialCmdProcess;

/** CMD命令发送
 * @author lzn
 *
 */
public class CmdSender {
	
	/**
	 * 搜索所有设备(不包括主机自己)
	 */
	public static void searchDevice(){
		byte[] cmd;
		CMD icmd = new ICMD();
		
		SerialCmdProcess.getInstance().getAgingSwitch1().open();
		
		cmd = icmd.CMD_Search_Device(CommandUtil.getLocalAddr(), 1);
		SerialPortConnect.getInstance().send(Data.CUSTOM_CODE_RF, cmd);
		
		cmd = icmd.CMD_Search_Device(CommandUtil.getLocalAddr(), 0);
		SerialPortConnect.getInstance().send(Data.CUSTOM_CODE_RF, cmd);
	}
	
	/**
	 * 获取主机自己的芯片ID
	 */
	public static void readMyChipId(){
		byte[] cmd;
		CMD icmd = new ICMD();
		
		cmd = icmd.CMD_Search_Device(Data.ADDR_SELF, 1);
		SerialPortConnect.getInstance().send(Data.CUSTOM_CODE_RF, cmd);
	}
	
	
	 /**
	 *  Function: 请求写入开关状态
	 *  @author lzn 
	 *  2015-1-15 上午9:55:37
	 *  @param phoneCode  手机按键码
	 *  @param state  开关值
	 */
	public static void writeState(int phoneCode, int state){
		CMD icmd = new ICMD();
		switch(StateStorage.getInstance().getTypeByPhoneCode(phoneCode)){
		case Device.TYPE_COLOR_LAMP:
			// 调色灯
			SerialCmdProcess.getInstance().handleWifiCommand(icmd.CMD_CRTL_IO(
					CommandUtil.getLocalAddr(), Data.ADDR_WIFI, 
					CommandUtil.getPanKey(), phoneCode, 3, new byte[]{
					(byte) ColorState.getBlue(state), (byte) ColorState.getRed(state),
					(byte) ColorState.getGreen(state)}));
			break;
		default:
			// 其它
			SerialCmdProcess.getInstance().handleWifiCommand(icmd.CMD_CRTL_IO(
					CommandUtil.getLocalAddr(), Data.ADDR_WIFI, 
					CommandUtil.getPanKey(), phoneCode, 1, new byte[]{(byte) state}));
			break;
		}
	}
	
	
	 /**
	 *  Function: 请求读取开关状态
	 *  @author lzn 
	 *  2015-1-15 上午9:58:15
	 *  @param addr
	 *  @param number
	 */
	public static void readState(int addr, int number, int type){
		CMD icmd = new ICMD();
		switch(type){
		case Device.TYPE_GATEWAY:
			// 网关
			SerialCmdProcess.getInstance().handleWifiCommand(icmd.CMD_Read_IO(
					addr, Data.ADDR_WIFI, CommandUtil.getPanKey(), 
					0, 1, new byte[]{(byte) number}));
			break;
		case Device.TYPE_OUT_WITH_IN:
		case Device.TYPE_OUT:
			// 普通输出(如普通灯、窗帘)
			SerialCmdProcess.getInstance().handleWifiCommand(icmd.CMD_Read_IO(
					addr, Data.ADDR_WIFI, CommandUtil.getPanKey(), 
					number, 0, null));
			break;
		case Device.TYPE_COLOR_LAMP:
			// 调色灯
			SerialCmdProcess.getInstance().handleWifiCommand(icmd.CMD_Read_IO(
					addr, Data.ADDR_WIFI, CommandUtil.getPanKey(), 
					number, 0, null));
			break;
		default:
			// 其它输出
			SerialCmdProcess.getInstance().handleWifiCommand(icmd.CMD_Read_IO(
					addr, Data.ADDR_WIFI, CommandUtil.getPanKey(), 
					number, 0, null));
			break;
		}
	}
	
	
	 /**
	 *  Function: 让面板等设备闪烁
	 *  (这里的device不是指device对象，而是指实际意义上的面板、网关等设备)
	 *  @author lzn 
	 *  2015-1-21 下午2:34:14
	 */
	public static void blinkBox(byte[] chipId){
		byte[] cmd;
		CMD icmd = new ICMD();
		
		cmd = icmd.CMD_Device_Blink(CommandUtil.getLocalAddr(), chipId);
		SerialPortConnect.getInstance().send(Data.CUSTOM_CODE_RF, cmd);
	}
	
	
	 /**
	 *  Function: 无线网络连接状态改变时，发送该命令让MCU知道当前状态
	 *  @author lzn 
	 *  2015-3-17 上午9:51:50
	 *  @param connected 是否连接
	 */
	public static void wifiStateChange(boolean connected){
		byte[] cmd;
		CMD icmd = new ICMD();
		
		cmd = icmd.CMD_WIFI_State(CommandUtil.getLocalAddr(), connected);
		SerialPortConnect.getInstance().send(Data.CUSTOM_CODE_RF, cmd);
	}
	
	
	 /**
	 *  Function: 请求设备手动确认（用来主动添加设备）
	 *  @author lzn 
	 *  2015-4-2 上午9:17:56
	 */
	public static void requestDeviceConfirm(){
		byte[] cmd;
		CMD icmd = new ICMD();
		
		SerialCmdProcess.getInstance().getAgingSwitch2().open();
		
		cmd = icmd.CMD_Set_Hand_Mode(CommandUtil.getLocalAddr());
		SerialPortConnect.getInstance().send(Data.CUSTOM_CODE_RF, cmd);
	}
	
	
	 /**
	 *  Function:重新设置pan key
	 *  @author lzn 
	 *  2015-4-22 下午3:35:43
	 *  @param key
	 */
	public static void resetPanKey(int key){
		// 首先修改数据库中的key值
		CommandUtil.setPanKey(key);
		// 然后给MCU发送修改本机pan key的命令
		byte[] cmd;
		CMD icmd = new ICMD();
		
		cmd = icmd.CMD_Set_Target_Addr(
				CommandUtil.getLocalAddr(), CommandUtil.getLocalAddr(),
				CommandUtil.myCPUID, CommandUtil.getPanKey());
		SerialPortConnect.getInstance().send(Data.CUSTOM_CODE_RF, cmd);
		
		// 最后给所有从机发送修改pan key的命令
		// TODO
		
	}
}
