package com.huiyun.ixhuiyunaxtion.master.json.analyst;

import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;
import com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer;
import com.huiyun.ixhuiyunaxtion.master.bean.json.BaseJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.json.DataJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.table.Device;
import com.huiyun.ixhuiyunaxtion.master.cmd.StateStorage;
import com.huiyun.ixhuiyunaxtion.master.net.TokenManager;
import com.huiyun.ixhuiyunaxtion.master.net.tcp.TcpConnectionManager;

import java.util.Map;



/**
 * @Description: 开关控制
 * @date 2015年1月15日 上午11:38:37
 * @version V1.0
 */
public class ReadStateResult_29 extends BaseAnalyst {

	private static final int READ_STATE = 29;
	private static final int RE_READ_STATE = 30;
	@Override
	public void handleData(int socketId, BaseJsonObj jsonObj) {
		try {
			if (OBJ_MASTER.equals(jsonObj.getObj())
					&& READ_STATE == jsonObj.getCode()) {
				controlClosedAndOpen(jsonObj, socketId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			MainActivity.showString("请检查传入参数", MultiTextBuffer.TYPE_OTHER);
		}
	}

	/**
	 * Function:
	 * 
	 * @author Yangshao 2015年1月15日 上午11:41:48
	 * @param jsonObj
	 * @param socketId
	 *            {token:xxx,phoneCode:int,state:int} 成功
	 *            data内容{phoneCode:int,state:int} //state:0关，1开
	 * 
	 *            失败：{obj:phone,code:12,result:2,data:{phoneCode:int,state:int}}
	 *            //设备可能不在线
	 */
	private void controlClosedAndOpen(BaseJsonObj jsonObj, final int socketId) {
		if (jsonObj instanceof DataJsonObj) {
			DataJsonObj result_jsonObj = (DataJsonObj) jsonObj;
			final Map<String, String> resultMap = result_jsonObj.getData();
			final DataJsonObj send_jsonObj = new DataJsonObj();
			send_jsonObj.setCode(RE_READ_STATE);
			send_jsonObj.setObj(OBJ_PHONE);
			try {
				if (TokenManager.judgetToken(resultMap.get("token").toString()) == null) {
					send_jsonObj.setResult(RE_TOKEN_ERROR);
					TcpConnectionManager.getInstance().sendJson(socketId, send_jsonObj);
				} else {
					// 读取状态 设置监听
					Device device = StateStorage.getInstance().readState(
							Integer.valueOf(resultMap.get("phoneCode")));
					send_jsonObj.setResult(RE_RESULT_SUCCESS);
					resultMap.put("phoneCode", device.getPhoneCode() + "");
					resultMap.put("state", device.getState() + "");
					send_jsonObj.setData(resultMap);
					TcpConnectionManager.getInstance().sendJson(socketId, send_jsonObj);
				}
			} catch (Exception e) {
				e.printStackTrace();
				MainActivity.showString("请检查传入参数",MultiTextBuffer.TYPE_OTHER);
			}
		}
	}

}
