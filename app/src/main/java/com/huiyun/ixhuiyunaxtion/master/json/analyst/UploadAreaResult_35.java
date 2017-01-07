package com.huiyun.ixhuiyunaxtion.master.json.analyst;

import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;
import com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer;
import com.huiyun.ixhuiyunaxtion.master.bean.json.BaseJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.json.DataJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.json.DownJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.table.Area;
import com.huiyun.ixhuiyunaxtion.master.dao.AreaDao;
import com.huiyun.ixhuiyunaxtion.master.dao.impl.AreaDaoImpl;
import com.huiyun.ixhuiyunaxtion.master.json.JsonSendData;
import com.huiyun.ixhuiyunaxtion.master.net.NetworkSupport;
import com.huiyun.ixhuiyunaxtion.master.net.TokenManager;
import com.huiyun.ixhuiyunaxtion.master.net.tcp.TCPClientConnect;
import com.huiyun.ixhuiyunaxtion.master.net.tcp.TcpConnectionManager;
import com.huiyun.ixhuiyunaxtion.master.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class UploadAreaResult_35 extends BaseAnalyst {

	private final int DOWN_AREA = 35;
	private final int RE_DOWN_AREA = 36;
	private AreaDao areaDao = new AreaDaoImpl(UIUtils.getContext());

	@Override
	public void handleData(int socketId, BaseJsonObj jsonObj) {
		try {
			if (OBJ_MASTER.equals(jsonObj.getObj())
					&& DOWN_AREA == jsonObj.getCode()) {
				uploadArea(socketId, jsonObj);
			}
		} catch (Exception e) {
			e.printStackTrace();
			MainActivity.showString("请检查传入参数", MultiTextBuffer.TYPE_OTHER);
		}

	}

	/**
	 * Function:
	 * 
	 * @author Yangshao 2015年1月19日 下午6:04:26
	 * @param socketId
	 * @param jsonObj
	 */
	private void uploadArea(int socketId, BaseJsonObj jsonObj) {
		if (jsonObj instanceof DownJsonObj) {
			final DownJsonObj downJsonObj = (DownJsonObj) jsonObj;
			DownJsonObj.Data resultMap = downJsonObj.getData();

			final DataJsonObj send_jsonObj = new DataJsonObj();
			send_jsonObj.setCode(RE_DOWN_AREA);
			send_jsonObj.setObj(OBJ_PHONE);
			try {
				if (TokenManager.judgetToken(resultMap.token.toString()) == null) {
					send_jsonObj.setResult(RE_TOKEN_ERROR);
				} else {
					List<Map<String, String>> keymaps = downJsonObj.data.list;
					List<Area> areas = new ArrayList<Area>();
					for (Map<String, String> map : keymaps) {
						Area area = new Area();
						area.setAreaName(map.get("area"));
						areas.add(area);
					}
					if (areaDao.saveAndUpdateAreas(areas)) {
						send_jsonObj.setResult(RE_RESULT_SUCCESS);

						// 向服务器发送区域表
						new Thread() {
							public void run() {
								JsonSendData data = new JsonSendData();
								data.uploadAllArea(NetworkSupport
										.getSocketId(TCPClientConnect
												.getInstance().getSocket()));
							};
						}.start();
					} else {
						send_jsonObj.setResult(RE_ERROR);
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
