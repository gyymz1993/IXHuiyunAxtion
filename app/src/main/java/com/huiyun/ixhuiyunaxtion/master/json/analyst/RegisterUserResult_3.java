package com.huiyun.ixhuiyunaxtion.master.json.analyst;

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
 * @Description: 注册普通用户
 * @author Yangshao
 * @date 2015年1月7日 下午1:26:10
 * @version V1.0
 */
public class RegisterUserResult_3 extends BaseAnalyst {

	private static final int CODE_REGISTER_USER = 3;
	private static final int RE_CODE_REGISTER_USER = 4;

	private UserDao dao;

	@Override
	public void handleData(int socketId, BaseJsonObj jsonObj) {
		try {
			if (OBJ_MASTER.equals(jsonObj.getObj())
					&& CODE_REGISTER_USER == jsonObj.getCode()) {
				resgister(jsonObj, socketId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			MainActivity.showString("请检查传入参数", MultiTextBuffer.TYPE_OTHER);
		}
	}

	/**
	 * Function:用户注册 返回
	 * 
	 * @author Yangshao 2015年1月7日 下午1:38:52
	 * 
	 *         token:唯一标识 username:用户名 psw：密码
	 */
	@SuppressWarnings("null")
	private void resgister(BaseJsonObj jsonObj, int socketId) {
		if (jsonObj instanceof DataJsonObj) {
			DataJsonObj result_jsonObj = (DataJsonObj) jsonObj;
			Map<String, String> resultMap = result_jsonObj.getData();
			Map<String, String> sendMap = new HashMap<String, String>();
			
			/**
			 * 发送的
			 */
			DataJsonObj send_jsonObj = new DataJsonObj();
			send_jsonObj.setCode(RE_CODE_REGISTER_USER);
			send_jsonObj.setObj(OBJ_PHONE);
			try {
				/**
				 * 返回权限不够
				 */
				User users= TokenManager.judgetToken(resultMap.get("token"));
				if (users == null&& users.getType() != 1) {
					send_jsonObj.setResult(RE_RESULT_FAIL);
				}else {
					User user = new User();
					user.setName(resultMap.get("user").toString());
					user.setPassword(resultMap.get("psw").toString());
					user.setType(2);
					dao = new UserDaoImpl(UIUtils.getContext());
					if (dao.saveOrupdateForce(user)) {
						send_jsonObj.setResult(RE_RESULT_SUCCESS);
						sendMap.put("token", TokenManager.getANewToken(user));
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
						MainActivity.showString("存入数据库失败",MultiTextBuffer.TYPE_OTHER);
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
