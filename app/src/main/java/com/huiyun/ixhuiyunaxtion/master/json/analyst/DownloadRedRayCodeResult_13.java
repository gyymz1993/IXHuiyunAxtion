package com.huiyun.ixhuiyunaxtion.master.json.analyst;

import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;
import com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer;
import com.huiyun.ixhuiyunaxtion.master.bean.json.BaseJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.json.DataJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.json.Down2JsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.json.DownJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.table.RedRay;
import com.huiyun.ixhuiyunaxtion.master.dao.RedRayDao;
import com.huiyun.ixhuiyunaxtion.master.dao.impl.RedRayDaoImpl;
import com.huiyun.ixhuiyunaxtion.master.net.TokenManager;
import com.huiyun.ixhuiyunaxtion.master.net.tcp.TcpConnectionManager;
import com.huiyun.ixhuiyunaxtion.master.utils.UIUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 下载所有红外码
 * @date 2015年1月10日 上午9:48:58
 * @version V1.0
 */
@Deprecated
public class DownloadRedRayCodeResult_13 extends BaseAnalyst {

	private final int DOWN_REDRAY = 13;
	private final int RE_DOWN_REDRAY = 14;
	private RedRayDao infraredDao = new RedRayDaoImpl(UIUtils.getContext());

	@Override
	public void handleData(int socketId, BaseJsonObj jsonObj) {
		try {
			if (OBJ_MASTER.equals(jsonObj.getObj())
					&& DOWN_REDRAY == jsonObj.getCode()) {
				startDownRedRay(socketId, jsonObj);
			}
		} catch (Exception e) {
			e.printStackTrace();
			MainActivity.showString("请检查传入参数", MultiTextBuffer.TYPE_OTHER);
		}
	}

	/**
	 * Function:开启下载红外设备
	 * 
	 * @author Yangshao 2015年1月10日 上午9:53:18
	 * @param jsonObj
	 * @param socketId
	 */
	private void startDownRedRay(int socketId, BaseJsonObj jsonObj) {
		if (jsonObj instanceof DataJsonObj) {
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			DataJsonObj result_jsonObj = (DataJsonObj) jsonObj;
			Map<String, String> resultMap = result_jsonObj.getData();
			DownJsonObj.Data sendData = new DownJsonObj.Data();
			DownJsonObj send_jsonObj = new DownJsonObj();
			send_jsonObj.setCode(RE_DOWN_REDRAY);
			send_jsonObj.setObj(OBJ_PHONE);
			try {
				if (TokenManager.judgetToken(resultMap.get("token").toString()) == null) {
					send_jsonObj.setResult(RE_TOKEN_ERROR);
				} else {
					List<RedRay> listCodes = infraredDao.queryAllRedRays();
					if (listCodes != null) {
						for (RedRay rayCode : listCodes) {
							Map<String, String> maps = new HashMap<String, String>();
							maps.put("btn_code",
									rayCode.getBtn_code() + "");
							maps.put("r_name", rayCode.getR_name());
							maps.put("pageType", rayCode.getPageType() + "");
							list.add(maps);
						}
						sendData.list = list;
						send_jsonObj.setData(sendData);
						send_jsonObj.setResult(RE_RESULT_SUCCESS);
					} else {
						MainActivity.showString("数据库出错",
								MultiTextBuffer.TYPE_OTHER);
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
