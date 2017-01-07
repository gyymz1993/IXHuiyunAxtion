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
 * @Description: 设置主机的pan key码
 * @date 2015-4-25 下午1:55:25 
 * @version V1.0   
 */
public class SetPanKey_75 extends BaseAnalyst {
	private static final int CODE_SET_KEY = 75;
	private static final int RE_CODE_SET_KEY = 76;
	
	
	
	@Override
	public void handleData(int socketId, BaseJsonObj jsonObj) {
		try {
			if (OBJ_MASTER.equals(jsonObj.getObj())
					&& CODE_SET_KEY == jsonObj.getCode()) {
				setPanKey(jsonObj, socketId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			MainActivity.showString("SetPanKey_75请检查传入参数",
					MultiTextBuffer.TYPE_OTHER);
		}
	}
	
	private void setPanKey(BaseJsonObj jsonObj, int socketId){
		if (jsonObj instanceof DataJsonObj) {
			DataJsonObj result_jsonObj = (DataJsonObj) jsonObj;
			Map<String, String> resultMap = result_jsonObj.getData();

			DataJsonObj send_jsonObj = new DataJsonObj();
			send_jsonObj.setCode(RE_CODE_SET_KEY);
			send_jsonObj.setObj(OBJ_PHONE);
			try {
				if (TokenManager.judgetToken(resultMap.get("token").toString()) == null) {
					send_jsonObj.setResult(RE_TOKEN_ERROR);
				} else {
					int key = Integer.parseInt(resultMap.get("pankey").toString());
					MainActivity.showString("主机设置pan key，值为" + key,
							MultiTextBuffer.TYPE_OTHER);
					CmdSender.resetPanKey(key);
					send_jsonObj.setResult(RE_RESULT_SUCCESS);
				}
				TcpConnectionManager.getInstance().sendJson(socketId,
						send_jsonObj);
			} catch (Exception e) {
				e.printStackTrace();
				MainActivity.showString("SetPanKey_75请检查传入参数",
						MultiTextBuffer.TYPE_OTHER);
				send_jsonObj.setResult(RE_ERROR);
				TcpConnectionManager.getInstance().sendJson(socketId,
						send_jsonObj);
			}
		}
	}
}
