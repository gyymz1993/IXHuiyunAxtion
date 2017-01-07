package com.huiyun.ixhuiyunaxtion.master.cmd;

import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;
import com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer;
import com.huiyun.ixhuiyunaxtion.master.bean.table.Device;
import com.huiyun.ixhuiyunaxtion.master.dao.DeviceDao;
import com.huiyun.ixhuiyunaxtion.master.dao.impl.DeviceDaoImpl;
import com.huiyun.ixhuiyunaxtion.master.net.RelationLinker;
import com.huiyun.ixhuiyunaxtion.master.utils.UIUtils;

import java.util.List;


/**   手机按键码分配器
 * @Description: 给设备的phonecode码进行分配和重分配
 * 每次有新的输出设备加入的时候使用
 * @date 2015-1-14 上午10:33:18 
 * @author lzn
 * @version V1.0   
 */
public class PhoneCodeAllocator {
	// question:
	// 假设有一个长度为N的列表，有若干个元素，每个元素可以有两种状态：要么为0，要么为1~N中的不重复的数字。
	// 现在，要把该列表中的所有处于第一种状态的元素转换成第二种状态，有什么方法能快捷简便呢？
	
	
	 /**
	 *  Function:给所有未分配手机按键码的输出设备分配手机按键码
	 */
	public static void allocatePhoneCode(){
		DeviceDao ddao = new DeviceDaoImpl(UIUtils.getContext());
		List<Device> list = ddao.queryAllOutDevices();
		if(list == null) {
			return;
		}
		
		// 分配手机按键码
		boolean changed = false; // 如果信息有所变更,该值会被设为true
		for(Device dev:list){
			if(dev.getType() == 12 && dev.getNumber() == 0){
				// 如果该输出设备为网关本身，将其忽视
				continue;
			}
			// 如果该设备的手机按键码为0，说明仍未分配，需要重新分配按键码给它
			if(dev.getPhoneCode() == 0){
				dev.setPhoneCode(getNewPhoneCode(list));
				RelationLinker.linkRelation(dev.getPhoneCode(), dev); // 在关联表中添加记录
				MainActivity.showString("给地址为" + dev.getAddress() +
						"、编号为" + dev.getNumber() + 
						"的设备分配手机按键码" + dev.getPhoneCode(),
						MultiTextBuffer.TYPE_DEVICE);
				changed = true;
			}
		}
		
		// 保存到数据库
		ddao.saveOrUpdateDevices(list);
		if(changed){
			// 更新设备的状态
			StateStorage.getInstance().update();
		}
	}
	
	private static int getNewPhoneCode(List<Device> list){
		// 从1开始查看哪个号码未被使用
		for(int i=1; i<256; i++){
			boolean used = false;
			for(Device dev:list){
				if(dev.getPhoneCode() == i){
					// 说明该号码已被使用
					used = true;
					continue;
				}
			}
			if(!used){
				// 返回该未被使用的号码
				return i;
			}
		}
		
		// 所有号码都被使用过，返回0
		return 0;
	}
}
