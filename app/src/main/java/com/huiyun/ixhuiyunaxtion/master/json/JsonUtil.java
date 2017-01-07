package com.huiyun.ixhuiyunaxtion.master.json;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;
import com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer;
import com.huiyun.ixhuiyunaxtion.master.bean.json.BaseJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.json.DataJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.json.Down2JsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.json.DownJsonObj;


/**
 * 解析json的工具类
 * 
 * @author lzy_torah
 * 
 */
public class JsonUtil {
	static Gson gson = new Gson();
	static List<Integer> uploadlist = new ArrayList<Integer>();
	static List<Integer> upload2list = new ArrayList<Integer>();
	
	static {
		uploadlist.add(25);
		uploadlist.add(35);
		uploadlist.add(19);
		uploadlist.add(39);
		uploadlist.add(61);
		upload2list.add(53);
	}

	public static BaseJsonObj analyzeBytes(byte[] bytes) {
		// 1.判断非空。
		if (bytes == null)
			return null;
		// 2.bytes转string，string转json对象,返回
		BaseJsonObj basejson = null;
		try {
			String jsonStr = new String(bytes, "utf-8");
			BaseJsonObj tempjson = gson.fromJson(jsonStr, BaseJsonObj.class);
			if(tempjson.code != 222){
				MainActivity.showString("接收" + jsonStr,
						MultiTextBuffer.TYPE_JSON);
			}
			if (uploadlist.contains(tempjson.code)) {
				basejson = gson.fromJson(jsonStr, DownJsonObj.class);
			} else if (upload2list.contains(tempjson.code)) {
				basejson = gson.fromJson(jsonStr, Down2JsonObj.class);
			} else {
				basejson = gson.fromJson(jsonStr, DataJsonObj.class);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return basejson;
	}

	/**
	 * 将对象转成字符串
	 * 
	 * @param jsonobj
	 * @return
	 */
	public static byte[] jsonTobytes(BaseJsonObj jsonobj) {
		byte[] bytes = null;
		String json = gson.toJson(jsonobj);
		if(jsonobj.code != 221){
			MainActivity.showString("发送" + json,MultiTextBuffer.TYPE_JSON);
		}
		try {
			if (json != null)
				bytes = json.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return bytes;
	}
	
	 /**
	  * 得到一个发给服务器的obj
	 */
	public static DataJsonObj getAJsonObjForMaster(){
		DataJsonObj jsonObj=new DataJsonObj();
		jsonObj.result=1;
		jsonObj.obj="server";
		return jsonObj;
	}
}
