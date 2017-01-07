package com.huiyun.ixhuiyunaxtion.master.bean.table;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

@Table(name = "task_main")
public class TimerTask extends EntityBase {
	// 功能码
	@Column(column = "funCode")
	public int funCode;
	@Column(column = "date")
	public String date;
	@Column(column = "phoneCode")
	private int phoneCode;// 瀵瑰簲鐨勬墜鏈烘寜閿爜锛屽彧鏈夎緭鍑鸿澶囨湁
	@Column(column = "state")
	private int state;//
	@Column(column = "sceneName")
	private String sceneName;//

	@Column(column = "time")
	private String time;//

	// 是否是重复任务 1是重复任务 2不是重复任务
	@Column(column = "isRepeat")
	private int isRepeat;//

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getPhoneCode() {
		return phoneCode;
	}

	public void setPhoneCode(int phoneCode) {
		this.phoneCode = phoneCode;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	/**
	 * @param date
	 * @param time
	 * @param phoneCode
	 * @param state
	 */
	public TimerTask(String date, int phoneCode, int state) {
		super();
		this.date = date;
		this.phoneCode = phoneCode;
		this.state = state;
	}

	public int getFunCode() {
		return funCode;
	}

	public void setFunCode(int funCode) {
		this.funCode = funCode;
	}

	/**
	 * @param fun_code
	 * @param date
	 * @param phoneCode
	 * @param state
	 * @param sceneName
	 */
	public TimerTask(int funcode, String date, int phoneCode, int state,
			String sceneName) {
		super();
		this.funCode = funcode;
		this.date = date;
		this.phoneCode = phoneCode;
		this.state = state;
		this.sceneName = sceneName;
	}

	/**
	 * @param funCode
	 * @param date
	 * @param phoneCode
	 * @param state
	 * @param sceneName
	 * @param isRepeat
	 */
	public TimerTask(int funCode, String date, int phoneCode, int state,
			String sceneName, int isRepeat) {
		super();
		this.funCode = funCode;
		this.date = date;
		this.phoneCode = phoneCode;
		this.state = state;
		this.sceneName = sceneName;
		this.isRepeat = isRepeat;
	}

	public String getSceneName() {
		return sceneName;
	}

	public void setSceneName(String sceneName) {
		this.sceneName = sceneName;
	}

	/**
	 */
	public TimerTask() {
		super();
	}

	@Override
	public String toString() {
		return "TimerTask [funCode=" + funCode + ", date=" + date
				+ ", phoneCode=" + phoneCode + ", state=" + state
				+ ", sceneName=" + sceneName + ", isRepeat=" + isRepeat + "]";
	}

	/**
	 * @param funCode
	 * @param date
	 * @param sceneName
	 */
	public TimerTask(int funCode, String date, String sceneName) {
		super();
		this.funCode = funCode;
		this.date = date;
		this.sceneName = sceneName;
	}

	public int getIsRepeat() {
		return isRepeat;
	}

	public void setIsRepeat(int isRepeat) {
		this.isRepeat = isRepeat;
	}

}
