package com.huiyun.ixhuiyunaxtion.master.dao;

import com.huiyun.ixhuiyunaxtion.master.bean.table.User;

import java.util.List;


/**   
 * @Title: UserDao.java 
 * @Package com.huiyun.ixconfig.dao.impl 
 * @Description: User表数据库的操作
 * @author Yangshao  
 * @date 2015年1月6日 下午5:24:15 
 * @version V1.0   
 */
public interface UserDao {
	
	
	/**
	 *  插入或根据用户名强制更新，但是type=2不能覆盖type=1;
	 * @param user
	 * @return  是否存入或更新进数据库
	 */
	boolean saveOrupdateForce(User user);
	
	 /**
	 *  Function: 查询User表 根据用户名和密码查询
	 *  @author Yangshao 
	 *  2015年1月6日 下午5:26:14
	 *  @return 成功 返回true
	 */
	User queryUser(User user);
	/**
	 * 
	 *  Function:查询所有用户
	 *  @author busy 
	 *  2015-1-26 下午2:23:57
	 *  @return list
	 */
	List<User> queryAllUser();
	
}
