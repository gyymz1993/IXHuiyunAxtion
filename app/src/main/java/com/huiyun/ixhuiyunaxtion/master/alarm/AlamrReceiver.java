package com.huiyun.ixhuiyunaxtion.master.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @Description: 接收响铃广播 并调用Server操作
 * @date 2015年3月18日 下午4:43:59
 * @version V1.0
 */
public class AlamrReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		intent.setClass(context, AlarmService.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startService(intent);
	}
}
