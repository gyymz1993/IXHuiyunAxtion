package com.huiyun.ixhuiyunaxtion.master.bean.json;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description:下载场景的JSON
 * @date 2015年1月10日 上午9:55:16
 * @version V1.0
 * 
 * @version data内容：{"info": { "scene_name": xxx, "token": xxx, "image_bg": int
 *          },list:[ {phoneCode:int,state:int}, {phoneCode:int,state:int} ... ]}
 */
public class Down2JsonObj extends BaseJsonObj {

	public Data data;

	public static class Data {
		public Map<String, String> info;
		public List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		public Map<String, String> getInfo() {
			return info;
		}

		public void setInfo(Map<String, String> info) {
			this.info = info;
		}

		public List<Map<String, String>> getList() {
			return list;
		}

		public void setList(List<Map<String, String>> list) {
			this.list = list;
		}
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}
}