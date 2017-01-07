package com.huiyun.ixhuiyunaxtion.master.datatest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;
import com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer;
import com.huiyun.ixhuiyunaxtion.master.net.NetworkSupport;


/**
* @Description: 檢查網絡
* @date 2015年5月26日 下午4:44:42 
* @version V1.0   
*/
public class NetWorkDetectionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo  mobNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo  wifiNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
            //改变背景或者 处理网络的全局变量
        	MainActivity.showString("ConnectionChangeReceiver网络断开",
					MultiTextBuffer.TYPE_OTHER);
        }else {
            //改变背景或者 处理网络的全局变量
        	MainActivity.showString("ConnectionChangeReceiver网络连接",
					MultiTextBuffer.TYPE_OTHER);
        }
        
        NetworkSupport.checkWifiState();
    }
  
}