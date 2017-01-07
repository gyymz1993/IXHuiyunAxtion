package com.huiyun.ixhuiyunaxtion.master.json.analyst;

import java.util.Map;

import android.os.SystemClock;

import com.huiyun.ixhuiyunaxtion.master.StaticValues;
import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;
import com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer;
import com.huiyun.ixhuiyunaxtion.master.bean.json.BaseJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.json.DataJsonObj;
import com.huiyun.ixhuiyunaxtion.master.json.JsonSendData;
import com.huiyun.ixhuiyunaxtion.master.net.NetworkSupport;
import com.huiyun.ixhuiyunaxtion.master.net.tcp.TCPClientConnect;
import com.huiyun.ixhuiyunaxtion.master.utils.SpUtils;


/**
 * @Description: 主机登陆
 * @date 2015-1-24 下午4:33:23 
 * @version V1.0   
 * @author busy
 */
public class MasterLoginBack_206 extends BaseAnalyst{
	private static final int RE_CODE_LOGIN = 206;
	
	@Override
	public void handleData(int socketId, BaseJsonObj jsonObj) {
		try {
			if (OBJ_MASTER.equals(jsonObj.getObj())
					&& RE_CODE_LOGIN == jsonObj.getCode()) {
				login(jsonObj, socketId);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			MainActivity.showString("请检查传入参数", MultiTextBuffer.TYPE_OTHER);
		}
	}
	
	 /**
	 *  Function:
	 *  @author busy 
	 *  2015-1-26 下午2:53:58
	 *  @param jsonObj
	 *  @param socketId
	 */
	private void login(BaseJsonObj jsonObj, int socketId) {
		if (jsonObj instanceof DataJsonObj) {
			DataJsonObj result_jsonObj = (DataJsonObj) jsonObj;
			try {
				if(result_jsonObj.getResult()!=RE_RESULT_SUCCESS){
					JsonSendData data = new JsonSendData();
					data.masterLogin(socketId);
				}else{
					Map<String, String> resultMap = result_jsonObj.getData();
					String token = resultMap.get("token");
					StaticValues.SERVER_TOKEN = token;
					String familyId = resultMap.get("familyId");
					SpUtils.save("familyId", familyId);
					
					// 发送信息给服务器
					new Thread(){
						public void run() {
							JsonSendData data = new JsonSendData();
							
							// 向服务器发送用户表
							SystemClock.sleep(1000 * 3);
							data.uploadUser(NetworkSupport.getSocketId(
									TCPClientConnect.getInstance().getSocket()));
							
							// 向服务器发送区域表
							SystemClock.sleep(1000 * 3);
							data.uploadAllArea(NetworkSupport.getSocketId(
									TCPClientConnect.getInstance().getSocket()));
							
							// 向服务器发送场景表
							SystemClock.sleep(1000 * 3);
							data.uploadAllScene(NetworkSupport.getSocketId(
									TCPClientConnect.getInstance().getSocket()));
							
							// 向服务器发送红外码表(新)
							SystemClock.sleep(1000 * 3);
							data.uploadAllNewRedcode(NetworkSupport.getSocketId(
									TCPClientConnect.getInstance().getSocket()));
							
							// 向服务器发送输出设备表
							SystemClock.sleep(1000 * 3);
							data.uploadAllDevice(NetworkSupport.getSocketId(
									TCPClientConnect.getInstance().getSocket()));
							
							//向服务器发送摄像头表
							SystemClock.sleep(1000*3);
							data.uploadCameraName(NetworkSupport.getSocketId(
									TCPClientConnect.getInstance().getSocket()));
						};
					}.start();
				}
				
			} catch (NullPointerException e) {
				e.printStackTrace();
				MainActivity.showString("请检查传入参数",MultiTextBuffer.TYPE_OTHER);
			}
		}
	}

}
