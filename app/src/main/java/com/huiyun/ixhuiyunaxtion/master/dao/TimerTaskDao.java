package com.huiyun.ixhuiyunaxtion.master.dao;

import com.huiyun.ixhuiyunaxtion.master.bean.table.TimerTask;

import java.util.List;


public interface TimerTaskDao {
	/**
	 * @return 执行结果是否成功
	 */
	boolean saveTimerTask(TimerTask list);

	boolean saveAndupdates(TimerTask task);

	/**
	 * @author Yangshao 2015�?�?0�?下午5:26:31
	 * @return
	 */
	boolean delteTimerTask(String time);

	/**
	 * @author Yangshao 2015�?�?0�?下午5:26:31
	 * @return
	 */
	List<TimerTask> queryTimerTask(String date);

	List<TimerTask> queryolnyTimerTask(String date);

	/**
	 * @author Yangshao 2015�?�?0�?下午5:26:31
	 * @return
	 */
	List<TimerTask> queryAllTimerTask();

	//boolean deleteTimer(List<TimerTask> tasks);

}
