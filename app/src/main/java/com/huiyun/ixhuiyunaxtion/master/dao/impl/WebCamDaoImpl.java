package com.huiyun.ixhuiyunaxtion.master.dao.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;


import com.huiyun.ixhuiyunaxtion.master.bean.table.Webcam;
import com.huiyun.ixhuiyunaxtion.master.dao.WebCamDao;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.table.DbModel;
import com.lidroid.xutils.exception.DbException;

/**
 * @Description: TODO(用一句话描述该文件做什么)
 * @date 2015年1月19日 下午2:30:07
 * @version V1.0
 */
public class WebCamDaoImpl implements WebCamDao {
	DbUtils db;

	public WebCamDaoImpl(Context context) {
		db = DbUtils.create(context);
		db.configAllowTransaction(true);
		db.configDebug(true);
	}

	@Override
	public boolean addAndUpdateWebCam(List<Webcam> webcams) {
		try {
			if (webcams != null && webcams.size() != 0)
				for (Webcam webcam : webcams) {
					Webcam temp = db.findFirst(Selector.from(Webcam.class)
							.where("did", "=", webcam.getDid()));
					if (temp != null) {
						webcam.setId(temp.getId());
						db.update(webcam, new String[] { "did", "area" });
					} else {
						db.save(webcam);
					}
				}
		} catch (DbException e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}

	@Override
	public List<Webcam> getAllWebCam() {
		List<Webcam> list = new ArrayList<Webcam>();
		try {
			List<DbModel> models = db.findDbModelAll(Selector
					.from(Webcam.class).groupBy("did").select("did", "area"));
			if (models != null)
				for (DbModel model : models) {
					Webcam webcam = new Webcam();
					webcam.setDid(model.getString("did"));
					webcam.setArea(model.getString("area"));
					list.add(webcam);
				}
		} catch (DbException e) {
			e.printStackTrace();
		}
		return list;
	}

}
