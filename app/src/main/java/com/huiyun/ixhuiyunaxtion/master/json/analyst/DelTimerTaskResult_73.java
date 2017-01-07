package com.huiyun.ixhuiyunaxtion.master.json.analyst;

import java.util.Map;

import android.content.Intent;

import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;
import com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer;
import com.huiyun.ixhuiyunaxtion.master.alarm.AlarmHelper;
import com.huiyun.ixhuiyunaxtion.master.alarm.AlarmUtils;
import com.huiyun.ixhuiyunaxtion.master.bean.json.BaseJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.json.DataJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.table.TimerTask;
import com.huiyun.ixhuiyunaxtion.master.dao.TimerTaskDao;
import com.huiyun.ixhuiyunaxtion.master.dao.impl.TimerTaskDaoImpl;
import com.huiyun.ixhuiyunaxtion.master.net.TokenManager;
import com.huiyun.ixhuiyunaxtion.master.net.tcp.TcpConnectionManager;
import com.huiyun.ixhuiyunaxtion.master.utils.UIUtils;


/**
 * @Description: 执行定时任务
 * @date 2015年3月9日 下午5:34:54
 * @version V1.0
 */
public class DelTimerTaskResult_73 extends BaseAnalyst {

	private final int TIME_TASL = 73;
	private final int RE_TIME_TASL = 74;
	private TimerTaskDao dao = new TimerTaskDaoImpl(UIUtils.getContext());
	private AlarmHelper mAlarmHelper = new AlarmHelper(UIUtils.getContext());

	@Override
	public void handleData(int socketId, BaseJsonObj jsonObj) {
		try {
			if (OBJ_MASTER.equals(jsonObj.getObj())
					&& TIME_TASL == jsonObj.getCode()) {
				timeStack(socketId, jsonObj);
			}
		} catch (Exception e) {
			e.printStackTrace();
			MainActivity.showString("请检查传入参数",MultiTextBuffer.TYPE_OTHER);
		}

	}

	/**
	 * Function:定时任务
	 * 
	 * @author Yangshao 2015年1月10日 上午9:53:18
	 * @param jsonObj
	 * @param socketId
	 */
	private void timeStack(int socketId, BaseJsonObj jsonObj) {
		if (jsonObj instanceof DataJsonObj) {
			DataJsonObj result_jsonObj = (DataJsonObj) jsonObj;
			Map<String, String> data = result_jsonObj.getData();
			DataJsonObj sendjsonObj = new DataJsonObj();
			sendjsonObj.setCode(RE_TIME_TASL);
			sendjsonObj.setObj(OBJ_PHONE);
			try {

				if (TokenManager.judgetToken(data.get("token")) == null) {
					sendjsonObj.setResult(RE_TOKEN_ERROR);
				} else {
					TimerTask task = new TimerTask();
					String sceneName = data.get("scene_name");
					String date = data.get("date");
					int isRepeat = Integer.valueOf(data.get("isRepeat"));
					task.setDate(date);
					task.setIsRepeat(isRepeat);
					task.setSceneName(sceneName);
					String time = AlarmUtils.getStringToTime(data.get("date"));
					if (dao.delteTimerTask(time)) {
						Intent intent = mAlarmHelper.getAlarmIntent(task);
						mAlarmHelper.closeAlarm(intent);
						sendjsonObj.setResult(RE_RESULT_SUCCESS);
					} else {
						sendjsonObj.setResult(RE_RESULT_FAIL);
					}

				}
				TcpConnectionManager.getInstance().sendJson(socketId,
						sendjsonObj);
			} catch (Exception e) {
				e.printStackTrace();
				MainActivity.showString("请检查传入参数", MultiTextBuffer.TYPE_OTHER);
				sendjsonObj.setResult(RE_ERROR);
				TcpConnectionManager.getInstance().sendJson(socketId,
						sendjsonObj);
			}
		}
	}
}
