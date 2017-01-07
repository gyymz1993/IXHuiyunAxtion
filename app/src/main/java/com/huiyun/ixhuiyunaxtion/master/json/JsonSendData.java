package com.huiyun.ixhuiyunaxtion.master.json;

import com.google.gson.Gson;
import com.huiyun.ixhuiyunaxtion.master.StaticValues;
import com.huiyun.ixhuiyunaxtion.master.bean.json.BaseJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.json.DataJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.json.DownJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.table.Device;
import com.huiyun.ixhuiyunaxtion.master.bean.table.RedRay;
import com.huiyun.ixhuiyunaxtion.master.bean.table.SceneItem;
import com.huiyun.ixhuiyunaxtion.master.bean.table.User;
import com.huiyun.ixhuiyunaxtion.master.bean.table.Webcam;
import com.huiyun.ixhuiyunaxtion.master.cmd.StateStorage;
import com.huiyun.ixhuiyunaxtion.master.dao.AreaDao;
import com.huiyun.ixhuiyunaxtion.master.dao.DeviceDao;
import com.huiyun.ixhuiyunaxtion.master.dao.RedRayDao;
import com.huiyun.ixhuiyunaxtion.master.dao.SceneDao;
import com.huiyun.ixhuiyunaxtion.master.dao.UserDao;
import com.huiyun.ixhuiyunaxtion.master.dao.WebCamDao;
import com.huiyun.ixhuiyunaxtion.master.dao.impl.AreaDaoImpl;
import com.huiyun.ixhuiyunaxtion.master.dao.impl.DeviceDaoImpl;
import com.huiyun.ixhuiyunaxtion.master.dao.impl.RedRayDaoImpl;
import com.huiyun.ixhuiyunaxtion.master.dao.impl.SceneDaoImpl;
import com.huiyun.ixhuiyunaxtion.master.dao.impl.UserDaoImpl;
import com.huiyun.ixhuiyunaxtion.master.dao.impl.WebCamDaoImpl;
import com.huiyun.ixhuiyunaxtion.master.net.tcp.TcpConnectionManager;
import com.huiyun.ixhuiyunaxtion.master.utils.UIUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Description: 传送到服务器的数据
 * @date 2015-1-24 下午4:42:43
 * @version V1.0
 * @author busy
 */
public class JsonSendData {
	private DataJsonObj send_jsonObj;
	private DownJsonObj send_jsonObj_;
	private String Obj = "server";
	private Gson gson;

	/**
	 * 
	 * Function:主机登录
	 * 
	 * @author busy 2015-1-26 下午2:39:19
	 * @param socketId
	 * @param masterid
	 */
	public void masterLogin(int socketId) {
		Map<String, String> sendMap = new HashMap<String, String>();
		sendMap.put("masterid", StaticValues.MASTER_ID);
		send_jsonObj = new DataJsonObj();
		send_jsonObj.setObj(Obj);
		send_jsonObj.setCode(205);
		send_jsonObj.setResult(1);
		send_jsonObj.setData(sendMap);
		send(socketId, send_jsonObj);
	}

	/**
	 * 
	 * Function:主机主动上传所有的用户
	 * 
	 * @author busy 2015-1-26 下午2:39:35
	 * @param socketId
	 * @param token
	 */
	public void uploadUser(int socketId) {
		UserDao userDao = new UserDaoImpl(UIUtils.getContext());
		List<User> listUser = userDao.queryAllUser();
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		DownJsonObj.Data data = new DownJsonObj.Data();
		if (listUser != null && listUser.size() > 0) {
			for (User user : listUser) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("name", user.getName());
				map.put("password", user.getPassword());
				map.put("type", user.getType() + "");
				list.add(map);
			}
		}
		data.token = StaticValues.SERVER_TOKEN;
		data.list = list;
		send_jsonObj_ = new DownJsonObj();
		send_jsonObj_.setObj(Obj);
		send_jsonObj_.setCode(201);
		send_jsonObj_.setResult(1);
		send_jsonObj_.setData(data);
		send(socketId, send_jsonObj_);
	}

	/**
	 * 
	 * Function:上传所有输出设备
	 * 
	 * @author busy 2015-1-28 下午2:50:37
	 * @param socketId
	 */
	public void uploadAllDevice(int socketId) {
//		DeviceDao deviceDao = new DeviceDaoImpl(UIUtils.getContext());
//		List<Device> allDeivices = deviceDao.queryAllDevices();
		// 从statestorage不但能获得所有输出设备，还能获得准确的状态
		List<Device> allDeivices = StateStorage.getInstance().readAllState();
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		DownJsonObj.Data data = new DownJsonObj.Data();
		if (allDeivices != null && allDeivices.size() > 0) {
			for (Device d : allDeivices) {
				Map<String, String> maps = new HashMap<String, String>();
				maps.put("area", d.getArea());
				maps.put("name", d.getName());
				maps.put("phoneCode", d.getPhoneCode() + "");
				maps.put("type", d.getType() + "");
				maps.put("details", d.getDetails());
				maps.put("address", d.getAddress() + "");
				maps.put("number", d.getNumber() + "");
				maps.put("state", d.getState() + "");
				list.add(maps);
			}
		}
		data.token = StaticValues.SERVER_TOKEN;
		data.list = list;
		send_jsonObj_ = new DownJsonObj();
		send_jsonObj_.setObj(Obj);
		send_jsonObj_.setCode(203);
		send_jsonObj_.setResult(1);
		send_jsonObj_.setData(data);
		send(socketId, send_jsonObj_);
	}

	/**
	 * 
	 * Function:上传红外码表（废弃）
	 * 
	 * @author busy
	 *  2015-1-28 下午5:50:33
	 * @param socketId
	 */
	@Deprecated
	public void uploadAllRedcode(int socketId) {
		RedRayDao redDao = new RedRayDaoImpl(UIUtils.getContext());
		List<RedRay> allRedCode = redDao.queryAllDeviceDisc();
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		DownJsonObj.Data data = new DownJsonObj.Data();
		if (allRedCode != null && allRedCode.size() > 0) {
			for (RedRay d : allRedCode) {
				Map<String, String> maps = new HashMap<String, String>();
				maps.put("btn_code", d.getBtn_code() + "");
				maps.put("r_name", d.getR_name());
				maps.put("pageType", d.getPageType() + "");
				list.add(maps);
			}
		}
		data.token = StaticValues.SERVER_TOKEN;
		data.list = list;
		send_jsonObj_ = new DownJsonObj();
		send_jsonObj_.setObj(Obj);
		send_jsonObj_.setCode(207);
		send_jsonObj_.setResult(1);
		send_jsonObj_.setData(data);
		send(socketId, send_jsonObj_);
	}

	
	 /**
	 *  Function:上传新的红外码表
	 *  @author lzn 
	 *  2015-4-24 上午10:19:57
	 *  @param socketId
	 */
	public void uploadAllNewRedcode(int socketId){
		RedRayDao redDao = new RedRayDaoImpl(UIUtils.getContext());
		List<RedRay> allRedCode = redDao.queryAllRedRays();
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		DownJsonObj.Data data = new DownJsonObj.Data();
		if (allRedCode != null && allRedCode.size() > 0) {
			for (RedRay d : allRedCode) {
				Map<String, String> maps = new HashMap<String, String>();
				maps.put("btn_code", d.getBtn_code() + "");
				maps.put("r_name", d.getR_name());
				maps.put("pageType", d.getPageType() + "");
				list.add(maps);
			}
		}
		data.token = StaticValues.SERVER_TOKEN;
		data.list = list;
		send_jsonObj_ = new DownJsonObj();
		send_jsonObj_.setObj(Obj);
		send_jsonObj_.setCode(207);
		send_jsonObj_.setResult(1);
		send_jsonObj_.setData(data);
		send(socketId, send_jsonObj_);
	}
	
	/**
	 * 
	 *  Function:上传所有的家庭区域
	 *  @author busy 
	 *  2015-1-28 下午6:27:52
	 *  @param socketId
	 */
	public void uploadAllArea(int socketId) {
		AreaDao areaDao = new AreaDaoImpl(UIUtils.getContext());
		List<String> allArea = areaDao.getAllAreaforArea();
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		DownJsonObj.Data data = new DownJsonObj.Data();
		if (allArea != null && allArea.size() > 0) {
			for (String d : allArea) {
				Map<String, String> maps = new HashMap<String, String>();
				maps.put("area", d);
				list.add(maps);
			}
		}
		data.token = StaticValues.SERVER_TOKEN;
		data.list = list;
		send_jsonObj_ = new DownJsonObj();
		send_jsonObj_.setObj(Obj);
		send_jsonObj_.setCode(209);
		send_jsonObj_.setResult(1);
		send_jsonObj_.setData(data);
		send(socketId, send_jsonObj_);
	}
	
	/**
	 * 
	 *  Function:上传所有场景
	 *  @author Yangshao 
	 *  2015-1-28 下午6:31:57
	 *  @param socketId
	 */
	public void uploadAllScene(int socketId) {
		SceneDao sceneDao = new SceneDaoImpl(UIUtils.getContext());
		List<SceneItem> allScene = sceneDao.getSceneName();
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		DownJsonObj.Data data = new DownJsonObj.Data();
		if (allScene != null && allScene.size() > 0) {
			for (SceneItem d : allScene) {
				Map<String, String> maps = new HashMap<String, String>();
				maps.put("scene", d.getScene_name());
				maps.put("image_bg", d.getImage_bg()+"");
				list.add(maps);
			}
		}
		data.token = StaticValues.SERVER_TOKEN;
		data.list = list;
		send_jsonObj_ = new DownJsonObj();
		send_jsonObj_.setObj(Obj);
		send_jsonObj_.setCode(211);
		send_jsonObj_.setResult(1);
		send_jsonObj_.setData(data);
		send(socketId, send_jsonObj_);
	}

	/**
	 * 
	 * Function:主机开关值变化主动通知
	 * 
	 * @author Yangshao 2015-1-26 下午2:40:11
	 * @param socketId
	 * @param token
	 */
	public void switchChangeInfo(int socketId) {
		DeviceDao deviceDao = new DeviceDaoImpl(UIUtils.getContext());
		List<Device> listDevice = deviceDao.queryAllDevices();
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		DownJsonObj.Data data = new DownJsonObj.Data();
		if (listDevice != null && listDevice.size() > 0) {
			for (Device d : listDevice) {
				Map<String, String> maps = new HashMap<String, String>();
				maps.put("phone_code", d.getPhoneCode() + "");
				maps.put("state", d.getState() + "");
				list.add(maps);
			}
		}
		data.token = StaticValues.SERVER_TOKEN;
		data.list = list;
		send_jsonObj_ = new DownJsonObj();
		send_jsonObj_.setObj(Obj);
		send_jsonObj_.setCode(24);
		send_jsonObj_.setResult(1);
		send_jsonObj_.setData(data);
		send(socketId, send_jsonObj_);
	}
	
	/**
	 * 
	 *  Function:上传摄像头
	 *  @author busy 
	 *  2015-3-9 上午11:28:55
	 *  @param socketId
	 */
	public void uploadCameraName(int socketId){
		WebCamDao cameraDao = new WebCamDaoImpl(UIUtils.getContext());
		List<Webcam> allCamera = cameraDao.getAllWebCam();
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		DownJsonObj.Data data = new DownJsonObj.Data();
		if (allCamera != null && allCamera.size() > 0) {
			for (Webcam c : allCamera) {
				Map<String, String> maps = new HashMap<String, String>();
				maps.put("did",c.getDid());
				maps.put("area", c.getArea());
				list.add(maps);
			}
		}
		data.token = StaticValues.SERVER_TOKEN;
		data.list = list;
		send_jsonObj_ = new DownJsonObj();
		send_jsonObj_.setObj(Obj);
		send_jsonObj_.setCode(213);
		send_jsonObj_.setResult(1);
		send_jsonObj_.setData(data);
		send(socketId, send_jsonObj_);
	}
	
	 /**
	 *  Function: 发送命令
	 *  @author lzn 
	 *  2015-1-29 上午11:09:44
	 *  @param socketId
	 *  @param send_jsonObj
	 */
	private void send(int socketId, BaseJsonObj send_jsonObj) {
		TcpConnectionManager.getInstance().sendJson(socketId, send_jsonObj);
	}

	@SuppressWarnings("unused")
	private void toString(Object o) {
		gson = new Gson();
		String value = gson.toJson(o);
	}

}
