package com.huiyun.ixhuiyunaxtion.master.json.analyst;

import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;
import com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer;
import com.huiyun.ixhuiyunaxtion.master.bean.json.BaseJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.json.DataJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.json.DownJsonObj;
import com.huiyun.ixhuiyunaxtion.master.dao.RedRayDao;
import com.huiyun.ixhuiyunaxtion.master.dao.impl.RedRayDaoImpl;
import com.huiyun.ixhuiyunaxtion.master.net.TokenManager;
import com.huiyun.ixhuiyunaxtion.master.net.tcp.TcpConnectionManager;
import com.huiyun.ixhuiyunaxtion.master.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;



/**
 * @Description: 删除遥控器
 * @date 2015年2月3日 下午5:23:25
 * @version V1.0
 */
public class DeleteRemoteResult_19 extends BaseAnalyst {

	private final int DELETE_REMOTE = 19;
	private final int RE_DELETE_REMOTE = 20;
	RedRayDao infraredDao = new RedRayDaoImpl(UIUtils.getContext());

	@Override
	public void handleData(int socketId, BaseJsonObj jsonObj) {
		try {
			if (OBJ_MASTER.equals(jsonObj.getObj())
					&& DELETE_REMOTE == jsonObj.getCode()) {
				deleteRemote(jsonObj, socketId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			MainActivity.showString("请检查传入参数", MultiTextBuffer.TYPE_OTHER);
		}
	}

	/**
	 * Function:
	 * 
	 * @author Yangshao 2015年2月3日 下午2:12:04
	 * @param jsonObj
	 * @param socketId
	 */
	private void deleteRemote(BaseJsonObj jsonObj, int socketId) {
		if (jsonObj instanceof DownJsonObj) {
			DownJsonObj result_jsonObj = (DownJsonObj) jsonObj;
			DownJsonObj.Data resultMap = result_jsonObj.getData();
			List<Map<String, String>> list = resultMap.list;

			DataJsonObj send_jsonObj = new DataJsonObj();
			send_jsonObj.setCode(RE_DELETE_REMOTE);
			send_jsonObj.setObj(OBJ_PHONE);
			try {
				if (TokenManager.judgetToken(resultMap.token.toString()) == null) {
					send_jsonObj.setResult(RE_TOKEN_ERROR);
				} else {
					List<String> rayCode = new ArrayList<String>();
					for (Map<String, String> map : list) {
						rayCode.add(map.get("r_name"));
					}
					if (rayCode.size() != 0) {
						if (infraredDao.deteleBydeviceDisc(rayCode)) {
							send_jsonObj.setResult(RE_RESULT_SUCCESS);
						} else {
							send_jsonObj.setResult(RE_RESULT_FAIL);
						}
					} else {
						send_jsonObj.setResult(RE_RESULT_FAIL);
					}

				}
				TcpConnectionManager.getInstance().sendJson(socketId, send_jsonObj);
			} catch (Exception e) {
				e.printStackTrace();
				MainActivity.showString("请检查传入参数",MultiTextBuffer.TYPE_OTHER);
				send_jsonObj.setResult(RE_ERROR);
				TcpConnectionManager.getInstance().sendJson(socketId, send_jsonObj);
			}
		}
	}

}
