package com.huiyun.ixhuiyunaxtion.master.json.analyst;

import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;
import com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer;
import com.huiyun.ixhuiyunaxtion.master.bean.json.BaseJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.json.DataJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.table.Device;
import com.huiyun.ixhuiyunaxtion.master.bean.table.RedRay;
import com.huiyun.ixhuiyunaxtion.master.cmd.redcode.RedCodeProcessor;
import com.huiyun.ixhuiyunaxtion.master.dao.DeviceDao;
import com.huiyun.ixhuiyunaxtion.master.dao.RedRayDao;
import com.huiyun.ixhuiyunaxtion.master.dao.impl.DeviceDaoImpl;
import com.huiyun.ixhuiyunaxtion.master.dao.impl.RedRayDaoImpl;
import com.huiyun.ixhuiyunaxtion.master.inner.OnResultListener;
import com.huiyun.ixhuiyunaxtion.master.json.JsonSendData;
import com.huiyun.ixhuiyunaxtion.master.net.NetworkSupport;
import com.huiyun.ixhuiyunaxtion.master.net.TokenManager;
import com.huiyun.ixhuiyunaxtion.master.net.tcp.TCPClientConnect;
import com.huiyun.ixhuiyunaxtion.master.net.tcp.TcpConnectionManager;
import com.huiyun.ixhuiyunaxtion.master.utils.UIUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * @Title: Contrl_Infrared_9.java
 * @Package com.huiyun.master.activity.analyst
 * @Description: 开启红外码学习通道
 * @author Yangshao
 * @date 2015年1月7日 下午4:15:13
 * @version V1.0
 */
public class StartRedRayResult_9 extends BaseAnalyst {

	// 注册
	private static final int CODE_INFARED = 9;
	private static final int RE_CODE_INFARED = 10;
	private RedRayDao rayDao = new RedRayDaoImpl(UIUtils.getContext());
	private DeviceDao deviceDao = new DeviceDaoImpl(UIUtils.getContext());

	@Override
	public void handleData(int socketId, BaseJsonObj jsonObj) {
		try {
			if (OBJ_MASTER.equals(jsonObj.getObj())
					&& CODE_INFARED == jsonObj.getCode()) {
				startInfraredStudy(jsonObj, socketId);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			MainActivity.showString("红外请检查传入参数", MultiTextBuffer.TYPE_OTHER);
		}
	}

	/**
	 * Function: 通知开启红外通道 返回
	 * 
	 * @author Yangshao 2015年1月7日 下午4:20:06
	 * @param jsonObj
	 * @param socketId
	 * 
	 */
	Map<String, String> sendMap = new HashMap<String, String>();

	DataJsonObj send_jsonObj = new DataJsonObj();
	int socketID;
	int currentLearn = 1;

	private void startInfraredStudy(BaseJsonObj jsonObj, final int socketId) {
		final DataJsonObj result_jsonObj = (DataJsonObj) jsonObj;
		send_jsonObj.setObj(OBJ_PHONE);
		send_jsonObj.setCode(RE_CODE_INFARED);
		final Map<String, String> resultMap = result_jsonObj.getData();
		socketID = socketId;
		if (TokenManager.judgetToken(resultMap.get("token").toString()) == null) {
			send_jsonObj.setResult(RE_TOKEN_ERROR);
			// token错误
			TcpConnectionManager.getInstance().sendJson(socketId, send_jsonObj);
		} else {
			final int btn_code = Integer.valueOf(resultMap.get("btn_code"));
			final String r_name = resultMap.get("r_name");
			final int pageType = Integer.valueOf(resultMap.get("pageType"));
			RedRay rayCode = new RedRay();
			rayCode.setR_name(r_name);
			rayCode.setBtn_code(btn_code);

			RedCodeProcessor.getInstance().setOnLearnListener(
					new OnResultListener<RedCodeProcessor.SimpleRedCodeObj>() {
						@Override
						public void onResult(boolean isSucceed,
								RedCodeProcessor.SimpleRedCodeObj obj) {
							if (isSucceed) {
								RedRay rayCode = new RedRay();
								rayCode.setBtn_code(btn_code);
								rayCode.setD_address(obj.addr);
								rayCode.setPageType(pageType);
								rayCode.setR_name(r_name);
								rayCode.setD_code(obj.number);
								if (rayDao.saveAndUptate(rayCode)) {
									send_jsonObj.setResult(RE_RESULT_SUCCESS);
									sendMap.put("btn_code", btn_code + "");
									sendMap.put("pageType", pageType + "");
									sendMap.put("r_name", r_name + "");
									send_jsonObj.setData(sendMap);
									MainActivity.showString("学习成功" + pageType
											+ r_name + "地址" + obj.addr + "编号"
											+ obj.number,
											MultiTextBuffer.TYPE_OTHER);
									// 向服务器发送红外码表(新)
									new Thread() {
										public void run() {
											JsonSendData data = new JsonSendData();
											data.uploadAllNewRedcode(NetworkSupport
													.getSocketId(TCPClientConnect
															.getInstance()
															.getSocket()));
										};
									}.start();
								} else {
									MainActivity.showString("数据库错误",
											MultiTextBuffer.TYPE_OTHER);
									send_jsonObj.setResult(RE_ERROR);
								}
							} else {
								MainActivity.showString("回调失败",
										MultiTextBuffer.TYPE_OTHER);
								send_jsonObj.setResult(RE_RESULT_FAIL);
							}
							TcpConnectionManager.getInstance().sendJson(
									socketId, send_jsonObj);
						}
					});
			int num = 1;
			// 查询是否是复用设备

			RedRay red = rayDao.queryDcode(rayCode);
			if (red != null) {
				MainActivity.showString("成功开启复用红外学习通道",
						MultiTextBuffer.TYPE_OTHER);
				RedCodeProcessor.getInstance().learn(red.getD_address(),
						red.getD_code());
			}
			// 查询所有红外设备地址
			List<Integer> adress = deviceDao
					.getAddressByType(Device.TYPE_RED_RAY);
			if (adress.size() != 0 && adress != null) {
				for (Integer addr : adress) {
					if (red != null && red.getD_address() == addr) {
						continue;
					}
					List<Integer> numbers = rayDao.queryNumber(addr);
					if (numbers != null && numbers.size() != 0) {
						num = getNumber(numbers);
					}
					if (num < 217) {
						// 开启红外学习通道
						MainActivity.showString("成功开启红外学习通道",
								MultiTextBuffer.TYPE_OTHER);
						RedCodeProcessor.getInstance().learn(addr, num);
					} else {
						MainActivity.showString("遥控编号已超过217",
								MultiTextBuffer.TYPE_OTHER);
						send_jsonObj.setResult(RE_RESULT_FAIL);
						TcpConnectionManager.getInstance().sendJson(socketId,
								send_jsonObj);
					}

				}
			}

		}
	}

	/**
	 * Function:生成一个未被使用的最小编号 如果按顺序都被使用返回一个最大可用值
	 * 
	 * @author Yangshao 2015年4月22日 下午4:15:28
	 * @param list
	 * @return
	 */
	public static int getNumber(List<Integer> list) {
		int j = 1;
		for (int i = 0; i < list.size(); i++) {
			if (!list.contains(j)) {
				return j;
			}
			j++;
		}
		return Collections.max(list) + 1;
	}
}
