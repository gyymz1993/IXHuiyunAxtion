package com.huiyun.ixhuiyunaxtion.master.dao;

import com.huiyun.ixhuiyunaxtion.master.bean.table.RedRay;

import java.util.List;


/**   
 * @Title: InfraredDao.java 
 * @Package com.huiyun.master.dao 
 * @Description: 红外码表的操作
 * @author Yangshao  
 * @date 2015年1月7日 下午4:17:15 
 * @version V1.0   
 */
/**
 * @Description: 红外码表接口
 * @date 2015年2月3日 下午5:04:22
 * @version V1.0
 */
public interface RedRayDao {

	/**
	 * Function: 查询所有红外地址
	 * 
	 * @author Yangshao 2015年4月22日 下午2:16:55
	 * @return
	 */
	// List<Integer> queryAddress();

	/**
	 * Function:保存和更新红外码表
	 * 
	 * @author Yangshao 2015年4月22日 下午2:18:49
	 * @param redRay
	 * @return
	 */
	boolean saveAndUptate(RedRay redRay);

	boolean saveAndupdates(List<RedRay> rayCode);

	/**
	 * Function:删除一个红外码数据
	 * 
	 * @author Yangshao 2015年4月22日 下午2:20:33
	 * @param redRay
	 * @return
	 */
	boolean delete(RedRay redRay);

	/**
	 * Function:得到所有
	 * 
	 * @author Yangshao 2015年4月22日 下午3:13:35
	 * @return
	 */
	List<Integer> queryNumber(Integer adress);

	/**
	 * Function:根据名称和手机编号查询 地址和地址编号
	 * 
	 * @author Yangshao 2015年4月23日 下午4:53:01
	 * @param redRay
	 * @return
	 */
	RedRay queryNumberAndAdress(RedRay redRay);

	/**
	 * Function: 查询所有红外码设备
	 * 
	 * @author Yangshao 2015年1月10日 上午10:19:34
	 * @return
	 */
	List<RedRay> queryAllRedRays();

	/**
	 * 
	 * Function:查询遥控器 不许名称相同
	 * 
	 * @author Yangshao 2015年1月15日 下午3:36:55
	 * @return
	 */
	List<RedRay> queryAllDeviceDisc();

	/**
	 * Function:添加一个遥控器
	 * 
	 * @author Yangshao 2015年2月4日 上午9:58:19
	 * @param rayCode
	 * @return
	 */
	boolean addRedRay(RedRay rayCode);
	
	public boolean deteleBydeviceDisc(List<String> r_name) ;
	
	
	 /**
	 *  Function:查询已存在设备的功能吗
	 *  @author Yangshao 
	 *  2015年4月24日 下午3:17:35
	 *  @param r_name
	 *  @return
	 */
	public RedRay queryDcode(RedRay rayCode);
}
