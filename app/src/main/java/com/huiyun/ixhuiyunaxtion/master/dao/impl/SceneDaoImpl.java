package com.huiyun.ixhuiyunaxtion.master.dao.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;
import com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer;
import com.huiyun.ixhuiyunaxtion.master.bean.table.SceneItem;
import com.huiyun.ixhuiyunaxtion.master.dao.SceneDao;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.db.table.DbModel;
import com.lidroid.xutils.exception.DbException;

/**   
 * @Description: TODO(用一句话描述该文件做什么) 
 * @date 2015年1月24日 上午11:49:32 
 * @version V1.0   
 */
public class SceneDaoImpl implements SceneDao {
	/**
	 */
	private DbUtils db;
	public SceneDaoImpl(Context context) {
		db = DbUtils.create(context);
		db.configAllowTransaction(true);
		db.configDebug(true);
	}
	
	public boolean updateScene(String sceneName, List<SceneItem> scenes) {
		try {
			// 首先，根据场景名删除所有场景记录
			deleteSceneByName(sceneName);
			// 然后，把列表里的场景记录添加到数据库
			if(scenes != null){
				saveAndUpdate(scenes);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	
	@Override
	public boolean updateScenes(List<SceneItem> scenes) {
		try {
			//db.deleteAll(SceneItem.class);
			if(scenes!=null&&scenes.size()!=0){
				for(SceneItem entity:scenes){
					deleteSceneByName(entity.getScene_name());
				}
				saveAndUpdate(scenes);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	@Override
	public List<SceneItem> getSceneName() {
		List<SceneItem> sceneItems=new ArrayList<SceneItem>();
		try {
			List<DbModel> findDbModelAll = db.findDbModelAll
					(Selector.from(SceneItem.class).groupBy("scene_name").select("image_bg","scene_name"));
			if (findDbModelAll != null && findDbModelAll.size() > 0) {
				findDbModelAll.size();
				for (DbModel mod : findDbModelAll) {
					SceneItem item=new SceneItem();
					item.setScene_name(mod.getString("scene_name"));
					item.setImage_bg(mod.getInt("image_bg"));
					//item.setImage_bg();
					sceneItems.add(item);
				}
			}
		} catch (DbException e) {
			e.printStackTrace();
			return null;
		}
		return sceneItems;
	}

	@Override
	public List<SceneItem> queryPhoneCodeforScene(String sceneName) {
		List<SceneItem> scenes=new ArrayList<SceneItem>();
		try {
			scenes=db.findAll(Selector.from(SceneItem.class).where(
					"scene_name","=",sceneName));
		} catch (DbException e) {
			e.printStackTrace();
			return null;
		}
		return scenes;
	}

	@Override
	public boolean saveAndUpdate(List<SceneItem> scenes) {
		for(SceneItem temp:scenes){
			try {
				SceneItem scene=db.findFirst(Selector.from(SceneItem.class).
						where("scene_name","=",temp.getScene_name()).
						and("phone_code","=",temp.getPhone_code()));
				if(scene!=null){
					temp.setId(scene.getId());
					db.update(temp, new String[]{"scene_name",
							"phone_code","state","image_bg"});
				}else{
					db.save(temp);
				}
			} catch (DbException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public boolean deleteSceneByName(String scenesName) {
		try {
			db.delete(SceneItem.class, WhereBuilder.b("scene_name", "=", scenesName));
		} catch (DbException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	
	/**
	 * 打印所有场景
	 */
	@Override
	public List<SceneItem> getAllScenes() {
		List<SceneItem>  items=new ArrayList<SceneItem>();
		try {
			items=db.findAll(SceneItem.class);
		} catch (DbException e) {
			e.printStackTrace();
			return null;
		}
		return items;
	}
	
	/**
	 * 打印所有场景
	 */
	@Override
	public boolean printAllScene() {
		try {
			List<SceneItem>  items=db.findAll(SceneItem.class);
			for(SceneItem sceneItem:items){
				MainActivity.showString(
						sceneItem.toString(), MultiTextBuffer.TYPE_OTHER);
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
		return false;
	}
}
