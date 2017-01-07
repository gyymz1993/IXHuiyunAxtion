 package com.huiyun.ixhuiyunaxtion.master.alarm;

 import android.app.AlarmManager;
 import android.app.PendingIntent;
 import android.content.Context;
 import android.content.Intent;

 import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;
 import com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer;
 import com.huiyun.ixhuiyunaxtion.master.bean.table.TimerTask;
 import com.huiyun.ixhuiyunaxtion.master.dao.TimerTaskDao;
 import com.huiyun.ixhuiyunaxtion.master.dao.impl.TimerTaskDaoImpl;

 import java.util.List;



public class AlarmHelper {
	private Context context;
	private AlarmManager mAlarmManager;
	private PendingIntent pIntent;
	private TimerTaskDao taskDao;
	private AlarmHelper alarmHelper = null;

	public AlarmHelper(Context c) {
		this.context = c;
		mAlarmManager = (AlarmManager) c
				.getSystemService(Context.ALARM_SERVICE);
		//gyymz1993@gmail.com
	}

	/**
	 * Function:关闭闹钟
	 * 
	 * @author Yangshao 2015年3月17日 上午10:43:13
	 * @param intent
	 */
	public void closeAlarm(Intent intent) {
		pIntent = PendingIntent.getBroadcast(context, 1, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		mAlarmManager.cancel(pIntent);
	}

	/**
	 * Function:周期闹钟
	 * 
	 * @author Yangshao 2015年3月17日 上午10:43:32
	 * @param intent
	 * @param time
	 */
	public void setAlarm(Intent intent) {
		int INTERVAL = 1000 * 60 * 60 * 24;// 24h
		// 用广播管理闹铃
		String date = intent.getAction();
		int isRepeat = Integer.valueOf(intent.getStringExtra("isRepeat"));
		Long time = Long.valueOf(date);
		pIntent = PendingIntent.getBroadcast(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		if (isRepeat == 1) {
			// 设置闹钟重复时间
			mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, INTERVAL,
					pIntent);
		} else {
			//单次任务
			mAlarmManager.set(AlarmManager.RTC_WAKEUP, time, pIntent);
		}

	}

	/**
	 * Function:得到一个闹钟需要的Intent
	 * 
	 * setAction判断唯一
	 * 
	 * @author Yangshao 2015年3月18日 下午4:51:35
	 * @param taskk
	 * @return
	 */
	public Intent getAlarmIntent(TimerTask taskk) {
		Intent intent = new Intent();
		intent.putExtra("phonecode", taskk.getPhoneCode() + "");
		intent.putExtra("state", taskk.getState() + "");
		intent.putExtra("date", taskk.getDate() + "");
		intent.putExtra("time", AlarmUtils.getStringToTime(taskk.getDate()));
		intent.putExtra("scene_name", taskk.getSceneName() + "");
		intent.putExtra("funcode", taskk.getFunCode() + "");
		intent.putExtra("isRepeat", taskk.getIsRepeat() + "");
		intent.setAction(taskk.getDate());
		MainActivity.showString(
				"设置执行时间" + AlarmUtils.getDateOfString(taskk.getDate()),
				MultiTextBuffer.TYPE_OTHER);
		intent.setClass(context, AlamrReceiver.class);
		return intent;
	}

	/**
	 * 重新设置内容
	 * 
	 * @param context
	 *            上下文
	 */
	public void setAlarmTimes(Context context) {
		try {
			taskDao = new TimerTaskDaoImpl(context);
			alarmHelper = new AlarmHelper(context);
			List<TimerTask> tasks = taskDao.queryAllTimerTask();
			MainActivity.showString("设置定时任务条数" + tasks.size(),
					MultiTextBuffer.TYPE_OTHER);
			if (tasks.size() != 0 && tasks != null) {
				String time;
				for (TimerTask task : tasks) {
					if (AlarmUtils.compareNowTime(task.getDate())
							&& task.getIsRepeat() == 1) {
						time = AlarmUtils.getTodayTime(task.getDate());
						MainActivity.showString(
								"设置今天循环" + AlarmUtils.getDateOfString(time)
										+ "场景" + task.getSceneName(),
								MultiTextBuffer.TYPE_OTHER);
					} else {
						time = AlarmUtils.getTomorrowTime(task.getDate());
						MainActivity.showString(
								"设置明天循环" + AlarmUtils.getDateOfString(time)
										+ "场景" + task.getSceneName(),
								MultiTextBuffer.TYPE_OTHER);

					}
					if (task.getIsRepeat() != 1) {
						if (!AlarmUtils.compareNowTime(task.getDate())) {
							System.out.println("设置今天不循环");
							time = AlarmUtils.getTodayTime(task.getDate());
						} else {
							taskDao.delteTimerTask(task.getTime());
						}
					}
					task.setDate(time);
					Intent intent = alarmHelper.getAlarmIntent(task);
					alarmHelper.setAlarm(intent);
				}
			}
		} catch (Exception e) {
		}
	}

}
