package com.huiyun.ixhuiyunaxtion.master.json.analyst;

import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;
import com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer;
import com.huiyun.ixhuiyunaxtion.master.bean.json.BaseJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.json.DataJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.json.Down2JsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.table.SceneItem;
import com.huiyun.ixhuiyunaxtion.master.dao.SceneDao;
import com.huiyun.ixhuiyunaxtion.master.dao.impl.SceneDaoImpl;
import com.huiyun.ixhuiyunaxtion.master.json.JsonSendData;
import com.huiyun.ixhuiyunaxtion.master.net.NetworkSupport;
import com.huiyun.ixhuiyunaxtion.master.net.TokenManager;
import com.huiyun.ixhuiyunaxtion.master.net.tcp.TCPClientConnect;
import com.huiyun.ixhuiyunaxtion.master.net.tcp.TcpConnectionManager;
import com.huiyun.ixhuiyunaxtion.master.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @Description: 上传或删除场景
 * @author lzn
 * @date 2015-1-26 上午9:48:17
 * @version V1.0
 */
public class UploadSceneResult_53 extends BaseAnalyst {

	private static final int UPLOAD_SCENE = 53;
	private static final int RE_UPLOAD_SCENE = 54;

	@Override
	public void handleData(int socketId, BaseJsonObj jsonObj) {
		try {
			if (OBJ_MASTER.equals(jsonObj.getObj())
					&& UPLOAD_SCENE == jsonObj.getCode()) {
				uploadScene(jsonObj, socketId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			MainActivity.showString("请检查传入参数", MultiTextBuffer.TYPE_OTHER);
		}
	}

	private void uploadScene(BaseJsonObj jsonObj, int socketId) {
		if (jsonObj instanceof Down2JsonObj) {
			final Down2JsonObj down2JsonObj = (Down2JsonObj) jsonObj;
			Down2JsonObj.Data resultMap = down2JsonObj.getData();

			final DataJsonObj send_jsonObj = new DataJsonObj();
			send_jsonObj.setCode(RE_UPLOAD_SCENE);
			send_jsonObj.setObj(OBJ_PHONE);
			try {
				// 判断token是否为空
				if (TokenManager.judgetToken(resultMap.info.get("token")) == null) {
					// token error
					send_jsonObj.setResult(RE_TOKEN_ERROR);
				} else {
					Map<String, String> info = down2JsonObj.data.info;
					List<Map<String, String>> list = down2JsonObj.data.list;

					String sceneName = info.get("scene_name");
					int sceneIcon = Integer.valueOf(info.get("image_bg"));

					List<SceneItem> listScene = new ArrayList<SceneItem>();

					// 生成场景记录列表
					SceneItem item;
					int phoneCode, state;
					if (list != null) {
						for (Map<String, String> map : list) {
							phoneCode = Integer.valueOf(map.get("phoneCode"));
							state = Integer.valueOf(map.get("state"));
							item = new SceneItem(sceneName, phoneCode, state,
									sceneIcon);
							listScene.add(item);
						}
					}

					// 更新数据库
					SceneDao sdao = new SceneDaoImpl(UIUtils.getContext());
					if (sdao.updateScene(sceneName, listScene)) {
						// 成功
						send_jsonObj.setResult(RE_RESULT_SUCCESS);

						// 向服务器发送场景表
						new Thread() {
							public void run() {
								JsonSendData data = new JsonSendData();
								data.uploadAllScene(NetworkSupport
										.getSocketId(TCPClientConnect
												.getInstance().getSocket()));
							};
						}.start();
					} else {
						// 失败
						send_jsonObj.setResult(RE_RESULT_FAIL);
					}
					sdao.printAllScene();
				}

				TcpConnectionManager.getInstance().sendJson(socketId, send_jsonObj);
			} catch (NullPointerException e) {
				MainActivity.showString("请检查传入参数",MultiTextBuffer.TYPE_OTHER);
				// 参数不对
				send_jsonObj.setResult(RE_ERROR);
				TcpConnectionManager.getInstance().sendJson(socketId, send_jsonObj);
			} catch (Exception e) {
				MainActivity.showString("请检查传入参数",MultiTextBuffer.TYPE_OTHER);
				// 参数不对
				send_jsonObj.setResult(RE_ERROR);
				TcpConnectionManager.getInstance().sendJson(socketId, send_jsonObj);
			}
		}
	}

}
