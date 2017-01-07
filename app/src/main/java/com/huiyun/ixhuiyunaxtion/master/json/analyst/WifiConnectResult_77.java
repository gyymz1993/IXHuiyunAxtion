package com.huiyun.ixhuiyunaxtion.master.json.analyst;

import com.huiyun.ixhuiyunaxtion.master.bean.json.BaseJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.json.DataJsonObj;
import com.huiyun.ixhuiyunaxtion.master.net.TokenManager;
import com.huiyun.ixhuiyunaxtion.master.net.tcp.TcpConnectionManager;
import com.huiyun.ixhuiyunaxtion.master.net.wifi.WifiUtil;
import com.huiyun.ixhuiyunaxtion.master.utils.UIUtils;

import java.util.Map;

/**
 * @Title: NotificationStudy_7.java
 * @Package com.huiyun.master.activity.analyst
 * @Description: 通知主机查找设备
 * @author Yangshao
 * @date 2015年1月8日 上午10:16:42
 * @version V1.0
 */
public class WifiConnectResult_77 extends BaseAnalyst {
	private static final int CODE_NOTIFIC = 77;
	private static final int RE_CODE_NOTIFIC = 78;
	private WifiUtil mWifiAdmin;

	@Override
	public void handleData(int socketId, BaseJsonObj jsonObj) {
		try {
			if (OBJ_MASTER.equals(jsonObj.getObj())
					&& CODE_NOTIFIC == jsonObj.getCode()) {
				startSeachDevices(jsonObj, socketId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Function: 开启查找设备 返回结果
	 * 
	 * @author Yangshao 2015年1月8日 上午10:20:32
	 * @param jsonObj
	 * @param socketId
	 */
	private void startSeachDevices(BaseJsonObj jsonObj, int socketId) {
		if (jsonObj instanceof DataJsonObj) {
			DataJsonObj result_jsonObj = (DataJsonObj) jsonObj;
			final Map<String, String> resultMap = result_jsonObj.getData();

			DataJsonObj send_jsonObj = new DataJsonObj();
			send_jsonObj.setCode(RE_CODE_NOTIFIC);
			send_jsonObj.setObj(OBJ_PHONE);
			try {
				if (TokenManager.judgetToken(resultMap.get("token").toString()) == null) {
					send_jsonObj.setResult(RE_TOKEN_ERROR);
				} else {
					mWifiAdmin = new WifiUtil(UIUtils.getContext());
					mWifiAdmin.openNetCard();
					new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								// "axtion-public"
								String ssid = resultMap.get("SSID");
								// ix5201314
								String ssidPwd = resultMap.get("SSIDPWD");
								Thread.sleep(10000);
								System.out.println(mWifiAdmin
										.checkNetCardState());
								mWifiAdmin.addNetwork(mWifiAdmin
										.createWifiInfo(ssid, ssidPwd, 3));
							} catch (InterruptedException e) {
								e.printStackTrace();
							}

						}
					}).start();
					send_jsonObj.setResult(RE_RESULT_SUCCESS);
				}
				TcpConnectionManager.getInstance().sendJson(socketId,
						send_jsonObj);
			} catch (Exception e) {
				e.printStackTrace();
				send_jsonObj.setResult(RE_ERROR);
				TcpConnectionManager.getInstance().sendJson(socketId,
						send_jsonObj);
			}
		}
	}

}
