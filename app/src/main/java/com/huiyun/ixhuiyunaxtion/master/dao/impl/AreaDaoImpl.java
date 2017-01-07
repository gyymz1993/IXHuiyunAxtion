

package com.huiyun.ixhuiyunaxtion.master.dao.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.huiyun.ixhuiyunaxtion.master.bean.table.Area;
import com.huiyun.ixhuiyunaxtion.master.dao.AreaDao;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.table.DbModel;
import com.lidroid.xutils.exception.DbException;

/**
 * @Description: 保存所有区域
 * @date 2015年1月19日 下午2:32:27
 * @version V1.0
 */
public class AreaDaoImpl implements AreaDao {
	private DbUtils db;

	public AreaDaoImpl(Context mcontext) {
		db = DbUtils.create(mcontext);
		db.configAllowTransaction(true);
		db.configDebug(true);
	}

	@Override
	public boolean addAndUpdateArea(Area area) {
		try {
			Area area2 = queryEare(area);
			if (area2 != null) {
				area.setId(area2.getId());
				db.update(area, new String[] { "areaName" });
			} else {
				db.save(area);
			}
			return true;
		} catch (DbException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Area queryEare(Area area) {
		Area areas = null;
		try {
			areas = db.findFirst(Selector.from(Area.class).where("areaName",
					"=", area.getAreaName()));
		} catch (DbException e) {
			e.printStackTrace();
		}
		return areas;
	}

	
	@Override
	public List<String> getAllAreaforArea() {
		List<String> areas = new ArrayList<String>();
		try {
			List<DbModel> findDbModelAll = db.findDbModelAll(Selector.from(
					Area.class).groupBy("areaName"));
			if (findDbModelAll != null && findDbModelAll.size() > 0) {
				for (DbModel mod : findDbModelAll) {
					areas.add(mod.getString("areaName"));
				}
			}
		} catch (DbException e) {
			e.printStackTrace();
			return null;
		}
		return areas;
	}

	@Override
	public boolean saveAndUpdateAreas(List<Area> areas) {
		try {
			db.deleteAll(Area.class);
			db.saveAll(areas);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
