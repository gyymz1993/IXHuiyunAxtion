package com.huiyun.ixhuiyunaxtion.master.bean.json;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 普通的json对象
 * @date 2015年1月10日 上午11:21:00
 * @version V1.0 格式如下 {obj:master,code:1,result:1,data:{
 *          token：xxx,user:xxx,psw:xxx }}
 */
public class DataJsonObj extends BaseJsonObj {
	public Map<String, String> data = new HashMap<String, String>();

	public Map<String, String> getData() {
		return data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "DataJsonObj [data=" + data + ", obj=" + obj + ", code=" + code
				+ ", result=" + result + "]";
	}

}
