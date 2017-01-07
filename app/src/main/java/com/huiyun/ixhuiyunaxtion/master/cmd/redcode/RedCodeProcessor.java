package com.huiyun.ixhuiyunaxtion.master.cmd.redcode;


import com.huiyun.ixhuiyunaxtion.axtion.serialport.SerialPortConnect;
import com.huiyun.ixhuiyunaxtion.master.cmd.CommandUtil;
import com.huiyun.ixhuiyunaxtion.master.cmd.command.CMD;
import com.huiyun.ixhuiyunaxtion.master.cmd.command.Data;
import com.huiyun.ixhuiyunaxtion.master.cmd.command.ICMD;
import com.huiyun.ixhuiyunaxtion.master.inner.OnResultListener;
import com.huiyun.ixhuiyunaxtion.master.utils.BytesStringUtils;

/**
 * @Description: 处理红外码命令的发送和接收监听 
 * @date 2015-4-22 下午1:50:48 
 * @version V1.0   
 */
public class RedCodeProcessor {
	
	private static RedCodeProcessor instance;
	
	 /**
	 *  Function:单例模式
	 *  @author lzn 
	 *  2015-4-22 下午2:19:47
	 *  @return
	 */
	public static RedCodeProcessor getInstance(){
		synchronized (RedCodeProcessor.class) {
			if(instance == null){
				instance = new RedCodeProcessor();
			}
			
			return instance;
		}
	}
	
	private OnResultListener<SimpleRedCodeObj> onLearnListener;
	private OnResultListener<SimpleRedCodeObj> onControlListener;
	
	 /**
	 *  Function:红外码学习
	 *  @author lzn 
	 *  2015-4-22 下午2:15:15
	 *  @param addr
	 *  @param number
	 */
	public void learn(int addr, int number){
		CMD icmd = new ICMD();
		byte[] cmd = icmd.CMD_RedCode_Learn(addr, CommandUtil.getLocalAddr(), 
				CommandUtil.getPanKey(), number);
		
		SerialPortConnect.getInstance().send(Data.CUSTOM_CODE_RAY, cmd);
	}
	
	/**
	 *  Function:红外码控制
	 *  @author lzn 
	 *  2015-4-22 下午2:15:15
	 *  @param addr
	 *  @param number
	 */
	public void control(int addr, int number){
		CMD icmd = new ICMD();
		byte[] cmd = icmd.CMD_RedCode_Control(addr, CommandUtil.getLocalAddr(), 
				CommandUtil.getPanKey(), number);
		
		SerialPortConnect.getInstance().send(Data.CUSTOM_CODE_RAY, cmd);
	}
	
	
	 /**
	 *  Function:学习成功返回的监听
	 *  该监听返回的参数为地址
	 *  @author lzn 
	 *  2015-4-22 下午2:07:40
	 *  @param onLearnListener
	 */
	public void setOnLearnListener(OnResultListener<SimpleRedCodeObj> onLearnListener){
		this.onLearnListener = onLearnListener;
	}
	
	/**
	 *  Function:控制成功返回的监听
	 *  该监听返回的参数为地址
	 *  @author lzn 
	 *  2015-4-22 下午2:07:40
	 *  @param onLearnListener
	 */
	public void setOnControlListener(OnResultListener<SimpleRedCodeObj> onControlListener){
		this.onControlListener = onControlListener;
	}
	
	
	 /**
	 *  Function:处理从串口传来的命令
	 *  @author lzn 
	 *  2015-4-22 下午2:30:22
	 *  @param cmd
	 */
	public void handleCommand(byte[] cmd){
		if(cmd.length < 5){
			return;
		}
		
		int master = BytesStringUtils.toUnsignedInt(cmd[Data.P_TARGET_ADDR]);
		int addr = BytesStringUtils.toUnsignedInt(cmd[Data.P_LOCAL_ADDR]);
		int funcCode = BytesStringUtils.toUnsignedInt(cmd[Data.P_FUNC_CODE]);
		int key = BytesStringUtils.toUnsignedInt(cmd[Data.P_PAN_KEY]);
		int number = BytesStringUtils.toUnsignedInt(cmd[Data.P_OBJECT_NUM]);
		
		// 检查主机地址
		if(master != CommandUtil.getLocalAddr()){
			return;
		}
		
		// 检查key
		if(key != CommandUtil.getPanKey()){
			return;
		}
		
		switch(funcCode){
		case Data.FUNC_CODE_REDCODE_LEARN_RE:
			// 红外码学习
			if(onLearnListener != null){
				onLearnListener.onResult(true, new SimpleRedCodeObj(addr,number));
			}
			break;
		case Data.FUNC_CODE_REDCODE_CONTROL_RE:
			// 红外码发送
			if(onControlListener != null){
				onControlListener.onResult(true, new SimpleRedCodeObj(addr,number));
			}
			break;
		}
	}
	
	public class SimpleRedCodeObj{
		public int addr;
		public int number;
		public SimpleRedCodeObj(int addr, int number){
			this.addr = addr;
			this.number = number;
		}
	}
}
