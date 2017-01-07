package com.huiyun.ixhuiyunaxtion.master.cmd.container;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.util.Log;

/**
 * 每当接收到一个开关写入命令时，添加一个IOCtrlQueue对象；
 * 直到接收到此开关写入的返回命令，删除该IOCtrlQueue对象
 */
public class IOCtrlQueue {
	// TAG
	public static final String TAG = "InputQueue";
	
	private class IOCtrlObject{
		public int inputAddr;
		public int inputObj;
		public int outputAddr;
		public int outputObj;
		public int val;
	}
	
	private List<IOCtrlObject> list;
	
	public IOCtrlQueue(){
		list = new ArrayList<IOCtrlObject>();
	}
	
	
	 /**
	 *  Function:加入到队列
	 *  @param inputAddr
	 *  @param inputObj
	 *  @param outputAddr
	 *  @param outputObj
	 *  @param val
	 */
	public void add(int inputAddr, int inputObj, int outputAddr, int outputObj, int val){
		IOCtrlObject obj = new IOCtrlObject();
		obj.inputAddr = inputAddr;
		obj.inputObj = inputObj;
		obj.outputAddr = outputAddr;
		obj.outputObj = outputObj;
		obj.val = val;
		list.add(obj);
	}
	
	
	 /**
	 *  Function: 从队列中移除
	 *  @param inputAddr
	 *  @param inputObj
	 *  @param outputAddr
	 *  @param outputObj
	 *  @param val
	 */
	public void delete(int inputAddr, int inputObj, int outputAddr, int outputObj, int val){
		for(Iterator<IOCtrlObject> it = list.iterator();it.hasNext();){
			IOCtrlObject obj = it.next();
			if(obj.inputAddr == inputAddr && obj.inputObj == inputObj &&
				obj.outputAddr == outputAddr && obj.outputObj == outputObj && obj.val == val)
				it.remove();
		}
	}
	
	
	 /**
	 *  Function: 查看是否包含在队列里
	 *  @param inputAddr
	 *  @param inputObj
	 *  @param outputAddr
	 *  @param outputObj
	 *  @param val
	 *  @return
	 */
	public boolean contain(int inputAddr, int inputObj, int outputAddr, int outputObj, int val){
		for(IOCtrlObject obj:list){
			if(obj.inputAddr == inputAddr && obj.inputObj == inputObj &&
					obj.outputAddr == outputAddr && obj.outputObj == outputObj && obj.val == val)
				return true;
		}
		return false;
	}
	
	
	 /**
	 *  Function:获得开关值
	 *  @param inputAddr
	 *  @param inputObj
	 *  @param outputAddr
	 *  @param outputObj
	 *  @return
	 */
	public int getVal(int inputAddr, int inputObj, int outputAddr, int outputObj){
		for(IOCtrlObject obj:list){
			if(obj.inputAddr == inputAddr && obj.inputObj == inputObj &&
					obj.outputAddr == outputAddr && obj.outputObj == outputObj){
				return obj.val;
			}
		}
			
		return -1;
	}
	
	
	public void showLogInfo(){
		Log.i(TAG, "队列共有" + list.size() + "行");
		for(int i=0; i<list.size(); i++){
			String logInfo =  "输入地址：" + list.get(i).inputAddr + ",输入编号：" + list.get(i).inputObj +
					"输出地址：" + list.get(i).outputAddr + ",输出编号：" + list.get(i).outputObj +
					",开关量：" + list.get(i).val;
			Log.i("第" + i + "行", logInfo);
		}
	}
}
