package com.huiyun.ixhuiyunaxtion.master.net.thread;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**   
 * @Description: 向移动端发送命令的线程 
 * @date 2015-2-9 下午4:02:13 
 * @version V1.0   
 */
public class PhoneSendThread extends Thread {
	private static PhoneSendThread instance;

	public static PhoneSendThread getInstance() {
		synchronized (PhoneSendThread.class) {
			if (instance == null) {
				instance = new PhoneSendThread();
			}

			return instance;
		}
	}
	private PhoneSendThread(){
		
	}
	
	private Handler handler;

	public Handler getHandler() {
		return handler;
	}

	@Override
	public void run() {
		System.out.println("创建PhoneSend线程，线程名为" + currentThread().getName());

		Looper.prepare();

		handler = new PhoneSendHandler();

		Looper.loop();
	}

	static class PhoneSendHandler extends Handler {

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
