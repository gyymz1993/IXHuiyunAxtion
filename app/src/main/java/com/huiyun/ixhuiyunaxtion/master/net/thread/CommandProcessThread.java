package com.huiyun.ixhuiyunaxtion.master.net.thread;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**   
 * @Description: 命令处理的线程
 * @date 2015-2-9 下午3:03:49 
 * @version V1.0   
 */
public class CommandProcessThread extends Thread {

	private static CommandProcessThread instance;
	public static CommandProcessThread getInstance() {
		synchronized (CommandProcessThread.class) {
			if (instance == null) {
				instance = new CommandProcessThread();
			}

			return instance;
		}
	}
	private CommandProcessThread(){
		
	}
	
	private Handler handler;

	public Handler getHandler() {
		return handler;
	}

	@Override
	public void run() {
		System.out.println("创建CommandProcess线程，线程名为"
				+ currentThread().getName());

		Looper.prepare();
		handler = new CommandProcessHandler();
		Looper.loop();
	}

	static class CommandProcessHandler extends Handler {
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
