package com.huiyun.ixhuiyunaxtion.master.json.analyst;

import com.huiyun.ixhuiyunaxtion.master.StaticValues;
import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;
import com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer;
import com.huiyun.ixhuiyunaxtion.master.bean.json.BaseJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.json.DataJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.table.User;
import com.huiyun.ixhuiyunaxtion.master.dao.UserDao;
import com.huiyun.ixhuiyunaxtion.master.dao.impl.UserDaoImpl;
import com.huiyun.ixhuiyunaxtion.master.json.JsonSendData;
import com.huiyun.ixhuiyunaxtion.master.net.NetworkSupport;
import com.huiyun.ixhuiyunaxtion.master.net.TokenManager;
import com.huiyun.ixhuiyunaxtion.master.net.tcp.TCPClientConnect;
import com.huiyun.ixhuiyunaxtion.master.net.tcp.TcpConnectionManager;
import com.huiyun.ixhuiyunaxtion.master.utils.UIUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * @Title: ContrlUser.java
 * @Package com.huiyun.master.contrl
 * @Description: 注册管理员
 * @author Yangshao
 * @date 2015年1月7日 下午1:26:10
 * @version V1.0
 */
public class RegisterAdminResult_1 extends BaseAnalyst {

	// 注册
	private static final int CODE_REGISTER = 1;
	private static final int RE_CODE_REGISTER = 2;

	private UserDao dao;

	@Override
	public void handleData(int socketId, BaseJsonObj jsonObj) {
		try {
			if (OBJ_MASTER.equals(jsonObj.getObj())
					&& CODE_REGISTER == jsonObj.getCode()) {
				resgister(jsonObj, socketId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			MainActivity.showString("请检查传入参数",MultiTextBuffer.TYPE_OTHER);
		}
	}

	/**
	 * Function:管理员注册 返回
	 * 
	 * @author Yangshao 2015年1月7日 下午1:38:52
	 * 
	 *         masterId:主机id username:用户名 psw：密码
	 */
	private void resgister(BaseJsonObj jsonObj, int socketId) {
		if (jsonObj instanceof DataJsonObj) {
			DataJsonObj result_jsonObj = (DataJsonObj) jsonObj;
			Map<String, String> resultMap = result_jsonObj.getData();
			Map<String, String> sendMap = new HashMap<String, String>();
			DataJsonObj send_jsonObj = new DataJsonObj();
			send_jsonObj.setCode(RE_CODE_REGISTER);
			send_jsonObj.setObj(OBJ_PHONE);
			try {
			
				/**
				 * 返回masterid错误
				 */
				if (!resultMap.get("masterId").equalsIgnoreCase(StaticValues.MASTER_ID)
					&& !resultMap.get("masterId").equalsIgnoreCase("123456789")) {
					send_jsonObj.setResult(RE_RESULT_FAIL);
				} else {
					User user = new User();
					user.setName(resultMap.get("user").toString());
					user.setPassword(resultMap.get("psw").toString());
					user.setType(1);
					dao = new UserDaoImpl(UIUtils.getContext());
					if (dao.saveOrupdateForce(user)) {
						send_jsonObj.setResult(RE_RESULT_SUCCESS);
						sendMap.put("token", TokenManager.getANewToken(user));
						sendMap.put("user", resultMap.get("user").toString());
						send_jsonObj.setData(sendMap);
						
						// 向服务器发送用户表
						new Thread(){
							public void run() {
								JsonSendData data = new JsonSendData();
								data.uploadUser(NetworkSupport.getSocketId(
										TCPClientConnect.getInstance().getSocket()));
							};
						}.start();
					} else {
						MainActivity.showString("数据库错误", MultiTextBuffer.TYPE_OTHER);
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
