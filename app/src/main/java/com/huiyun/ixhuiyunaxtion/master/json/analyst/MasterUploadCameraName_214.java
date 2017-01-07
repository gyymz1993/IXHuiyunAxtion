package com.huiyun.ixhuiyunaxtion.master.json.analyst;

import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;
import com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer;
import com.huiyun.ixhuiyunaxtion.master.bean.json.BaseJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.json.DataJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.json.DownJsonObj;

/**
 * @Description: TODO(上传摄像头名字返回结果) 
 * @date 2015-3-9 上午11:06:51 
 * @version V1.0   
 */
class MasterUploadCameraName_214 extends BaseAnalyst{
	private final int RE_CAMERA_NAME = 214;
	
	@Override
	public void handleData(int socketId, BaseJsonObj jsonObj) {
		try {
			if (OBJ_MASTER.equals(jsonObj.getObj())
					&& RE_CAMERA_NAME == jsonObj.getCode()) {
				uploadCamereName(socketId, jsonObj);
			}
		} catch (Exception e) {
			e.printStackTrace();
			MainActivity.showString("请检查传入参数", MultiTextBuffer.TYPE_OTHER);
		}
	}

	
	 /**
	 *  Function:
	 *  @author busy 
	 *  2015-3-9 上午11:14:23
	 *  @param socketId
	 *  @param jsonObj
	 */
	private void uploadCamereName(int socketId, BaseJsonObj jsonObj) {
		if (jsonObj instanceof DownJsonObj) {
			DataJsonObj result_jsonObj = (DataJsonObj) jsonObj;
			try {
				if(result_jsonObj.getResult()!=RE_RESULT_SUCCESS){
					
				}
				
			} catch (NullPointerException e) {
				e.printStackTrace();
				MainActivity.showString("请检查传入参数",MultiTextBuffer.TYPE_OTHER);
//				TcpPhoneUtil.getInstance().sendJson(socketId, result_jsonObj);
			}
		}
	}

}
