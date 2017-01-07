package com.huiyun.ixhuiyunaxtion.master.dao;

import com.huiyun.ixhuiyunaxtion.master.bean.table.Area;

import java.util.List;


/**
 * @Description: TODO(用一句话描述该文件做什么)
 * @date 2015年1月19日 下午2:30:07
 * @version V1.0
 */
public interface AreaDao {

	/**
	 * Function:添加和更新区域
	 * 
	 * @author Yangshao 2015年1月19日 下午2:35:01
	 */
	public boolean addAndUpdateArea(Area area);

	/**
	 * Function:查询所有区域
	 * 
	 * @author Yangshao 2015年1月19日 下午2:38:43
	 */
	public List<String> getAllAreaforArea();

	/**
	 * Function:查询区域
	 * 
	 * @author Yangshao 2015年1月19日 下午2:38:43
	 */
	public Area queryEare(Area area);

	public boolean saveAndUpdateAreas(List<Area> areas);

}
