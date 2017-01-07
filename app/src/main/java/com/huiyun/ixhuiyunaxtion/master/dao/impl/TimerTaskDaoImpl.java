package com.huiyun.ixhuiyunaxtion.master.dao.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;
import com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer;
import com.huiyun.ixhuiyunaxtion.master.bean.table.TimerTask;
import com.huiyun.ixhuiyunaxtion.master.dao.TimerTaskDao;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;

public class TimerTaskDaoImpl implements TimerTaskDao {
	/**
	 */
	private DbUtils db;

	public TimerTaskDaoImpl(Context context) {
		db = DbUtils.create(context);
		db.configAllowTransaction(true);
		db.configDebug(true);
	}

	@Override
	public boolean saveAndupdates(TimerTask task) {
		try {
			TimerTask rs = null;
			if (task.getFunCode() == 1) {
				rs = db.findFirst(Selector.from(TimerTask.class)
						.where("date", "=", task.getDate())
						.and("phoneCode", "=", task.getPhoneCode()));
			}
			if (task.getFunCode() == 2) {
				rs = db.findFirst(Selector.from(TimerTask.class)
						.where("date", "=", task.getDate())
						.and("sceneName", "=", task.getSceneName()));
			}
			if (rs == null) {
				db.save(task);
			} else {
				task.setId(rs.getId());
				db.update(task, new String[] { "funCode", "date", "phoneCode",
						"state", "sceneName", "isRepeat", "time" });
			}
			return true;
		} catch (DbException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean saveTimerTask(TimerTask task) {
		try {
			db.save(task);
		} catch (DbException e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}

	int count = 0;

	@Override
	public boolean delteTimerTask(String time) {
		try {
			MainActivity.showString("删除一条定时任务", MultiTextBuffer.TYPE_OTHER);
			db.delete(TimerTask.class, WhereBuilder.b("time", "=", time));
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public List<TimerTask> queryTimerTask(String date) {
		List<TimerTask> timers = new ArrayList<TimerTask>();
		try {
			timers = db.findAll(Selector.from(TimerTask.class).where("date",
					"=", date));
			for (TimerTask task : timers) {
				System.out.println(task.toString());
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
		return timers;
	}

	@Override
	public List<TimerTask> queryAllTimerTask() {
		List<TimerTask> timers = new ArrayList<TimerTask>();
		try {
			timers = db.findAll(TimerTask.class);
			if (timers != null && timers.size() != 0) {
				for (TimerTask task : timers) {
					System.out.println(task.toString());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return timers;
	}

	@Override
	public List<TimerTask> queryolnyTimerTask(String time) {
		List<TimerTask> timers = new ArrayList<TimerTask>();
		try {
			timers = db.findAll(Selector.from(TimerTask.class).where("time",
					"=", time));
			for (TimerTask task : timers) {
				System.out.println(task.toString());
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
		return timers;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.huiyun.master.dao.TimerTaskDao#deleteTimer(java.util.List)
	 */
	// @Override
	// public boolean deleteTimer(List<TimerTask> tasks) {
	// if (tasks != null && tasks.size() != 0) {
	// for (TimerTask task : tasks) {
	// try {
	// db.delete(task);
	// return true;
	// } catch (DbException e) {
	// e.printStackTrace();
	// }
	// }
	// }
	//
	// return false;
	// }

}