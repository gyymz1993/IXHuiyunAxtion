package com.huiyun.ixhuiyunaxtion.master.net;

import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;
import com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer;
import com.huiyun.ixhuiyunaxtion.master.bean.table.Device;
import com.huiyun.ixhuiyunaxtion.master.cmd.command.Data;
import com.huiyun.ixhuiyunaxtion.master.dao.database.RelationBean;
import com.huiyun.ixhuiyunaxtion.master.dao.database.RelationDao;
import com.huiyun.ixhuiyunaxtion.master.utils.UIUtils;

import java.util.List;


/**   
 * @Description: 负责将输入设备与输出设备在关联表中连接
 * @author lzn
 * @date 2015-1-15 下午4:48:36 
 * @version V1.0   
 */
public class RelationLinker {
	
	 /**
	 *  Function: 将指定的输入设备和指定的输出设备在关联表中连接
	 *  需要注意的是，如果指定的输入设备原本在关联表中有连接时，断开原本的连接
	 *  @author lzn 
	 *  2015-1-15 下午4:50:30
	 *  @param input
	 *  @param output
	 */
	public static void linkRelation(Device input, Device output){
		// 断开旧有的连接
		delRelation(input,true);
		
		// 增加新的连接
		RelationDao rdao = new RelationDao(UIUtils.getContext());
		RelationBean rb = new RelationBean();
		rb.setInputAddr(input.getAddress());
		rb.setInputObj(input.getNumber());
		rb.setOutputAddr(output.getAddress());
		rb.setOutputObj(output.getNumber());
		rb.setInputType(RelationBean.IN_TYPE_NORMAL);
		rb.setOutputType(RelationBean.OUT_TYPE_NORMAL);
		rdao.add(rb);
		MainActivity.showString("Relation数据库更新记录：" + rb.toString(),
				MultiTextBuffer.TYPE_DEVICE);
		
		rdao.close();
	}
	
	 /**
	 *  Function: 将指定的输入设备和指定的场景在关联表中连接
	 *  @author lzn 
	 *  2015-3-25 下午3:43:23
	 *  @param input
	 *  @param sceneName
	 */
	public static void linkSceneRelation(Device input, String sceneName){
		// 断开旧有的连接
		delRelation(input,true);
		
		// 增加新的连接
		RelationDao rdao = new RelationDao(UIUtils.getContext());
		RelationBean rb = new RelationBean();
		rb.setInputAddr(input.getAddress());
		rb.setInputObj(input.getNumber());
		rb.setOutputAddr(0);
		rb.setOutputObj(0);
		rb.setInputType(RelationBean.IN_TYPE_NORMAL);
		rb.setOutputType(RelationBean.OUT_TYPE_SCENE);
		rb.setSceneName(sceneName);
		rdao.add(rb);
		MainActivity.showString("Relation数据库更新记录：" + rb.toString(),
				MultiTextBuffer.TYPE_DEVICE);
		
		rdao.close();
	}
	
	 /**
	 *  Function: 将指定的手机按键码和指定的输出设备在关联表中连接
	 *  需要注意的是，如果指定的手机按键码原本在关联表中有连接时，断开原本的连接
	 *  @author lzn 
	 *  2015-1-15 下午4:51:01
	 *  @param phoneCode
	 *  @param output
	 */
	public static void linkRelation(int phoneCode, Device output){
		Device input = new Device();
		input.setAddress(Data.ADDR_WIFI);
		input.setNumber(phoneCode);
		input.setType(4);
		linkRelation(input, output);
	}
	
	 /**
	 *  Function:删除指定设备相关的所有连接
	 *  @param dev
	 *  @param isInput 该设备是输入设备还是输出设备
	 */
	public static void delRelation(Device dev, boolean isInput){
		RelationDao rdao = new RelationDao(UIUtils.getContext());
		rdao.delete(dev.getAddress(), dev.getNumber(), isInput);
	}
	
	 /** 将数据库所有信息打印出来
	 *  Function:
	 *  @author lzn 
	 *  2015-1-27 下午6:32:52
	 */
	public static void print(){
		RelationDao rdao = new RelationDao(UIUtils.getContext());
		List<RelationBean> list = rdao.findAll();
		for(RelationBean bean:list){
			MainActivity.showString("_relation记录：" + bean.toString(),
					MultiTextBuffer.TYPE_DEVICE);
		}
	}
}
