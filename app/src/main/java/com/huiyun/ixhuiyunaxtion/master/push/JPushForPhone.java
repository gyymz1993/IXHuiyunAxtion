  package com.huiyun.ixhuiyunaxtion.master.push;

import cn.jpush.api.ErrorCodeEnum;
import cn.jpush.api.JPushClient;
import cn.jpush.api.MessageResult;

/**
 * 主机信息主动发给手机
 * 
 * @author busy
 * 
 */

public class JPushForPhone {
	private static final String appKey = "140e4e91ba48b01ddd1f8956"; // 必填，例如466f7032ac604e02fb7bda89

	private static final String masterSecret = "feb70a806867eb130615d355";// "13ac09b17715bd117163d8a1";//必填，每个应用都对应一个masterSecret

	private static JPushClient jpush = null;


	/**
	 * 保存离线的时长。秒为单位。最多支持10天（864000秒）。 0 表示该消息不保存离线。即：用户在线马上发出，当前不在线用户将不会收到此消息。
	 * 此参数不设置则表示默认，默认为保存1天的离线消息（86400秒)。
	 */
	private static long timeToLive = 60 * 60 * 24;

	static {
		jpush = new JPushClient(masterSecret, appKey, timeToLive);
	}
	
	public static void send(String familyid){
		int sendNo = getRandomSendNo();
		String msgTitle = "通知";
		String msgContent="有人进入";
		
		/*
		 * IOS设备扩展参数,
		 * 设置badge，设置声音
		 */

//		Map<String, Object> extra = new HashMap<String, Object>();
//		IOSExtra iosExtra = new IOSExtra(10, "WindowsLogonSound.wav");
//		extra.put("android", "魅力");
		
		MessageResult msgResult = jpush.sendNotificationWithTag(sendNo, familyid, msgTitle, msgContent);

		if (null != msgResult) {
			System.out.println("服务器返回数据: " + msgResult.toString());
			if (msgResult.getErrcode() == ErrorCodeEnum.NOERROR.value()) {
				System.out.println(String.format("发送成功， sendNo= %s,messageId= %s",msgResult.getSendno(),msgResult.getMsg_id()));
			} else {
				System.out.println("发送失败， 错误代码=" + msgResult.getErrcode() + ", 错误消息=" + msgResult.getErrmsg());
			}
		} else {
			System.out.println("无法获取数据");
		}
		
	}


	public static final int MAX = Integer.MAX_VALUE;
	public static final int MIN = (int) MAX / 2;

	/**
	 * 保持 sendNo 的唯一性是有必要的 It is very important to keep sendNo unique.
	 * 
	 * @return sendNo
	 */
	public static int getRandomSendNo() {
		return (int) (MIN + Math.random() * (MAX - MIN));
	}

}
