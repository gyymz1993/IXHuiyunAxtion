package com.huiyun.ixhuiyunaxtion.master.json.analyst;

import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;
import com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer;
import com.huiyun.ixhuiyunaxtion.master.bean.json.BaseJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.json.DataJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.table.RedRay;
import com.huiyun.ixhuiyunaxtion.master.dao.RedRayDao;
import com.huiyun.ixhuiyunaxtion.master.dao.impl.RedRayDaoImpl;
import com.huiyun.ixhuiyunaxtion.master.net.TokenManager;
import com.huiyun.ixhuiyunaxtion.master.net.tcp.TcpConnectionManager;
import com.huiyun.ixhuiyunaxtion.master.utils.UIUtils;

import java.util.Map;



public class UploadRedRayResult_17 extends BaseAnalyst {

	private final int UPLOAD_REMOTE = 17;
	private final int RE_UPLOAD_REMOTE = 18;
	RedRayDao infraredDao = new RedRayDaoImpl(UIUtils.getContext());

	@Override
	public void handleData(int socketId, BaseJsonObj jsonObj) {
		try {
			if (OBJ_MASTER.equals(jsonObj.getObj())
					&& UPLOAD_REMOTE == jsonObj.getCode()) {
				uploadRemote(jsonObj, socketId);
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
	private void uploadRemote(BaseJsonObj jsonObj, int socketId) {
		if (jsonObj instanceof DataJsonObj) {
			DataJsonObj result_jsonObj = (DataJsonObj) jsonObj;
			Map<String, String> resultMap = result_jsonObj.getData();

			DataJsonObj send_jsonObj = new DataJsonObj();
			send_jsonObj.setCode(RE_UPLOAD_REMOTE);
			send_jsonObj.setObj(OBJ_PHONE);
			try {
				if (TokenManager.judgetToken(resultMap.get("token").toString()) == null) {
					send_jsonObj.setResult(RE_TOKEN_ERROR);
				} else {
					RedRay rayCode = new RedRay();
					rayCode.setR_name(resultMap.get("r_name"));
					rayCode.setPageType(Integer.valueOf(resultMap
							.get("pageType")));
					if (infraredDao.addRedRay(rayCode)) {
						send_jsonObj.setResult(RE_RESULT_SUCCESS);
					} else {
						send_jsonObj.setResult(RE_RESULT_FAIL);
					}

				}
				TcpConnectionManager.getInstance().sendJson(socketId,
						send_jsonObj);
			} catch (Exception e) {
				e.printStackTrace();
				MainActivity.showString("请检查传入参数", MultiTextBuffer.TYPE_OTHER);
				send_jsonObj.setResult(RE_ERROR);
				TcpConnectionManager.getInstance().sendJson(socketId,
						send_jsonObj);
			}
		}
	}

}
