package com.huiyun.ixhuiyunaxtion.master.json.analyst;


import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;
import com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer;
import com.huiyun.ixhuiyunaxtion.master.bean.json.BaseJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.json.DataJsonObj;

/**
 * @Description: 
 * @date 2015-1-28 下午6:02:53 
 * @version V1.0   
 */
public class MasterLoadSceneBack_212 extends BaseAnalyst{
	private static final int RE_LOAD_SCENE= 212;
	
	@Override
	public void handleData(int socketId, BaseJsonObj jsonObj) {
		try {
			if (OBJ_MASTER.equals(jsonObj.getObj())
					&& RE_LOAD_SCENE == jsonObj.getCode()) {
				loadDevice(jsonObj, socketId);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			MainActivity.showString("请检查传入参数", MultiTextBuffer.TYPE_OTHER);
		}
		
	}

	
	 /**
	 *  Function:
	 *  @author busy 
	 *  2015-1-28 下午6:07:12
	 *  @param jsonObj
	 *  @param socketId
	 */
	private void loadDevice(BaseJsonObj jsonObj, int socketId) {
		if (jsonObj instanceof DataJsonObj) {
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
