package com.huiyun.ixhuiyunaxtion.master.dao;



/**   
 * @Title: InfraredDao.java 
 * @Package com.huiyun.master.dao 
 * @Description: 红外码表的操作
 * @author Yangshao  
 * @date 2015年1月7日 下午4:17:15 
 * @version V1.0   
 */

import com.huiyun.ixhuiyunaxtion.master.bean.table.RedRayFunction;

/**
 * @Description: 红外码表接口
 * @date 2015年2月3日 下午5:04:22
 * @version V1.0
 */
public interface RedRayFunctionDao {

	
	 /**
	 *  Function:
	 *  @author Yangshao 
	 *  2015年5月19日 上午9:56:20
	 *  @param rayFunction
	 *  @return
	 */
	boolean addRedRayFunction(RedRayFunction rayFunction);
	
	
	
	 /**
	 *  Function:
	 *  @author Yangshao 
	 *  2015年5月19日 上午9:56:12
	 *  @return
	 */
	RedRayFunction queryRedRayFunction(RedRayFunction function);
}
