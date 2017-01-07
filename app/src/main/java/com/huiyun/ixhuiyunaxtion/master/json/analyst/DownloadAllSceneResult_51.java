package com.huiyun.ixhuiyunaxtion.master.json.analyst;

import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;
import com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer;
import com.huiyun.ixhuiyunaxtion.master.bean.json.BaseJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.json.DataJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.json.DownJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.table.SceneItem;
import com.huiyun.ixhuiyunaxtion.master.dao.SceneDao;
import com.huiyun.ixhuiyunaxtion.master.dao.impl.SceneDaoImpl;
import com.huiyun.ixhuiyunaxtion.master.net.TokenManager;
import com.huiyun.ixhuiyunaxtion.master.net.tcp.TcpConnectionManager;
import com.huiyun.ixhuiyunaxtion.master.utils.UIUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer.*;


/**
 * @Description: 所有场景下载
 * @date 2015年1月10日 下午5:43:06
 * @version V1.0
 */
public class DownloadAllSceneResult_51 extends BaseAnalyst {

	private final int DOWN_EAEW = 51;
	private final int RE_DOWN_EAEW = 52;
	private SceneDao deviceDao = new SceneDaoImpl(UIUtils.getContext());

	@Override
	public void handleData(int socketId, BaseJsonObj jsonObj) {
		try {
			if (OBJ_MASTER.equals(jsonObj.getObj())
					&& DOWN_EAEW == jsonObj.getCode()) {
				startDownDevices(socketId, jsonObj);
			}
		} catch (Exception e) {
			e.printStackTrace();
			MainActivity.showString("请检查传入参数", TYPE_OTHER);
		}
		
	}

	/**
	 * Function:
	 * 
	 * @author Yangshao 2015年1月10日 上午9:53:18
	 * @param jsonObj
	 * @param socketId
	 */
	private void startDownDevices(int socketId, BaseJsonObj jsonObj) {
		if (jsonObj instanceof DataJsonObj) {
			DataJsonObj result_jsonObj = (DataJsonObj) jsonObj;
			Map<String, String> data=result_jsonObj.getData();
			DownJsonObj send_jsonObj = new DownJsonObj();
			DownJsonObj.Data send_Data=new DownJsonObj.Data();
			send_jsonObj.setCode(RE_DOWN_EAEW);
			send_jsonObj.setObj(OBJ_PHONE);
			MainActivity.showString("下载场景", TYPE_OTHER);
			try {
				if (TokenManager.judgetToken(data.get("token")) == null) {
					send_jsonObj.setResult(RE_TOKEN_ERROR);
				} else {
					List<SceneItem> scnens = deviceDao.getSceneName();
					if (scnens != null && scnens.size() > 0) {
						for (SceneItem d : scnens) {
							Map<String, String> maps = new HashMap<String, String>();
							maps.put("scene_name", d.getScene_name());
							maps.put("image_bg", d.getImage_bg()+"");
							send_Data.list.add(maps);
						}
						send_jsonObj.setData(send_Data);
						send_jsonObj.setResult(RE_RESULT_SUCCESS);
					} else {
						MainActivity.showString("场景数据库为空", TYPE_OTHER);
						send_jsonObj.setResult(RE_RESULT_FAIL);
					}
					TcpConnectionManager.getInstance().sendJson(socketId, send_jsonObj);
				}
			} catch (Exception e) {
				e.printStackTrace();
				MainActivity.showString("请检查传入参数", TYPE_OTHER);
				send_jsonObj.setResult(RE_ERROR);
				TcpConnectionManager.getInstance().sendJson(socketId, send_jsonObj);
			}
		}
	}
}
