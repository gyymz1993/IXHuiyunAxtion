package com.huiyun.ixhuiyunaxtion.master.json.analyst;

import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;
import com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer;
import com.huiyun.ixhuiyunaxtion.master.bean.json.BaseJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.json.DataJsonObj;
import com.huiyun.ixhuiyunaxtion.master.net.TokenManager;
import com.huiyun.ixhuiyunaxtion.master.net.tcp.TcpConnectionManager;

import java.util.Map;


/**   
 * @Description: 开启远程控制
 * @date 2015年1月15日 下午1:26:37 
 * @version V1.0   
 */
public class StartLongRangeResult_33 extends BaseAnalyst{

	private static final int START_REMOTE=33;
	private static final int RE_START_REMOTE=34;
	@Override
	public void handleData(int socketId, BaseJsonObj jsonObj) {
		try {
			if (OBJ_MASTER.equals(jsonObj.getObj())
					&& START_REMOTE == jsonObj.getCode()) {
				startRemote(jsonObj, socketId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			MainActivity.showString("请检查传入参数",MultiTextBuffer.TYPE_OTHER);
		}
	}
	
	 /**
	 *  Function: 开启远程控制
	 *  @author Yangshao 
	 *  2015年1月15日 下午3:11:36
	 *  @param jsonObj
	 *  @param socketId
	 *  
	 *  返回：data内容：{state:int}   //1：开；2：关
	 */
	private void startRemote(BaseJsonObj jsonObj, int socketId) {
		if (jsonObj instanceof DataJsonObj) {
			DataJsonObj result_jsonObj = (DataJsonObj) jsonObj;
			Map<String, String> resultMap = result_jsonObj.getData();
			DataJsonObj send_jsonObj = new DataJsonObj();
			send_jsonObj.setCode(RE_START_REMOTE);
			send_jsonObj.setObj(OBJ_PHONE);
			try {
				if (TokenManager.judgetToken(resultMap.get("token").toString()) == null) {
					send_jsonObj.setResult(RE_TOKEN_ERROR);
					TcpConnectionManager.getInstance().sendJson(socketId, send_jsonObj);
				}else{
					//TODO 开启远程
				}
			} catch (Exception e) {
				e.printStackTrace();
				MainActivity.showString("请检查传入参数", MultiTextBuffer.TYPE_OTHER);
				send_jsonObj.setResult(RE_ERROR);
				TcpConnectionManager.getInstance().sendJson(socketId, send_jsonObj);
			}
		}
	}

}
