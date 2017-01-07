package com.huiyun.ixhuiyunaxtion.master.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;


/**
 * 开机自动启动
 * 
 * @author lzn
 * 
 */
public class Autorun extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intentReceive) {
		if (intentReceive.getAction().equals(
				"android.intent.action.BOOT_COMPLETED")) {
			Intent intent = new Intent();
			intent.setClass(context, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		}
	}
}
