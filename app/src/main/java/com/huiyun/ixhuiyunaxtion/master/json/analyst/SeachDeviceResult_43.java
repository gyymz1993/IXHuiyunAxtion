package com.huiyun.ixhuiyunaxtion.master.json.analyst;

import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;
import com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer;
import com.huiyun.ixhuiyunaxtion.master.bean.json.BaseJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.json.DataJsonObj;
import com.huiyun.ixhuiyunaxtion.master.cmd.CmdSender;
import com.huiyun.ixhuiyunaxtion.master.net.TokenManager;
import com.huiyun.ixhuiyunaxtion.master.net.tcp.TcpConnectionManager;

import java.util.Map;


/**
 * @Title: SeachDeviceResult_43.java
 * @Description: 主动查找设备
 * @author Yangshao
 * @date 2015年1月8日 上午10:16:42
 * @version V1.0
 */
public class SeachDeviceResult_43 extends BaseAnalyst {
	private static final int CODE_NOTIFIC = 43;
	private static final int RE_CODE_NOTIFIC = 44;

	@Override
	public void handleData(int socketId, BaseJsonObj jsonObj) {
		try {
			if (OBJ_MASTER.equals(jsonObj.getObj())
					&& CODE_NOTIFIC == jsonObj.getCode()) {
				startSeachDevices(jsonObj, socketId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			MainActivity.showString("SeachDeviceResult_43请检查传入参数",
					MultiTextBuffer.TYPE_OTHER);
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
			Map<String, String> resultMap = result_jsonObj.getData();

			DataJsonObj send_jsonObj = new DataJsonObj();
			send_jsonObj.setCode(RE_CODE_NOTIFIC);
			send_jsonObj.setObj(OBJ_PHONE);
			try {
				if (TokenManager.judgetToken(resultMap.get("token").toString()) == null) {
					send_jsonObj.setResult(RE_TOKEN_ERROR);
				} else {
					MainActivity.showString("主机开始查找设备",
							MultiTextBuffer.TYPE_OTHER);
					CmdSender.requestDeviceConfirm();
					send_jsonObj.setResult(RE_RESULT_SUCCESS);
				}
				TcpConnectionManager.getInstance().sendJson(socketId,
						send_jsonObj);
			} catch (Exception e) {
				e.printStackTrace();
				MainActivity.showString("SeachDeviceResult_43请检查传入参数",
						MultiTextBuffer.TYPE_OTHER);
				send_jsonObj.setResult(RE_ERROR);
				TcpConnectionManager.getInstance().sendJson(socketId,
						send_jsonObj);
			}
		}
	}

}
