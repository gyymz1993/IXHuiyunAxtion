package com.huiyun.ixhuiyunaxtion.master.net.thread;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**   
 * @Description: 向服务器发送命令的线程 
 * @date 2015-2-9 下午4:24:53 
 * @version V1.0   
 */
public class ServerSendThread extends Thread {
	private static ServerSendThread instance;
	public static ServerSendThread getInstance(){
		synchronized(ServerSendThread.class){
			if(instance == null){
				instance = new ServerSendThread();
			}
			
			return instance;
		}
	}
	private ServerSendThread(){
		
	}
	
	private Handler handler;
	
	public Handler getHandler(){
		return handler;
	}
	
	@Override
	public void run() {
		System.out.println("创建ServerSend线程，线程名为" + currentThread().getName());
		
		Looper.prepare();
		
		handler = new ServerSendHandler();
		
		Looper.loop();
	}
	
	static class ServerSendHandler extends Handler {
		
		@Override
		public void handleMessage(Message msg) {
			
		}
	}

	@Override
	public synchronized void start() {
		if (!this.isAlive())
			super.start();
	}
}
