package com.huiyun.ixhuiyunaxtion.master.dao;//package com.huiyun.master.dao;
//
//import java.util.List;
//
//import com.huiyun.master.bean.table.RedRayCode;
//
///**   
// * @Title: InfraredDao.java 
// * @Package com.huiyun.master.dao 
// * @Description: 红外码表的操作
// * @author Yangshao  
// * @date 2015年1月7日 下午4:17:15 
// * @version V1.0   
// */
///**   
//* @Description: TODO(用一句话描述该文件做什么) 
//* @date 2015年2月3日 下午5:04:22 
//* @version V1.0   
//*/
//@SuppressWarnings("deprecation")
//public interface InfraredDao {
//	 /**
//	 *  Function: 更新和插入
//	 *  @author Yangshao 
//	 *  2015年1月6日 下午5:25:29
//	 *  @return 成功返回ture
//	 */
//	boolean saveAndupdates(RedRayCode rayCode);
//	
//	
//	
//	 /**
//	 *  Function:添加一个遥控器
//	 *  @author Yangshao 
//	 *  2015年2月4日 上午9:58:19
//	 *  @param rayCode
//	 *  @return
//	 */
//	boolean addRedRayCode(RedRayCode rayCode);
//	
//	boolean saveAndupdates(List<RedRayCode> rayCode);
//
//	 /**
//	 *  Function: 查询RedRay表 根据function_code和device_disc查询
//	 *  @author Yangshao 
//	 *  2015年1月6日 下午5:26:14
//	 *  @return 成功 返回RedRayCode
//	 */
//	RedRayCode queryRedRayCodeId(RedRayCode rayCode);
//	
//	/**
//	 * 
//	 *  Function:根据ID查询
//	 *  @author Yangshao 
//	 *  2015年1月7日 下午6:35:19
//	 *  @param rayCode
//	 *  @return
//	 */
//	RedRayCode queryRedRayByID(int rayCode_ID);
//	
//	
//	
//	 /**
//	 *  Function: 查询所有红外码设备
//	 *  @author Yangshao 
//	 *  2015年1月10日 上午10:19:34
//	 *  @return
//	 */
//	List<RedRayCode> queryAllRedRays();
//	
//	/**
//	 * 
//	 *  Function:查询遥控器    不许名称相同 
//	 *  @author Yangshao 
//	 *  2015年1月15日 下午3:36:55
//	 *  @return
//	 */
//	List<RedRayCode> queryAllDeviceDisc();
//	
//	
//	
//	
//	 /**
//	 *  Function: 根据遥控器名称删除遥控器
//	 *  @author Yangshao 
//	 *  2015年2月3日 下午5:04:24
//	 *  @param disc
//	 *  @return
//	 */
//	boolean deteleBydeviceDisc(List<String> disc);
//	
//}
