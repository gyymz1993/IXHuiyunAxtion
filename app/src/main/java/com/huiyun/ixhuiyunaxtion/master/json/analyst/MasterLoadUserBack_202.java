package com.huiyun.ixhuiyunaxtion.master.json.analyst;


import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;
import com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer;
import com.huiyun.ixhuiyunaxtion.master.bean.json.BaseJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.json.DataJsonObj;

/**
 * @Description: 主机上传所有用户信息
 * @date 2015-1-26 下午3:11:30 
 * @version V1.0   
 */
public class MasterLoadUserBack_202 extends BaseAnalyst{
	private static final int RE_LOAD_USER= 202;

	@Override
	public void handleData(int socketId, BaseJsonObj jsonObj) {
		try {
			if (OBJ_MASTER.equals(jsonObj.getObj())
					&& RE_LOAD_USER == jsonObj.getCode()) {
				loadUser(jsonObj, socketId);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			MainActivity.showString("请检查传入参数",MultiTextBuffer.TYPE_OTHER);
		}
		
	}

	
	 /**
	 *  Function:
	 *  @author busy 
	 *  2015-1-26 下午3:13:37
	 *  @param jsonObj
	 *  @param socketId
	 */
	private void loadUser(BaseJsonObj jsonObj, int socketId) {
		if (jsonObj instanceof DataJsonObj) {
			DataJsonObj result_jsonObj = (DataJsonObj) jsonObj;
			try {
				if(result_jsonObj.getResult()!=RE_RESULT_SUCCESS){
//					JsonSendData data = new JsonSendData();
//					data.uploadUser(socketId );
				}
				
			} catch (NullPointerException e) {
				e.printStackTrace();
				MainActivity.showString("请检查传入参数", MultiTextBuffer.TYPE_OTHER);
//				TcpPhoneUtil.getInstance().sendJson(socketId, result_jsonObj);
			}
		}
	}

}
