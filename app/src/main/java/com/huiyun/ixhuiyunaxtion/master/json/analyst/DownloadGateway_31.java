package com.huiyun.ixhuiyunaxtion.master.json.analyst;

import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;
import com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer;
import com.huiyun.ixhuiyunaxtion.master.bean.json.BaseJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.json.DataJsonObj;
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
 * @Description: 下载网关设备返回
 * @date 2015年1月10日 下午5:43:06
 * @version V1.0
 */
public class DownloadGateway_31 extends BaseAnalyst {

	private final int DOWN_GATEWAY = 31;
	private final int RE_DOWN_GATEWAY = 32;
	private DeviceDao deviceDao = new DeviceDaoImpl(UIUtils.getContext());

	@Override
	public void handleData(int socketId, BaseJsonObj jsonObj) {
		
		try {
			if (OBJ_MASTER.equals(jsonObj.getObj())
					&& DOWN_GATEWAY == jsonObj.getCode()) {
				startDownGateway(socketId, jsonObj);
			}
		} catch (Exception e) {
			e.printStackTrace();
			MainActivity.showString("请检查传入参数",MultiTextBuffer.TYPE_OTHER);
		}
		
	}

	
	 /**
	 *  Function:  开始下载网关设备
	 *  @author Yangshao 
	 *  2015年1月15日 下午1:14:02
	 *  @param socketId
	 *  @param jsonObj
	 */
	private void startDownGateway(int socketId, BaseJsonObj jsonObj) {
		if (jsonObj instanceof DataJsonObj) {
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			DataJsonObj result_jsonObj = (DataJsonObj) jsonObj;
			Map<String, String> resultMap = result_jsonObj.getData();
			DownJsonObj send_jsonObj = new DownJsonObj();
			DownJsonObj.Data sendData=new DownJsonObj.Data();
			send_jsonObj.setCode(RE_DOWN_GATEWAY);
			send_jsonObj.setObj(OBJ_PHONE);
			try {
				if (TokenManager.judgetToken(resultMap.get("token").toString()) == null) {
					send_jsonObj.setResult(RE_TOKEN_ERROR);
				} else {
					resultMap.remove("token");
					List<Device> allDeivices = deviceDao.getAllGateway();
					if (allDeivices != null && allDeivices.size() > 0) {
						for (Device d : allDeivices) {
							Map<String, String> maps = new HashMap<String, String>();
							maps.put("address", d.getAddress()+"");
							maps.put("type", d.getType() + "");
							list.add(maps);
						}
						sendData.list=list;
						send_jsonObj.setData(sendData);
						send_jsonObj.setResult(RE_RESULT_SUCCESS);
					} else {
						MainActivity.showString("Device数据库为空", MultiTextBuffer.TYPE_OTHER);
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
