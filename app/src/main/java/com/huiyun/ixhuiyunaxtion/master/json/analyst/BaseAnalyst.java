package com.huiyun.ixhuiyunaxtion.master.json.analyst;


import com.huiyun.ixhuiyunaxtion.master.bean.json.BaseJsonObj;

public abstract class BaseAnalyst {
	/**
	 * 返回成功 失败
	 */
	public final int RE_RESULT_SUCCESS = 1;
	public final int RE_RESULT_FAIL = 2;
	/**
	 * 服务器处理错误，可能是手机传参错误
	 */
	public final int RE_ERROR = 12;

	public final int RE_TOKEN_ERROR = 11;
	// 是否主机处理 obj:目标
	public final String OBJ_MASTER = "master";
	public final String OBJ_PHONE = "phone";
	public final String OBJ_SERVER = "server";

	/**
	 * 处理数据
	 * 
	 * @param jsonObj
	 */
	public abstract void handleData(int socketId, BaseJsonObj jsonObj);
}
