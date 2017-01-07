package com.huiyun.ixhuiyunaxtion.axtion.serialport;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**   
 * @Description: 串口命令发送线程 
 * handler线程
 * @date 2015-2-9 下午5:16:40 
 * @version V1.0   
 */
public class SerialPortSendThread extends Thread {
	private static SerialPortSendThread instance;
	public static SerialPortSendThread getInstance(){
		synchronized(SerialPortSendThread.class){
			if(instance == null){
				instance = new SerialPortSendThread();
			}
			
			return instance;
		}
	}
	private SerialPortSendThread(){
		
	}
	
	private Handler handler;
	
	
	 /**
	 *  Function:获得该线程的handler
	 *  @return
	 */
	public Handler getHandler(){
		return handler;
	}
	
	@Override
	public void run() {
		System.out.println("创建SerialPortSend线程，线程名为" + currentThread().getName());
		
		Looper.prepare();
		
		handler = new SerialPortSendHandler();
		
		Looper.loop();
	}
	
	static class SerialPortSendHandler extends Handler {
		
		@Override
		public void handleMessage(Message msg) {
			
		}
	}
}
