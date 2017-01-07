package com.huiyun.ixhuiyunaxtion.master.alarm;

import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;
import com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer;
import com.huiyun.ixhuiyunaxtion.master.bean.table.SceneItem;
import com.huiyun.ixhuiyunaxtion.master.bean.table.TimerTask;
import com.huiyun.ixhuiyunaxtion.master.cmd.StateStorage;
import com.huiyun.ixhuiyunaxtion.master.dao.SceneDao;
import com.huiyun.ixhuiyunaxtion.master.dao.TimerTaskDao;
import com.huiyun.ixhuiyunaxtion.master.dao.impl.SceneDaoImpl;
import com.huiyun.ixhuiyunaxtion.master.dao.impl.TimerTaskDaoImpl;
import com.huiyun.ixhuiyunaxtion.master.utils.UIUtils;

/**
 * @Description: 接收到响铃广播后操作数据
 * @date 2015年3月18日 下午4:44:45
 * @version V1.0
 */
public class AlarmService extends Service {
	private SceneDao sceneDao = new SceneDaoImpl(UIUtils.getContext());
	private TimerTaskDao taskDao = new TimerTaskDaoImpl(UIUtils.getContext());

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onStart(final Intent intent, int startId) {
		super.onStart(intent, startId);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String date = intent.getAction();
					String time = AlarmUtils.getStringToTime(date);
					List<TimerTask> tasks = taskDao.queryolnyTimerTask(time);
					if (tasks != null && tasks.size() != 0) {
						for (TimerTask task : tasks) {
							MainActivity.showString("开始执行任务时间" + time + "场景"
									+ task.getSceneName(),
									MultiTextBuffer.TYPE_OTHER);
							alarmAlertSwitch(task);
						}
					}
				} catch (Exception e) {
				}
			}
		}).start();

	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/**
	 * 
	 * @author Yangshao 2015年3月10日 下午3:59:38，
	 * @param task
	 */
	private void alarmAlertSwitch(TimerTask task) {
		if (task.getFunCode() == 1) {
			StateStorage.getInstance().writeState(
					Integer.valueOf(task.getPhoneCode()),
					Integer.valueOf(task.getState()));
		}
		if (task.getFunCode() == 2) {
			List<SceneItem> sceneItems = sceneDao.queryPhoneCodeforScene(task
					.getSceneName());
			// 不是循环任务
			if (sceneItems != null) {
				for (SceneItem item : sceneItems) {
					StateStorage.getInstance().writeState(item.getPhone_code(),
							item.getState());
				}
			}
			//删除不重复的过时任务
			if (task.getIsRepeat() != 1
					&& !AlarmUtils.differSetTimeAndNowTime(Long.valueOf(task
							.getDate()))) {
				taskDao.delteTimerTask(task.getTime());
			}
		}

	}

}
