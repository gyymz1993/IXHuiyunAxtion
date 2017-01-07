package com.huiyun.ixhuiyunaxtion.master.json.analyst;


import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;
import com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer;
import com.huiyun.ixhuiyunaxtion.master.bean.json.BaseJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.json.DataJsonObj;
import com.huiyun.ixhuiyunaxtion.master.inner.OnResultListener;

/**
 * @Description: 
 * @date 2015年2月12日 上午10:15:03
 * @version V1.0
 */
public class HeartbeatResult_222 extends BaseAnalyst {
	private final int RE_HEART = 222;
	// 回调
	OnResultListener<Object> listener;

	public OnResultListener<Object> getListener() {
		return listener;
	}

	public void setListener(OnResultListener<Object> listener) {
		this.listener = listener;
	}

	@Override
	public void handleData(int socketId, BaseJsonObj jsonObj) {
		try {
			if (OBJ_MASTER.equals(jsonObj.getObj())
					&& RE_HEART == jsonObj.getCode()) {
				heartBeat(jsonObj, socketId);
			}else{
				
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			MainActivity.showString("请检查传入参数", MultiTextBuffer.TYPE_OTHER);
		}

	}

	/**
	 * Function:
	 * 
	 * @author busy 2015-1-26 下午3:13:37
	 * @param jsonObj
	 * @param socketId
	 */
	private void heartBeat(BaseJsonObj jsonObj, int socketId) {
		if (jsonObj instanceof DataJsonObj) {
			DataJsonObj result_jsonObj = (DataJsonObj) jsonObj;
			try {
				if (result_jsonObj.getResult() == RE_RESULT_SUCCESS) {
					if (listener != null) {
						listener.onResult(true, null);
					}
				}
			} catch (NullPointerException e) {
				e.printStackTrace();
				MainActivity.showString("HeartbeatResult_222：请检查传入参数",
						MultiTextBuffer.TYPE_OTHER);
			}
		}
	}
}
