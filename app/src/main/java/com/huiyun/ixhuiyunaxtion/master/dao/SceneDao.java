package com.huiyun.ixhuiyunaxtion.master.dao;

import com.huiyun.ixhuiyunaxtion.master.bean.table.SceneItem;

import java.util.List;


/**
 * @Description: 场景DAO
 * @date 2015年1月19日 下午2:30:07
 * @version V1.0
 */
public interface SceneDao {

	/**
	 * Function: 删除对应场景号下的所有场景，然后添加记录
	 * scenes为空时，该方法的功能变成仅仅删除指定场景
	 * @author Yangshao 2015年1月19日 下午2:35:01
	 * 
	 * 成功后返回true，否则返回false
	 */
	public boolean updateScene(String sceneName, List<SceneItem> scenes);

	
	public boolean updateScenes(List<SceneItem> scenes);
	 /**
	 * Function: 根据场景名删除记录
	 *  
	 * @author Yangshao  
	 * @param sceneName
	 * 成功后返回true，否则返回false
	 */
	public boolean deleteSceneByName(String sceneName);
	
	 /**
	 * Function:查询所有场景
	 * 
	 * @author Yangshao 2015年1月19日 下午2:38:43
	 * 
	 * 没有数据返回null
	 */
	public List<SceneItem> getSceneName();

	/**
	 * Function:查询场景
	 * 
	 * @author Yangshao 2015年1月19日 下午2:38:43
	 * 
	 * 得到场景的设备 返回场景
	 * 
	 * 没有 返回null
	 */
	public List<SceneItem> queryPhoneCodeforScene(String sceneName);

	public boolean saveAndUpdate(List<SceneItem> scenes);
	
	/**
	 * 
	 *  Function:得到所有场景
	 *  @author Yangshao 
	 *  2015年1月26日 下午4:17:11
	 *  @return
	 */
	public boolean printAllScene();
	
	public List<SceneItem> getAllScenes();
}
