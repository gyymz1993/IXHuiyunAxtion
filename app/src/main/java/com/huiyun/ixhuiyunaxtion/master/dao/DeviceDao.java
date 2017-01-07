package com.huiyun.ixhuiyunaxtion.master.dao;

import com.huiyun.ixhuiyunaxtion.master.bean.Slave;
import com.huiyun.ixhuiyunaxtion.master.bean.table.Device;

import java.util.List;



public interface DeviceDao {
	/**
	 * 设置PhoneCode之后 储存或更新集合中的全部设备， 根据chipId和number判断是否是同一个设备
	 * 
	 * @param list
	 * @return 执行结果是否成功
	 */
	boolean saveOrUpdateDevices(List<Device> list);

	/**
	 * Function:以地址&编号作为唯一值更新设备的区域和名字信息
	 * 当设备的区域和名字为空时，删除该设备
	 * @author lzn
	 * @param list
	 * @return
	 */
	boolean UpdateAreaAndNameOrDeleteForDevices(List<Device> list);

	/**
	 * Function: 没有设置PhoneCode之前
	 * 
	 * @author Yangshao 2015年1月8日 下午4:31:55
	 * @param list
	 * @return
	 */
	boolean saveOrUpdateNoPhoneCodeDevices(List<Device> list);

	/**
	 * Function: 存储新设备
	 * 
	 * @author Yangshao 2015年1月8日 下午4:31:55
	 * @param list
	 * @return
	 */
	boolean saveNewDevices(List<Device> list);

	/**
	 * 根据设备类型获取设备地址，不能有重复的地址
	 * 
	 * @param type
	 * @return
	 */
	List<Integer> getAddressByType(int type);

	/**
	 * 获取所有设备地址，不能有重复的地址
	 * 
	 * @param type
	 * @return
	 */
	List<Integer> getAllAddress();

	/**
	 * Function: 查询所有设备
	 * 
	 * @author Yangshao 2015年1月10日 下午1:55:01
	 * @return
	 */
	List<Device> queryAllDevices();

	/**
	 * Function: 得到所有的输出设备
	 * 
	 * @author Yangshao 2015年1月10日 下午5:52:34
	 * @return
	 */
	List<Device> queryAllOutDevices();

	/**
	 * Function: 得到所有的输入设备
	 * 
	 * @author lzn
	 * @return
	 */
	List<Device> queryAllInDevices();

	/**
	 * Function: 根据地址获得指定从机下的所有设备
	 * 
	 * @author lzn 2015-1-14 下午6:08:45
	 * @param addr
	 * @return
	 */
	List<Device> getAllDevicesByAddress(int addr);

	/**
	 * Function:根据地址和编号获得指定设备
	 * 
	 * @author lzn 2015-1-29 下午6:58:40
	 * @param addr
	 * @param number
	 * @return
	 */
	Device getDeviceByAddrAndNumber(int addr, int number);

	/**
	 * Function: 删除设备（可能根据芯片ID和编号来判断）
	 * 
	 * @author lzn 2015-1-14 下午7:05:27
	 * @param addr
	 * @param number
	 */
	void deleteDevice(Device dev);

	/**
	 * Function:得到所有网关从机
	 * 
	 * @author Yangshao 2015年1月15日 下午1:16:19
	 * @return
	 * 
	 *         TODO 这方法的实现好像有点奇怪……
	 */
	List<Device> getAllGateway();

	/**
	 * Function:得到所有区域
	 * 
	 * @author Yangshao 2015年1月19日 下午1:57:32
	 * @return
	 */
	List<String> getAllAreaforDevice();

	/**
	 * Function:获取所有从机
	 * 
	 * @author lzn 2015-2-27 上午10:51:53
	 * @return
	 */
	List<Slave> getAllSlave();

	/**
	 * Function: 删除指定从机的所有设备
	 * 
	 * @author lzn 2015-2-27 上午10:55:30
	 * @param addr
	 */
	void deleteSlave(int addr);
}
