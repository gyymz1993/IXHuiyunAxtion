package com.huiyun.ixhuiyunaxtion.master.json.analyst;

import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;
import com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer;
import com.huiyun.ixhuiyunaxtion.master.bean.json.BaseJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.json.DataJsonObj;
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
 * @Description: 下载所有遥控器
 * @date 2015年1月10日 下午5:43:06
 * @version V1.0
 */
public class DownloadRedRay_15 extends BaseAnalyst {

	private final int DOWN_REMOTO = 15;
	private final int RE_DOWN_REMOTO = 16;
	private RedRayDao infraredDao = new RedRayDaoImpl(UIUtils.getContext());

	@Override
	public void handleData(int socketId, BaseJsonObj jsonObj) {
		try {
			if (OBJ_MASTER.equals(jsonObj.getObj())
					&& DOWN_REMOTO == jsonObj.getCode()) {
				startDownDevices(socketId, jsonObj);
			}
		} catch (Exception e) {
			e.printStackTrace();
			MainActivity.showString("请检查传入参数", MultiTextBuffer.TYPE_OTHER);
		}

	}

	/**
	 * Function:
	 * 
	 * @author Yangshao 2015年1月10日 上午9:53:18
	 * @param jsonObj
	 * @param socketId
	 */
	private void startDownDevices(int socketId, BaseJsonObj jsonObj) {
		if (jsonObj instanceof DataJsonObj) {
			DataJsonObj result_jsonObj = (DataJsonObj) jsonObj;
			Map<String, String> resultMap = result_jsonObj.getData();

			List<Map<String, String>> send_list = new ArrayList<Map<String, String>>();
			DownJsonObj send_jsonObj = new DownJsonObj();
			DownJsonObj.Data data = new DownJsonObj.Data();
			send_jsonObj.setCode(RE_DOWN_REMOTO);
			send_jsonObj.setObj(OBJ_PHONE);
			try {
				if (TokenManager.judgetToken(resultMap.get("token").toString()) == null) {
					send_jsonObj.setResult(RE_TOKEN_ERROR);
				} else {
					List<RedRay> aRayCodes = infraredDao.queryAllDeviceDisc();
					MainActivity.showString("查找到遥控器" + aRayCodes.size() + "个",
							MultiTextBuffer.TYPE_OTHER);
					if (aRayCodes.size() != 0) {
						for (RedRay d : aRayCodes) {
							Map<String, String> maps = new HashMap<String, String>();
							maps.put("r_name", d.getR_name());
							maps.put("pageType", d.getPageType() + "");
							send_list.add(maps);
						}
						data.list = send_list;
						send_jsonObj.setData(data);
					} 
					send_jsonObj.setResult(RE_RESULT_SUCCESS);
					TcpConnectionManager.getInstance().sendJson(socketId,
							send_jsonObj);
				}
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
