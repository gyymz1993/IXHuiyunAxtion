package com.huiyun.ixhuiyunaxtion.master.bean.json;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description:下载设备的JSON
 * @date 2015年1月10日 上午9:55:16
 * @version V1.0 格式如下 data内容：{obj:master,code:1,result:1,data:{
 *          token：xxx,user:xxx,psw:xxx list:[ {name:xxx,pwd:xxx} ... ]}
 */
public class DownJsonObj extends BaseJsonObj {

	public Data data;

	public static class Data {
		public List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		public String token;

		public List<Map<String, String>> getList() {
			return list;
		}

		public void setList(List<Map<String, String>> list) {
			this.list = list;
		}

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

}