package com.huiyun.ixhuiyunaxtion.master.dao.impl;//package com.huiyun.master.dao.impl;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.content.Context;
//
//import com.huiyun.master.activity.MainActivity;
//import com.huiyun.master.activity.MultiTextBuffer;
//import com.huiyun.master.bean.table.RedRayCode;
//import com.huiyun.master.dao.InfraredDao;
//import com.lidroid.xutils.DbUtils;
//import com.lidroid.xutils.db.sqlite.Selector;
//import com.lidroid.xutils.db.sqlite.WhereBuilder;
//import com.lidroid.xutils.db.table.DbModel;
//import com.lidroid.xutils.exception.DbException;
//
///**
// * @Title: InfraredDao.java
// * @Package com.huiyun.master.dao.impl
// * @Description: 红外码表的实现
// * @author Yangshao
// * @date 2015年1月7日 下午4:16:45
// * @version V1.0
// */
//@SuppressWarnings("deprecation")
//public class InfraredDaoImpl implements InfraredDao {
//
//	private DbUtils db;
//
//	public InfraredDaoImpl(Context mcontext) {
//		db = DbUtils.create(mcontext);
//		db.configAllowTransaction(true);
//		db.configDebug(true);
//	}
//
//	// function_code和device_disc合并非重
//	@Override
//	public boolean saveAndupdates(RedRayCode rCode) {
//		try {
//			RedRayCode rs = db.findFirst(Selector.from(RedRayCode.class)
//					.where("device_disc", "=", rCode.getDevice_disc())
//					.and("function_code", "=", rCode.getFunction_code()));
//			if (rs == null) {
//				db.save(rCode);
//			} else {
//				rCode.setId(rs.getId());
//				db.update(rCode, new String[] { "real_code", "function_code",
//						"device_disc", "pageType" });
//			}
//			return true;
//		} catch (DbException e) {
//			e.printStackTrace();
//			return false;
//		}
//	}
//
//	@Override
//	public boolean saveAndupdates(List<RedRayCode> rayCode) {
//		try {
//			for (RedRayCode rCode : rayCode) {
//				RedRayCode rs = db.findFirst(Selector.from(RedRayCode.class)
//						.where("device_disc", "=", rCode.getDevice_disc())
//						.and("function_code", "=", rCode.getFunction_code()));
//				if (rs == null) {
//					db.save(rCode);
//				} else {
//					rCode.setId(rs.getId());
//					db.update(rCode, new String[] { "real_code",
//							"function_code", "device_disc", "pageType" });
//				}
//			}
//			return true;
//		} catch (DbException e) {
//			e.printStackTrace();
//			return false;
//		}
//
//	}
//
//	@Override
//	public RedRayCode queryRedRayCodeId(RedRayCode rCode) {
//		try {
//			RedRayCode rs = db.findFirst(Selector.from(RedRayCode.class)
//					.where("function_code", "=", rCode.getFunction_code())
//					.and("device_disc", "=", rCode.getDevice_disc()));
//			if (rs != null) {
//				return rs;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//		return null;
//	}
//
//	@Override
//	public RedRayCode queryRedRayByID(int rayCode_Id) {
//		try {
//			RedRayCode code = db.findById(RedRayCode.class, rayCode_Id);
//			return code;
//		} catch (DbException e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
//
//	@Override
//	public List<RedRayCode> queryAllRedRays() {
//		try {
//			List<RedRayCode> list = db.findAll(RedRayCode.class);
//			return list;
//		} catch (DbException e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
//
//	@Override
//	public List<RedRayCode> queryAllDeviceDisc() {
//		List<RedRayCode> redRayCodes = new ArrayList<RedRayCode>();
//		try {
//			List<DbModel> models = db.findDbModelAll(Selector
//					.from(RedRayCode.class)
//					.groupBy("device_disc")
//					.select("device_disc", "real_code", "function_code",
//							"pageType"));
//			if (models != null) {
//				for (DbModel mod : models) {
//					RedRayCode code = new RedRayCode();
//					code.setDevice_disc(mod.getString("device_disc"));
//					code.setReal_code(mod.getString("real_code"));
//					code.setFunction_code(mod.getInt("function_code"));
//					code.setPageType(mod.getInt("pageType"));
//					redRayCodes.add(code);
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			MainActivity.showString("查遥控器名时发生数据库错误"
//					,MultiTextBuffer.TYPE_DEVICE);
//			return null;
//		}
//		return redRayCodes;
//	}
//
//	@Override
//	public boolean deteleBydeviceDisc(List<String> disc) {
//		try {
//			if (disc != null && disc.size() != 0) {
//				for (String d : disc) {
//					db.delete(RedRayCode.class,
//							WhereBuilder.b("device_disc", "=", d));
//				}
//			}
//
//		} catch (DbException e) {
//			e.printStackTrace();
//			return false;
//		}
//		return true;
//
//	}
//
//	@Override
//	public boolean addRedRayCode(RedRayCode rayCode) {
//		try {
//			RedRayCode findCode = db.findFirst(Selector.from(RedRayCode.class)
//					.where("device_disc", "=", rayCode.getDevice_disc()));
//			if (findCode == null) {
//				db.save(rayCode);
//				return true;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			return false;
//		}
//		return false;
//	}
//
//}
