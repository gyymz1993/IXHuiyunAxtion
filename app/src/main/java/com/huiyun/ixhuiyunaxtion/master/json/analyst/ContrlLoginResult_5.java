package com.huiyun.ixhuiyunaxtion.master.json.analyst;

import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;
import com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer;
import com.huiyun.ixhuiyunaxtion.master.bean.json.BaseJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.json.DataJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.table.User;
import com.huiyun.ixhuiyunaxtion.master.dao.UserDao;
import com.huiyun.ixhuiyunaxtion.master.dao.impl.UserDaoImpl;
import com.huiyun.ixhuiyunaxtion.master.net.TokenManager;
import com.huiyun.ixhuiyunaxtion.master.net.tcp.TcpConnectionManager;
import com.huiyun.ixhuiyunaxtion.master.utils.SpUtils;
import com.huiyun.ixhuiyunaxtion.master.utils.UIUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * @Title: ContrlUser.java
 * @Package com.huiyun.master.contrl
 * @Description: 控制登录
 * @author Yangshao
 * @date 2015年1月7日 下午1:26:10
 * @version V1.0
 */
public class ContrlLoginResult_5 extends BaseAnalyst {

	private static final int CODE_LOGIN = 5;
	private static final int RE_CODE_LOGIN = 6;
	private UserDao dao;

	@Override
	public void handleData(int socketId, BaseJsonObj jsonObj) {
		try {
			if (OBJ_MASTER.equals(jsonObj.getObj())
					&& CODE_LOGIN == jsonObj.getCode()) {
				login(jsonObj, socketId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			MainActivity.showString("请检查传入参数", MultiTextBuffer.TYPE_OTHER);
		}
	}

	/**
	 * Function: 用户登录 返回结果
	 * 
	 * 附加返回一个
	 * 
	 * @author Yangshao 2015年1月7日 下午1:39:26
	 * @param jsonObj
	 *            familyId
	 */
	private void login(BaseJsonObj jsonObj, int socketId) {
		if (jsonObj instanceof DataJsonObj) {
			DataJsonObj result_jsonObj = (DataJsonObj) jsonObj;
			Map<String, String> sendMap = new HashMap<String, String>();
			DataJsonObj send_jsonObj = new DataJsonObj();
			send_jsonObj.setCode(RE_CODE_LOGIN);
			send_jsonObj.setObj(OBJ_PHONE);
			try {
				Map<String, String> resultMap = result_jsonObj.getData();
				dao = new UserDaoImpl(UIUtils.getContext());
				User user = new User();
				user.setName(resultMap.get("user").toString());
				user.setPassword(resultMap.get("psw").toString());
				if (dao.queryUser(user) != null) {
					send_jsonObj.setResult(RE_RESULT_SUCCESS);
					sendMap.put("token",
							TokenManager.getANewToken(dao.queryUser(user)));
					sendMap.put("type", dao.queryUser(user).getType() + "");

					/**
					 * 发送家庭ID
					 */
				    String familyId = SpUtils.getValues("familyId");
					sendMap.put("familyId", familyId);
					send_jsonObj.setData(sendMap);
				} else {
					send_jsonObj.setResult(RE_ERROR);
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
