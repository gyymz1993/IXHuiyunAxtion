package com.huiyun.ixhuiyunaxtion.master.json.analyst;

import com.huiyun.ixhuiyunaxtion.master.StaticValues;
import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;
import com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer;
import com.huiyun.ixhuiyunaxtion.master.bean.json.BaseJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.json.DataJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.table.SceneItem;
import com.huiyun.ixhuiyunaxtion.master.cmd.StateStorage;
import com.huiyun.ixhuiyunaxtion.master.dao.SceneDao;
import com.huiyun.ixhuiyunaxtion.master.dao.impl.SceneDaoImpl;
import com.huiyun.ixhuiyunaxtion.master.net.TokenManager;
import com.huiyun.ixhuiyunaxtion.master.net.tcp.TcpConnectionManager;
import com.huiyun.ixhuiyunaxtion.master.utils.UIUtils;

import java.util.List;
import java.util.Map;


/**
 * @Package com.huiyun.master.contrl
 * @Description: 场景控制
 * @author Yangshao
 * @date 2015年1月7日 下午1:26:10
 * @version V1.0
 */
public class ContrlSceneResult_55 extends BaseAnalyst {

	private static final int CODE_SCENE = 55;
	private static final int RE_CODE_SCENE = 56;
	private SceneDao sceneDao = new SceneDaoImpl(UIUtils.getContext());

	@Override
	public void handleData(int socketId, BaseJsonObj jsonObj) {
		try {
			if (OBJ_MASTER.equals(jsonObj.getObj())
					&& CODE_SCENE == jsonObj.getCode()) {
				startContrlScene(jsonObj, socketId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			MainActivity.showString("请检查传入参数", MultiTextBuffer.TYPE_OTHER);
		}
	}

	/**
	 * Function: 场景控制
	 * 
	 * @author Yangshao 2015年1月7日 下午1:39:26
	 * @param jsonObj
	 */
	private void startContrlScene(BaseJsonObj jsonObj, int socketId) {
		if (jsonObj instanceof DataJsonObj) {
			DataJsonObj result_jsonObj = (DataJsonObj) jsonObj;
			Map<String, String> resultMap = result_jsonObj.getData();

			DataJsonObj send_jsonObj = new DataJsonObj();
			send_jsonObj.setCode(RE_CODE_SCENE);
			send_jsonObj.setObj(OBJ_PHONE);
			try {
				if (TokenManager.judgetToken(resultMap.get("token")) == null
						&& (!resultMap.get("token").equals(
								StaticValues.SERVER_TOKEN))) {
					send_jsonObj.setResult(RE_TOKEN_ERROR);
				} else {
					String scene_name = resultMap.get("scene_name").toString();
					List<SceneItem> sceneItems = sceneDao
							.queryPhoneCodeforScene(scene_name);
					if (sceneItems != null) {
						for (SceneItem item : sceneItems) {
							StateStorage.getInstance().writeState(
									item.getPhone_code(), item.getState());
						}
					}
					send_jsonObj.setResult(RE_RESULT_FAIL);
				}
				send_jsonObj.data = resultMap;
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
