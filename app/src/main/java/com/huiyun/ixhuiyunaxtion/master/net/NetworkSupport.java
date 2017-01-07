package com.huiyun.ixhuiyunaxtion.master.net;

import java.net.Socket;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;
import com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer;
import com.huiyun.ixhuiyunaxtion.master.cmd.CmdSender;
import com.huiyun.ixhuiyunaxtion.master.utils.UIUtils;


/**
 * 提供网络模块的变量和方法支持
 * @author lzn
 *
 */
public class NetworkSupport {
	public static int UDP_PORT = 48899;
	/**
	 * 开关、窗帘、风机的控制的通信端口
	 */
	public static int TCP_PORT = 48900;
	/**
	 * 特别用于用户登录注册、红外学习转发等功能的通信端口
	 */
	public static int TCP_PORT_2 = 48901;
	/**
	 * 与服务器连接的通信端口
	 */
	public static int TCP_CLIENT_PORT = 8989;
	
	/**
	 * UDP接收时需要处理的命令
	 */
	public static final String UDP_REQUEST_IP_FROM_PHONE = "HF-A11ASSISTHREAD";
	public static String UDP_REQUEST_IP_FROM_WTR; //= "who is master,i am Wifi_to_RF Repeaters";
	/**
	 * UDP收到上述命令时需要返回的命令
	 */
	public static String UDP_REQUEST_IP_FROM_PHONE_RETURN ; //"(ipAddr),MASTER,AXTION";
	public static final String UDP_REQUEST_IP_FROM_WTR_RETURN = "+Master IP (ipAddr)";
	// 返回的格式为 xxx.xxx.xxx.xxx,AAA,AAA，比如192.168.1.33,MASTER,AXTION


	/**
	 *  Function: 获取wifi信息
	 *  @author lzn 
	 *  2015-3-14 下午2:24:48
	 *  @return
	 */
	public static WifiInfo getWifiInfo(){
		WifiManager wifiManager = (WifiManager) UIUtils.getContext().getSystemService(Context.WIFI_SERVICE);
		return wifiManager.getConnectionInfo(); 
	}
	
	/**
	 * 根据socket获取socket的标识id
	 * @param socket
	 * @return
	 */
	public static int getSocketId(Socket socket){
		try {
			byte[] ipAddr = socket.getInetAddress().getAddress();
			int id = (ipAddr[0] & 0xff) | ((ipAddr[1] << 8) & 0xff00)
					| ((ipAddr[2] << 16) & 0xff0000)
					| ((ipAddr[3] << 24) & 0xff000000);
			return id;
		} catch (NullPointerException e) {
			return 0;
		}
	}
	
	
	 /**
	 *  Function: 获取当前网络状态，并根据情况向MCU反映
	 *  @author lzn 
	 *  2015-3-17 下午2:41:35
	 */
	public static void checkWifiState(){
		WifiInfo info = NetworkSupport.getWifiInfo();
		if(info.getSSID() != null && info.getSSID().trim().startsWith("\"") &&
				info.getSSID().trim().endsWith("\"")){
			// 网络已连接
			MainActivity.showString("当前网络SSID为：" + info.getSSID(),
					MultiTextBuffer.TYPE_OTHER);
			CmdSender.wifiStateChange(true);
		} else {
			// 网络未连接
			MainActivity.showString("当前网络SSID为空",
					MultiTextBuffer.TYPE_OTHER);
			CmdSender.wifiStateChange(false);
		}
	}
}
