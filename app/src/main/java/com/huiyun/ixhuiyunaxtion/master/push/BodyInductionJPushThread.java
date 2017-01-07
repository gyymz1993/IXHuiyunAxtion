package com.huiyun.ixhuiyunaxtion.master.push;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.huiyun.ixhuiyunaxtion.master.utils.SpUtils;

/**   
 * @Description: 当人体感应有反应时，推送信息的线程
 * @date 2015-2-10 下午5:41:02 
 * @version V1.0   
 */
public class BodyInductionJPushThread extends Thread {
	private static BodyInductionJPushThread instance;
	public static BodyInductionJPushThread getInstance(){
		synchronized(BodyInductionJPushThread.class){
			if(instance == null){
				instance = new BodyInductionJPushThread();
			}
			
			return instance;
		}
	}
	
	private Handler handler;
	private final static int TIME = 60;
	private boolean valid = true;
	
	public Handler getHandler(){
		return handler;
	}
	
	@Override
	public void run() {
		System.out.println("创建BodyInductionJPush线程，线程名为" + currentThread().getName());
		
		Looper.prepare();
		
		handler = new BodyInductionJPushHandler();
		
		Looper.loop();
	}
	
	static class BodyInductionJPushHandler extends Handler {
		
		@Override
		public void handleMessage(Message msg) {
			if(msg.arg1 == 1){
				if(BodyInductionJPushThread.getInstance().valid){
					JPushForPhone.send(SpUtils.getValues("familyId"));
					BodyInductionJPushThread.getInstance().valid = false;
					Message mEnd = new Message();
					mEnd.arg1 = 2;
					BodyInductionJPushThread.getInstance().getHandler()
						.sendMessageDelayed(mEnd, TIME * 1000);
				}
			} else if(msg.arg1 == 2){
				BodyInductionJPushThread.getInstance().valid = true;
			}
		}
	}

	@Override
	public synchronized void start() {
		if (!this.isAlive())
			super.start();
	}
}
