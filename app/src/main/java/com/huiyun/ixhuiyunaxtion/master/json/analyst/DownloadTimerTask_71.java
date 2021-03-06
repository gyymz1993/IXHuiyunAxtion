package com.huiyun.ixhuiyunaxtion.master.json.analyst;

import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;
import com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer;
import com.huiyun.ixhuiyunaxtion.master.bean.json.BaseJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.json.DataJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.json.Down2JsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.json.DownJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.table.TimerTask;
import com.huiyun.ixhuiyunaxtion.master.dao.TimerTaskDao;
import com.huiyun.ixhuiyunaxtion.master.dao.impl.TimerTaskDaoImpl;
import com.huiyun.ixhuiyunaxtion.master.net.TokenManager;
import com.huiyun.ixhuiyunaxtion.master.net.tcp.TcpConnectionManager;
import com.huiyun.ixhuiyunaxtion.master.utils.UIUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 所有设备下载
 * @date 2015年1月10日 下午5:43:06
 * @version V1.0
 */
public class DownloadTimerTask_71 extends BaseAnalyst {

	private final int DOWN_TIMER = 71;
	private final int RE_DOWN_TIMER = 72;
	private TimerTaskDao taskDao = new TimerTaskDaoImpl(UIUtils.getContext());

	@Override
	public void handleData(int socketId, BaseJsonObj jsonObj) {

		try {
			if (OBJ_MASTER.equals(jsonObj.getObj())
					&& DOWN_TIMER == jsonObj.getCode()) {
				startDownTimerTask(socketId, jsonObj);
			}
		} catch (Exception e) {
			e.printStackTrace();
			MainActivity.showString("请检查传入参数", MultiTextBuffer.TYPE_OTHER);
		}
	}

	/**
	 * Function:
	 * 
	 * @author Yangshao 2015年1月10日 上午9:53:18
	 * @param jsonObj
	 * @param socketId
	 */
	private void startDownTimerTask(int socketId, BaseJsonObj jsonObj) {
		if (jsonObj instanceof DataJsonObj) {
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			DownJsonObj.Data sendData = new DownJsonObj.Data();

			DataJsonObj result_jsonObj = (DataJsonObj) jsonObj;
			Map<String, String> resultMap = result_jsonObj.getData();

			DownJsonObj send_jsonObj = new DownJsonObj();
			send_jsonObj.setCode(RE_DOWN_TIMER);
			send_jsonObj.setObj(OBJ_PHONE);
			try {
				if (TokenManager.judgetToken(resultMap.get("token").toString()) == null) {
					send_jsonObj.setResult(RE_TOKEN_ERROR);
				} else {
					List<TimerTask> timerTasks = taskDao.queryAllTimerTask();
					if (timerTasks != null && timerTasks.size() > 0) {
						for (TimerTask d : timerTasks) {
							Map<String, String> maps = new HashMap<String, String>();
							maps.put("scene_name", d.getSceneName());
							maps.put("isRepeat", d.getIsRepeat() + "");
							maps.put("funcode", d.getFunCode() + "");
							maps.put("date", d.getDate());
							list.add(maps);
						}
						sendData.list = list;
						send_jsonObj.setData(sendData);
						send_jsonObj.setResult(RE_RESULT_SUCCESS);
					} else {
						MainActivity.showString("TimerTask数据库为空",MultiTextBuffer.TYPE_OTHER);
						send_jsonObj.setResult(RE_RESULT_FAIL);
					}
					TcpConnectionManager.getInstance().sendJson(socketId, send_jsonObj);
				}
			} catch (Exception e) {
				e.printStackTrace();
				MainActivity.showString("请检查下载定时任务参数",MultiTextBuffer.TYPE_OTHER);
				send_jsonObj.setResult(RE_ERROR);
				TcpConnectionManager.getInstance().sendJson(socketId, send_jsonObj);
			}
		}
	}

}
