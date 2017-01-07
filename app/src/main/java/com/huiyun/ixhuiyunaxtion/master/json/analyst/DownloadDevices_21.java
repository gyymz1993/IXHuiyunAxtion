package com.huiyun.ixhuiyunaxtion.master.json.analyst;

import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;
import com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer;
import com.huiyun.ixhuiyunaxtion.master.bean.json.BaseJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.json.DataJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.json.Down2JsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.json.DownJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.table.Device;
import com.huiyun.ixhuiyunaxtion.master.dao.DeviceDao;
import com.huiyun.ixhuiyunaxtion.master.dao.impl.DeviceDaoImpl;
import com.huiyun.ixhuiyunaxtion.master.net.TokenManager;
import com.huiyun.ixhuiyunaxtion.master.net.tcp.TcpConnectionManager;
import com.huiyun.ixhuiyunaxtion.master.utils.UIUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 所有设备下载
 * @date 2015年1月10日 下午5:43:06
 * @version V1.0
 */
public class DownloadDevices_21 extends BaseAnalyst {

	private final int DOWN_DEVICE = 21;
	private final int RE_DOWN_DEVICE = 22;
	private DeviceDao deviceDao = new DeviceDaoImpl(UIUtils.getContext());

	@Override
	public void handleData(int socketId, BaseJsonObj jsonObj) {
		
		try {
			if (OBJ_MASTER.equals(jsonObj.getObj())
					&& DOWN_DEVICE == jsonObj.getCode()) {
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
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			DownJsonObj.Data sendData=new DownJsonObj.Data();
			
			DataJsonObj result_jsonObj = (DataJsonObj) jsonObj;
			Map<String, String> resultMap = result_jsonObj.getData();
			
			DownJsonObj send_jsonObj = new DownJsonObj();
			send_jsonObj.setCode(RE_DOWN_DEVICE);
			send_jsonObj.setObj(OBJ_PHONE);
			try {
				if (TokenManager.judgetToken(resultMap.get("token").toString()) == null) {
					send_jsonObj.setResult(RE_TOKEN_ERROR);
				} else {
					List<Device> allDeivices = deviceDao.queryAllInDevices();
					if (allDeivices != null && allDeivices.size() > 0) {
						for (Device d : allDeivices) {
							Map<String, String> maps = new HashMap<String, String>();
							maps.put("area", d.getArea());
							maps.put("name", d.getName());
							maps.put("type", d.getType() + "");
							maps.put("detail", d.getDetails());
							maps.put("address", d.getAddress() + "");
							maps.put("number", d.getNumber() + "");
							list.add(maps);
						}
						sendData.list=list;
						send_jsonObj.setData(sendData);
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
