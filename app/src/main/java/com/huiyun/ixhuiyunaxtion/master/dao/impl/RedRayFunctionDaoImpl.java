package com.huiyun.ixhuiyunaxtion.master.dao.impl;

import android.content.Context;

import com.huiyun.ixhuiyunaxtion.master.bean.table.RedRay;
import com.huiyun.ixhuiyunaxtion.master.bean.table.RedRayFunction;
import com.huiyun.ixhuiyunaxtion.master.dao.RedRayFunctionDao;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

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
public class RedRayFunctionDaoImpl implements RedRayFunctionDao {

	private DbUtils db;

	public RedRayFunctionDaoImpl(Context mcontext) {
		db = DbUtils.create(mcontext);
		db.configAllowTransaction(true);
		db.configDebug(true);
	}

	@Override
	public boolean addRedRayFunction(RedRayFunction redRay) {
		try {
			RedRayFunction temp = db.findFirst(Selector.from(RedRay.class)
					.where("r_name", "=", redRay.getR_name())
					.and("btn_code", "=", redRay.getBtn_code()));
			if (temp == null) {
				db.save(redRay);
			} else {
				redRay.setId(temp.getId());
				db.update(redRay, new String[] { "r_name", "btn_code",
						"d_function" });
			}
		} catch (DbException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}


	@Override
	public RedRayFunction queryRedRayFunction(RedRayFunction rCode) {
		RedRayFunction rs;
		try {
			rs = db.findFirst(Selector.from(RedRay.class)
					.where("r_name", "=", rCode.getR_name())
					.and("btn_code", "=", rCode.getBtn_code()));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return rs;
	}

}
