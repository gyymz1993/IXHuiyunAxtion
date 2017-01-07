package com.huiyun.ixhuiyunaxtion.master.utils;

import android.content.SharedPreferences;

/**
 * @Description: 保存一些持久化数据
 * @date 2015年1月27日 下午3:37:42
 * @version V1.0
 */
public class SpUtils {
	
	private static SharedPreferences sharedPreferences=UIUtils.getContext().getSharedPreferences(
			"config", 0);
	/**
	 * 
	 *  Function:保存键值到SP文件
	 *  @author Yangshao 
	 *  2015年1月27日 下午4:00:34
	 *  @param key
	 *  @param values
	 */
	public static void save(String key, String values) {
		sharedPreferences.edit().putString(key, values).commit();
	}
	/**保存boolean键值到sp文件
	 * @param key
	 * @param value
	 */
	public static void save(String key, boolean value) {
		sharedPreferences.edit().putBoolean(key, value).commit();
	}

	 /**
	 *  Function: 根据名称取值
	 *  @author Yangshao 
	 *  2015年1月27日 下午4:01:09
	 *  @param key
	 *  @return
	 */
	public static String getValues(String key) {
		 SharedPreferences userInfo = UIUtils.getContext().getSharedPreferences("config", 0);  
	        String values = userInfo.getString(key, "");
			return values;  
	}
}
