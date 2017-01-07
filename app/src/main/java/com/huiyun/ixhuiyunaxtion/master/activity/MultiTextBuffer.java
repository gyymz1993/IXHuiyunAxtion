package com.huiyun.ixhuiyunaxtion.master.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.util.SparseArray;

/**   
 * @Description: 分类存储打印信息的容器
 * @date 2015-4-8 下午2:25:17 
 * @version V1.0   
 */
public class MultiTextBuffer {
	// 所有分类的类别
	public static final int TYPE_ALL        = 1;
	public static final int TYPE_SERIALPORT = 2;
	public static final int TYPE_JSON	    = 3;
	public static final int TYPE_DEVICE     = 4;
	public static final int TYPE_OTHER      = 5;
	
	// 总数
	public static final int TOTAL      = 5;
	
	private SparseArray<List<String>> allList;
	
	public MultiTextBuffer(){
		// 生成一个以字符串列表为元素的数组
		allList = new SparseArray<List<String>>();
		for(int i=1; i<=TOTAL; i++){
			allList.append(i, new ArrayList<String>());
		}
	}
	
	 /**
	 *  Function:把信息添加到指定类型的信息队列下
	 *  @param info
	 *  @param type
	 */
	public void add(String info, int type){
		// type值不能超过总数
		if(type > TOTAL || type < 1){
			return;
		}
		
		// 把新内容加到队列里
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		int second = c.get(Calendar.SECOND);
		if (info != null) {
			String show = hour + ":" + minute + ":" + second
					+ " -- " + info + "\n";
			allList.get(type).add(show);
		}
		
		// 从队列中移除旧内容
		if (allList.get(type).size() > 100) {
			allList.put(type, allList.get(type).subList(
					allList.get(type).size() - 50,
					allList.get(type).size() - 1));
		}
	}
	
	 /**
	 *  Function:根据类型获得最近的若干条打印信息文本
	 *  @param type
	 *  @return
	 */
	public String getText(int type){
		// type值不能超过总数
		if(type > TOTAL || type < 1){
			return null;
		}
		
		// 返回内容
		String show = "";
		for (int i = (allList.get(type).size() >= 50 ? allList.get(type).size() - 50
				: 0); i < allList.get(type).size(); i++) {
			show += allList.get(type).get(i);
		}
		return show;
	}
}
