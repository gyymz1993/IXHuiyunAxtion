package com.huiyun.ixhuiyunaxtion.master.activity;


import android.os.Handler;
import android.os.Message;

import com.huiyun.ixhuiyunaxtion.master.StaticValues;
import com.huiyun.ixhuiyunaxtion.master.bean.json.DataJsonObj;
import com.huiyun.ixhuiyunaxtion.master.json.JsonUtil;
import com.huiyun.ixhuiyunaxtion.master.net.NetworkSupport;
import com.huiyun.ixhuiyunaxtion.master.net.tcp.TCPClientConnect;
import com.huiyun.ixhuiyunaxtion.master.net.tcp.TcpConnectionManager;

/**   
 * 心跳包Handler
* @date 2015年4月21日 下午5:18:01 
* @version V1.0   
*/
public class HeartBeatingHandler extends Handler {

	/**
	 * 连接未断开则为true,每次心跳包返回会置为true
	 */
	public boolean bearting = true;
	/**
	 * 5秒内没有返回的次数
	 * 
	 */
	int noReturnTimes = 0;

	/**
	 * 发送心跳包给服务器
	 */
	public void sendBeatingPack() {
		DataJsonObj jsonObj = JsonUtil.getAJsonObjForMaster();
		jsonObj.code = 221;
		jsonObj.data.put("token", StaticValues.SERVER_TOKEN);
		// 发给服务器
		TcpConnectionManager.getInstance().sendJson(
				NetworkSupport.getSocketId(TCPClientConnect.getInstance()
						.getSocket()), jsonObj);
	}

	/**
	 * 重建服务器连接
	 */
	public void reConnToServer() {
		MainActivity.showString("重建服务器连接", MultiTextBuffer.TYPE_OTHER);
		TCPClientConnect.getInstance().restartClientReceive();
		bearting = true;
	}

	@Override
	public void handleMessage(Message msg) {

		switch (msg.what) {
		case 1:// 每隔一分钟发送一个心跳包
			Message msg1 = Message.obtain();
			msg1.what = 1;
			this.sendMessageDelayed(msg1, 60000);
			Message msg2 = Message.obtain();
			msg2.what = 2;
			this.sendMessageDelayed(msg2, 5000);
			bearting = false;
			sendBeatingPack();

			break;
		case 2:// 发送心跳包后5秒
			if (!bearting) {// 没有返回
				if (noReturnTimes > 2) {
					reConnToServer();// 重建服务器连接
					noReturnTimes = 0;// 记录次数还原
				} else {
					sendBeatingPack();// 再次发送
					// 5秒后再次检测
					Message msg22 = Message.obtain();
					msg22.what = 2;
					this.sendMessageDelayed(msg22, 5000);
					noReturnTimes++;
				}
			} else {// 有返回
				noReturnTimes = 0;// 记录次数还原
			}
			break;
		default:
			break;
		}

	};
}
