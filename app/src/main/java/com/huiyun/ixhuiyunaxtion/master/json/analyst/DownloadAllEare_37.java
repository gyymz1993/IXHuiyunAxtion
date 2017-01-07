package com.huiyun.ixhuiyunaxtion.master.json.analyst;

import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;
import com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer;
import com.huiyun.ixhuiyunaxtion.master.bean.json.BaseJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.json.DataJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.json.DownJsonObj;
import com.huiyun.ixhuiyunaxtion.master.dao.AreaDao;
import com.huiyun.ixhuiyunaxtion.master.dao.impl.AreaDaoImpl;
import com.huiyun.ixhuiyunaxtion.master.net.TokenManager;
import com.huiyun.ixhuiyunaxtion.master.net.tcp.TcpConnectionManager;
import com.huiyun.ixhuiyunaxtion.master.utils.UIUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Description: 所有区域下载
 * @date 2015年1月10日 下午5:43:06
 * @version V1.0
 */
public class DownloadAllEare_37 extends BaseAnalyst {

	private final int DOWN_EAEW = 37;
	private final int RE_DOWN_EAEW = 38;
	private AreaDao deviceDao = new AreaDaoImpl(UIUtils.getContext());

	@Override
	public void handleData(int socketId, BaseJsonObj jsonObj) {
		
		try {
			if (OBJ_MASTER.equals(jsonObj.getObj())
					&& DOWN_EAEW == jsonObj.getCode()) {
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
			Map<String, String> data=result_jsonObj.getData();
			DownJsonObj send_jsonObj = new DownJsonObj();
			DownJsonObj.Data send_Data=new DownJsonObj.Data();
			send_jsonObj.setCode(RE_DOWN_EAEW);
			send_jsonObj.setObj(OBJ_PHONE);
			MainActivity.showString("下载设备",MultiTextBuffer.TYPE_OTHER);
			try {
				if (TokenManager.judgetToken(data.get("token")) == null) {
					send_jsonObj.setResult(RE_TOKEN_ERROR);
				} else {
					List<String> allares = deviceDao.getAllAreaforArea();
					if (allares != null && allares.size() > 0) {
						for (String d : allares) {
							Map<String, String> maps = new HashMap<String, String>();
							maps.put("area", d);
							send_Data.list.add(maps);
						}
						send_jsonObj.setData(send_Data);
						send_jsonObj.setResult(RE_RESULT_SUCCESS);
					} else {
						MainActivity.showString("Device数据库为空",MultiTextBuffer.TYPE_OTHER);
						send_jsonObj.setResult(RE_RESULT_FAIL);
					}
					TcpConnectionManager.getInstance().sendJson(socketId, send_jsonObj);
				}
			} catch (Exception e) {
				e.printStackTrace();
				MainActivity.showString("请检查传入参数",MultiTextBuffer.TYPE_OTHER);
				send_jsonObj.setResult(RE_ERROR);
				TcpConnectionManager.getInstance().sendJson(socketId, send_jsonObj);
			}
		}
	}

}
