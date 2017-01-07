package com.huiyun.ixhuiyunaxtion.master.dao.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;


import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;
import com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer;
import com.huiyun.ixhuiyunaxtion.master.bean.table.RedRay;
import com.huiyun.ixhuiyunaxtion.master.dao.RedRayDao;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.db.table.DbModel;
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
public class RedRayDaoImpl implements RedRayDao {

	private DbUtils db;

	public RedRayDaoImpl(Context mcontext) {
		db = DbUtils.create(mcontext);
		db.configAllowTransaction(true);
		db.configDebug(true);
	}

	@Override
	public boolean saveAndUptate(RedRay redRay) {
		try {
			RedRay temp = db.findFirst(Selector.from(RedRay.class)
					.where("r_name", "=", redRay.getR_name())
					.and("btn_code", "=", redRay.getBtn_code()));
			if (temp == null) {
				db.save(redRay);
			} else {
				redRay.setId(temp.getId());
				db.update(redRay, new String[] { "r_name", "btn_code",
						"pageType", "d_address" ,"d_code"});
			}
		} catch (DbException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean delete(RedRay redRay) {
		try {
			db.delete(redRay);
		} catch (DbException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean deteleBydeviceDisc(List<String> r_name) {
		try {
			if (r_name != null && r_name.size() != 0) {
				for (String d : r_name) {
					db.delete(RedRay.class, WhereBuilder.b("r_name", "=", d));
				}
			}

		} catch (DbException e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}

	@Override
	public List<Integer> queryNumber(Integer adress) {
		List<Integer> list = new ArrayList<Integer>();
		if (adress != 0) {
			try {
				List<DbModel> findDbModelAll = db.findDbModelAll(Selector
						.from(RedRay.class).where("d_address", "=", adress)
						.groupBy("d_code"));
				if (findDbModelAll != null && findDbModelAll.size() > 0) {
					for (DbModel mod : findDbModelAll) {
						list.add(mod.getInt("d_code"));
					}
				}
			} catch (DbException e) {
				e.printStackTrace();
				return null;
			}
		}
		return list;
	}

	@Override
	public RedRay queryNumberAndAdress(RedRay redRay) {
		RedRay ray = null;
		try {
			ray = db.findFirst(Selector.from(RedRay.class)
					.where("r_name", "=", redRay.getR_name())
					.and("btn_code", "=", redRay.getBtn_code()));

			System.out.println(ray);
		} catch (DbException e) {
			e.printStackTrace();
			return null;
		}
		return ray;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.huiyun.master.dao.RedRayDao#queryAllRedRays()
	 */
	@Override
	public List<RedRay> queryAllRedRays() {
		try {
			List<RedRay> list = db.findAll(RedRay.class);
			return list;
		} catch (DbException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<RedRay> queryAllDeviceDisc() {
		List<RedRay> redRayCodes = new ArrayList<RedRay>();
		try {
			List<DbModel> models = db.findDbModelAll(Selector
					.from(RedRay.class)
					.groupBy("r_name")
					.select("r_name", "btn_code", "d_address", "pageType",
							"d_code"));
			if (models != null) {
				for (DbModel mod : models) {
					RedRay code = new RedRay();
					code.setR_name(mod.getString("r_name"));
					code.setBtn_code(mod.getInt("btn_code"));
					code.setD_address(mod.getInt("d_address"));
					code.setPageType(mod.getInt("pageType"));
					code.setD_code(mod.getInt("d_code"));
					redRayCodes.add(code);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			MainActivity.showString("查遥控器名时发生数据库错误",
					MultiTextBuffer.TYPE_DEVICE);
			return null;
		}
		return redRayCodes;
	}

	@Override
	public boolean saveAndupdates(List<RedRay> rayCode) {

		try {
			for (RedRay rCode : rayCode) {
				RedRay rs = db.findFirst(Selector.from(RedRay.class)
						.where("r_name", "=", rCode.getR_name())
						.and("btn_code", "=", rCode.getBtn_code()));
				if (rs == null) {
					db.save(rCode);
				} else {
					db.update(rCode, new String[] { "r_name", "btn_code",
							"pageType", "d_address", "d_code" });
				}
			}
			return true;
		} catch (DbException e) {
			e.printStackTrace();
			return false;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.huiyun.master.dao.RedRayDao#addRedRay(com.huiyun.master.bean.table
	 * .RedRay)
	 */
	@Override
	public boolean addRedRay(RedRay rayCode) {
		try {
			RedRay findCode = db.findFirst(Selector.from(RedRay.class).where(
					"r_name", "=", rayCode.getR_name()));
			if (findCode == null) {
				db.save(rayCode);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	@Override
	public RedRay queryDcode(RedRay rCode) {
		RedRay rs;
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
