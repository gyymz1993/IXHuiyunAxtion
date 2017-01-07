package com.huiyun.ixhuiyunaxtion.master.json.analyst;

import com.huiyun.ixhuiyunaxtion.master.StaticValues;
import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;
import com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer;
import com.huiyun.ixhuiyunaxtion.master.bean.json.BaseJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.json.DataJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.table.RedRay;
import com.huiyun.ixhuiyunaxtion.master.cmd.redcode.RedCodeProcessor;
import com.huiyun.ixhuiyunaxtion.master.dao.RedRayDao;
import com.huiyun.ixhuiyunaxtion.master.dao.impl.RedRayDaoImpl;
import com.huiyun.ixhuiyunaxtion.master.inner.OnResultListener;
import com.huiyun.ixhuiyunaxtion.master.net.TokenManager;
import com.huiyun.ixhuiyunaxtion.master.net.tcp.TcpConnectionManager;
import com.huiyun.ixhuiyunaxtion.master.utils.UIUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * @Title: Contrl_Infrared_9.java
 * @Package com.huiyun.master.activity.analyst
 * @Description:控制红外码
 * @author Yangshao
 * @date 2015年1月7日 下午4:15:13
 * @version V1.0
 */
public class ContrlRedRayResult_11 extends BaseAnalyst {
	private static final int CODE_INFARED = 11;
	private static final int RE_CODE_INFARED = 12;
	private RedRayDao dao = new RedRayDaoImpl(UIUtils.getContext());

	@Override
	public void handleData(int socketId, BaseJsonObj jsonObj) {
		try {
			if (OBJ_MASTER.equals(jsonObj.getObj())
					&& CODE_INFARED == jsonObj.getCode()) {
				startInfraredStudy(jsonObj, socketId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			MainActivity.showString("请检查传入参数", MultiTextBuffer.TYPE_OTHER);
		}
	}

	/**
	 * Function: 控制红外 返回结果
	 * 
	 * @author Yangshao 2015年1月7日 下午4:20:06
	 * @param jsonObj
	 * @param socketId
	 */
	private void startInfraredStudy(BaseJsonObj jsonObj, final int socketId) {
		if (jsonObj instanceof DataJsonObj) {
			DataJsonObj result_jsonObj = (DataJsonObj) jsonObj;
			final Map<String, String> sendMap = new HashMap<String, String>();
			final DataJsonObj send_jsonObj = new DataJsonObj();
			send_jsonObj.setCode(RE_CODE_INFARED);
			send_jsonObj.setObj(OBJ_PHONE);
			try {
				Map<String, String> resultMap = result_jsonObj.getData();
				if (TokenManager.judgetToken(resultMap.get("token")) == null
						&& (!resultMap.get("token").equals(
								StaticValues.SERVER_TOKEN))) {
					send_jsonObj.setResult(RE_TOKEN_ERROR);
					TcpConnectionManager.getInstance().sendJson(socketId,
							send_jsonObj);
				} else {
					final int btn_code = Integer.valueOf(resultMap
							.get("btn_code"));
					final String r_name = resultMap.get("r_name");
					final int pageType = Integer.valueOf(resultMap
							.get("pageType"));
					RedRay redRay = new RedRay();
					redRay.setBtn_code(btn_code);
					redRay.setR_name(r_name + "");
					RedRay contrlRay=dao.queryNumberAndAdress(redRay);
					if ( contrlRay!= null) {
						System.out.println(contrlRay.toString());
						RedCodeProcessor.getInstance().control(
								contrlRay.getD_address(), contrlRay.getD_code());
						RedCodeProcessor
								.getInstance()
								.setOnControlListener(
										new OnResultListener<RedCodeProcessor.SimpleRedCodeObj>() {
											@Override
											public void onResult(
													boolean isSucceed,
													RedCodeProcessor.SimpleRedCodeObj obj) {
												if (isSucceed) {
													send_jsonObj
															.setResult(RE_RESULT_SUCCESS);
													sendMap.put("btn_code",
															btn_code + "");
													sendMap.put("pageType",
															pageType + "");
													sendMap.put("r_name",
															r_name + "");
													send_jsonObj
															.setData(sendMap);
												} else {
													// 控制失败
													send_jsonObj
															.setResult(RE_RESULT_FAIL);

												}
												TcpConnectionManager
														.getInstance()
														.sendJson(socketId,
																send_jsonObj);
												RedCodeProcessor.getInstance()
														.setOnControlListener(
																null);
											}
										});
					} else {
						// 数据库错误
						MainActivity.showString("数据库无数据", MultiTextBuffer.TYPE_OTHER);
						send_jsonObj.setResult(RE_ERROR);
						TcpConnectionManager.getInstance().sendJson(socketId,
								send_jsonObj);
					}

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
