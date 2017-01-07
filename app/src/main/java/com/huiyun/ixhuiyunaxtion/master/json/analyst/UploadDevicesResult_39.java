package com.huiyun.ixhuiyunaxtion.master.json.analyst;

import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;
import com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer;
import com.huiyun.ixhuiyunaxtion.master.bean.json.BaseJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.json.DataJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.json.DownJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.table.Device;
import com.huiyun.ixhuiyunaxtion.master.dao.DeviceDao;
import com.huiyun.ixhuiyunaxtion.master.dao.impl.DeviceDaoImpl;
import com.huiyun.ixhuiyunaxtion.master.json.JsonSendData;
import com.huiyun.ixhuiyunaxtion.master.net.NetworkSupport;
import com.huiyun.ixhuiyunaxtion.master.net.TokenManager;
import com.huiyun.ixhuiyunaxtion.master.net.tcp.TCPClientConnect;
import com.huiyun.ixhuiyunaxtion.master.net.tcp.TcpConnectionManager;
import com.huiyun.ixhuiyunaxtion.master.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @Description: 上传所有设备
 * @date 2015年1月20日 下午2:04:22
 * @version V1.0
 */
public class UploadDevicesResult_39 extends BaseAnalyst {

	private final int UPLOAD_DEVICE = 39;
	private final int RE_UPLOAD_DEVICE = 40;
	private DeviceDao areaDao = new DeviceDaoImpl(UIUtils.getContext());

	@Override
	public void handleData(int socketId, BaseJsonObj jsonObj) {
		try {
			if (OBJ_MASTER.equals(jsonObj.getObj())
					&& UPLOAD_DEVICE == jsonObj.getCode()) {
				uploadDevices(socketId, jsonObj);
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
	private void uploadDevices(int socketId, BaseJsonObj jsonObj) {
		if (jsonObj instanceof DownJsonObj) {
			final DownJsonObj downJsonObj = (DownJsonObj) jsonObj;
			DownJsonObj.Data resultMap = downJsonObj.getData();

			final DataJsonObj send_jsonObj = new DataJsonObj();
			send_jsonObj.setCode(RE_UPLOAD_DEVICE);
			send_jsonObj.setObj(OBJ_PHONE);
			try {
				if (TokenManager.judgetToken(resultMap.token.toString()) == null) {
					send_jsonObj.setResult(RE_TOKEN_ERROR);
					TcpConnectionManager.getInstance().sendJson(socketId, send_jsonObj);
				} else {
					List<Map<String, String>> keymaps = downJsonObj.data.list;
					List<Device> devices = new ArrayList<Device>();
					for (Map<String, String> map : keymaps) {
						Device device = new Device();
						device.setArea(map.get("area"));
						device.setName(map.get("name"));
						device.setPhoneCode(Integer.valueOf(map
								.get("phoneCode")));
						device.setDetails(map.get("detail"));
						device.setType(Integer.valueOf(map.get("type")));
						device.setAddress(Integer.valueOf(map.get("address")));
						device.setNumber(Integer.valueOf(map.get("number")));
						devices.add(device);
					}
					if (areaDao.UpdateAreaAndNameOrDeleteForDevices(devices)) {
						send_jsonObj.setResult(RE_RESULT_SUCCESS);
						
						// 向服务器发送输出设备表
						new Thread(){
							public void run() {
								JsonSendData data = new JsonSendData();
								data.uploadAllDevice(NetworkSupport.getSocketId(
										TCPClientConnect.getInstance().getSocket()));
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
