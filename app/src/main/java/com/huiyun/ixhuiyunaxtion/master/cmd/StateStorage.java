package com.huiyun.ixhuiyunaxtion.master.cmd;

import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;
import com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer;
import com.huiyun.ixhuiyunaxtion.master.bean.table.Device;
import com.huiyun.ixhuiyunaxtion.master.dao.DeviceDao;
import com.huiyun.ixhuiyunaxtion.master.dao.impl.DeviceDaoImpl;
import com.huiyun.ixhuiyunaxtion.master.inner.OnResultListener;
import com.huiyun.ixhuiyunaxtion.master.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;


/**   
 * @Description: 存储所有设备状态的容器，全局唯一
 * @author lzn
 * @date 2015-1-15 上午11:41:44 
 * @version V1.0   
 */
public class StateStorage {
	private static StateStorage instance;
	public static StateStorage getInstance(){
		synchronized (StateStorage.class) {
			if(instance == null){
				instance = new StateStorage();
			}
			
			return instance;
		}
	}
	private StateStorage(){
		
	}
	
	private OnResultListener<Device> listener;
	private List<Device> listDev;
	
	 /**
	 *  Function: 当有device的状态被更新时，该监听会有回调
	 *  @author lzn 
	 *  2015-1-15 下午1:22:31
	 *  @param listener
	 */
	public void setOnStateChangeListener(OnResultListener<Device> listener){
		this.listener = listener;
	}
	
	
	 /**
	 *  Function: 更新所有device的状态，有更新时会回调
	 *  @author lzn 
	 *  2015-1-15 下午1:23:35
	 */
	public void update(){
		// 首先从数据库中拿到所有输出设备
		MainActivity.showString("准备更新StateStorage",MultiTextBuffer.TYPE_DEVICE);
		DeviceDao ddao = new DeviceDaoImpl(UIUtils.getContext());
		List<Device> listDevTemp = ddao.queryAllOutDevices();
		if(listDevTemp == null) return;
		
		// 剔除掉网关本身
		List<Device> listDevRemove = new ArrayList<Device>();
		for(Device dev:listDevTemp){
			if(dev.getType() == Device.TYPE_GATEWAY && dev.getNumber() == 0){
				listDevRemove.add(dev);
			}
		}
		listDevTemp.removeAll(listDevRemove);
		
		// 更新容器内的列表的信息
		updateListDevice(listDevTemp);
		
		if (listDev != null) {
			// 将列表所有设备设为不明确状态,并发送读取状态的命令
			for (Device dev : listDev) {
				dev.setReable(false);
				CmdSender.readState(dev.getAddress(), dev.getNumber(),
						dev.getType());
			}
		}
	}
	
	
	 /**
	 *  Function: 写入状态，如果写入成功会有回调
	 *  @author lzn 
	 *  2015-1-15 下午1:24:59
	 *  @param phoneCode 手机按键码
	 *  @param state 状态值
	 */
	public void writeState(int phoneCode, int state){
		CmdSender.writeState(phoneCode, state);
	}
	
	
	 /**
	 *  Function: 读取状态，能立刻获得信息,如果没有信息那么返回null
	 *  @author lzn 
	 *  2015-1-15 下午1:26:32
	 *  @param phoneCode 手机按键码
	 *  @return
	 */
	public Device readState(int phoneCode){
		if (listDev != null) {
			for (Device dev : listDev) {
				if (dev.getPhoneCode() == phoneCode) {
					return dev;
				}
			}
		}
		return null;
	}
	
	
	 /**
	 *  Function: 获取所有设备的状态,如果没有设备会返回null
	 *  @author lzn 
	 *  2015-1-15 下午1:33:26
	 *  @return
	 */
	public List<Device> readAllState(){
		updateAreaNameAndDetails();
		return listDev;
	}
	
	
	 /**
	 *  Function: 从从机获得状态信息以更新列表，如果有状态改变就回调
	 *  @author lzn 
	 *  2015-1-15 下午3:35:37
	 *  @param addr
	 *  @param number
	 *  @param state
	 */
	public void getUpdateInfoFromRF(int addr, int number, int state){
		if (listDev != null) {
			for (Device dev : listDev) {
				if (dev.getAddress() == addr && dev.getNumber() == number) {
					// 找到该设备
					dev.setReable(true);
					if (dev.getState() != state) {
						// 状态需要更新
						dev.setState(state);
						MainActivity.showString("StateStorage更新设备："
								+ dev.toString() + ",状态为" + dev.getState(),
								MultiTextBuffer.TYPE_DEVICE);
					}
					// 无论状态是否改变，都会回调
					if (listener != null) {
						listener.onResult(true, dev);
					}
				}
			}
		}
	}
	
	
	 /**
	 *  Function: 根据地址和编号读取设备的状态，能立刻获得信息，如果没有信息返回null
	 *  @author lzn 
	 *  2015-1-15 下午6:54:35
	 *  @param addr
	 *  @param number
	 *  @return
	 */
	public Device readState(int addr, int number){
		if (listDev != null) {
			for (Device dev : listDev) {
				if (dev.getAddress() == addr && dev.getNumber() == number) {
					return dev;
				}
			}
		}
		return null;
	}
	
	
	 /**
	 *  Function: 根据设备地址获得设备类型，如果没找到则返回0
	 *  @author lzn 
	 *  2015-1-22 上午10:33:28
	 *  @param addr
	 *  @return
	 */
	public int getTypeByAddress(int addr){
		if (listDev != null) {
			for (Device dev : listDev) {
				if (dev.getAddress() == addr) {
					return dev.getType();
				}
			}
		}
		return 0;
	}
	
	/**
	 *  Function: 根据手机按键码获得设备类型，如果没找到则返回0
	 *  @author lzn 
	 *  2015-1-22 上午10:33:28
	 *  @param addr
	 *  @return
	 */
	public int getTypeByPhoneCode(int phoneCode){
		if (listDev != null) {
			for (Device dev : listDev) {
				if (dev.getPhoneCode() == phoneCode) {
					return dev.getType();
				}
			}
		}
		return 0;
	}
	
	 /**
	 *  Function: 添加新设备到列表
	 *  @author lzn 
	 *  2015-1-15 下午2:29:13
	 *  @param list
	 */
	private void updateListDevice(List<Device> list){
		if(listDev == null){
			// 首次更新时
			listDev = list;
			for(Device dev:listDev){
				MainActivity.showString("StateStorage添加新设备：" + dev.toString() + 
						",状态为" + dev.getState(),
						MultiTextBuffer.TYPE_DEVICE);
			}
		} else {
			// 之后有新设备加入时
			for(Device dev:list){
				if(!listDev.contains(dev)){
					listDev.add(dev);
					MainActivity.showString("StateStorage添加新设备：" + dev.toString() + 
							",状态为" + dev.getState(),
							MultiTextBuffer.TYPE_DEVICE);
				}
			}
		}
	}
	
	
	 /** 根据数据库更新设备的区域、名字和类别
	 *  Function:
	 *  @author lzn 
	 *  2015-1-29 下午6:54:15
	 */
	public void updateAreaNameAndDetails(){
		DeviceDao ddao = new DeviceDaoImpl(UIUtils.getContext());
		
		if(listDev != null){
			for(Device dev:listDev){
				Device findDev = ddao.getDeviceByAddrAndNumber(
						dev.getAddress(), dev.getNumber());
				if(findDev != null){
					dev.setArea(findDev.getArea());
					dev.setName(findDev.getName());
					dev.setDetails(findDev.getDetails());
				}
			}
		}
	}
}
