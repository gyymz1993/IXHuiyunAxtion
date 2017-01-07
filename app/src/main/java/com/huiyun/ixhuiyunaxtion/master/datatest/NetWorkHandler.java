package com.huiyun.ixhuiyunaxtion.master.datatest;


import android.os.Handler;
import android.os.Message;

import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;
import com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer;

/**   
* @Description: 网络请求处理
* @date 2015年5月26日 下午5:16:32 
* @version V1.0   
*/
public class NetWorkHandler extends Handler {
	//允许失败访问次数
	private static int COUNT = 3;
	//30秒訪問一次百度請求
	private static int TIME = 30 * 1000;
	//當前網絡狀態
	public static boolean NET_STATUS = true;
	//如果三次訪問失敗則重啟主機標識
    public static boolean IS_NOT_NET=false;
    private int k=0;
    
	public void sendMessage() {
		Message message = Message.obtain();
		message.what = 1;
		this.sendMessageDelayed(message, TIME);
	}

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		if (msg.what == 1) {
			Thread visitBaiduThread = new Thread(new VisitWebRunnable());
			visitBaiduThread.start();
			Message message1 = Message.obtain();
			message1.what = 2;
			this.sendMessageDelayed(message1, 0);
		}
		if (msg.what == 2) {
			
			if (!NET_STATUS) {
				COUNT--;
				k++;
				if(!IS_NOT_NET&&k<4){
					MainActivity.showString("第"+k+"次访问失败，请检查主机", MultiTextBuffer.TYPE_OTHER);
				}
				if (COUNT == 0) {
					IS_NOT_NET=true;
					MainActivity.showString("即將请重启主机", MultiTextBuffer.TYPE_OTHER);
				}
			} else {
				COUNT = 3;
			}
			sendMessage();
		}
	}

	class VisitWebRunnable implements Runnable {
		@Override
		public void run() {
			NetWorkHttpRequest.getHttpRequest().isNetWorkPing();
		}
	}
}
