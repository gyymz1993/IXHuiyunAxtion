package com.huiyun.ixhuiyunaxtion.master.broadcast;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.huiyun.ixhuiyunaxtion.master.net.NetworkSupport;

/**   
 * @Description: 该广播用于监听网络状态的改变 
 * @date 2015-3-16 下午2:54:34 
 * @version V1.0   
 */
public class NetworkStateChange extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		NetworkSupport.checkWifiState();
	}
}
