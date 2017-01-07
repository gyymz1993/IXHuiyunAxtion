package com.huiyun.ixhuiyunaxtion.master.dao.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;


import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;
import com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer;
import com.huiyun.ixhuiyunaxtion.master.bean.Slave;
import com.huiyun.ixhuiyunaxtion.master.bean.table.Device;
import com.huiyun.ixhuiyunaxtion.master.dao.DeviceDao;
import com.huiyun.ixhuiyunaxtion.master.net.RelationLinker;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.table.DbModel;
import com.lidroid.xutils.exception.DbException;

/**
 * 设备的数据库处理类
 * 
 * @author Torah
 * 
 */
public class DeviceDaoImpl implements DeviceDao {

	private DbUtils db;
	
	public static final String[] DEVICES_NAME_PHONECODE = new String[] {
			"area", "name", "details", "address", "type", "phoneCode" };
	
	public static final String[] DEVICES_NAME_NO_PHONECODE = new String[] {
			"area", "name", "details", "address", "type" };
	
	public static final String[] UPDATE_AREA_NAME = new String[] {
		 "area", "name"};

	public DeviceDaoImpl(Context mcontext) {
		db = DbUtils.create(mcontext);
		db.configAllowTransaction(true);
		db.configDebug(true);
	}

	@Override
	public boolean saveOrUpdateNoPhoneCodeDevices(List<Device> lists) {
		try {
			for (Device dtemp : lists) {
				Device devicehad = db.findFirst(Selector.from(Device.class)
						.where("chipId", "=", dtemp.getChipId())
						.and("number", "=", dtemp.getNumber()));
				// 如果不存在，则添加，已存在则更新
				if (devicehad == null) {
					db.save(dtemp);
				} else {
					dtemp.setId(devicehad.getId());
					db.update(dtemp, DEVICES_NAME_NO_PHONECODE);
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean saveOrUpdateDevices(List<Device> lists) {
		try {
			for (Device dtemp : lists) {
				Device devicehad = db.findFirst(Selector.from(Device.class)
						.where("chipId", "=", dtemp.getChipId())
						.and("number", "=", dtemp.getNumber()));
				// 如果不存在，则添加，已存在则更新
				if (devicehad == null)
					db.save(dtemp);
				else {
					dtemp.setId(devicehad.getId());
					db.update(dtemp, DEVICES_NAME_PHONECODE);
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean saveNewDevices(List<Device> list) {
		try {
			for (Device dtemp : list) {
				Device devicehad = db.findFirst(Selector.from(Device.class)
						.where("chipId", "=", dtemp.getChipId())
						.and("number", "=", dtemp.getNumber()));
				// 如果不存在，则添加，已存在则更新
				if (devicehad == null) {
					db.save(dtemp);
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<Integer> getAddressByType(int type) {
		List<Integer> list = new ArrayList<Integer>();
		try {
			List<DbModel> findDbModelAll = db.findDbModelAll(Selector
					.from(Device.class).where("type", "=", type)
					.groupBy("address"));
			if (findDbModelAll != null && findDbModelAll.size() > 0) {
				for (DbModel mod : findDbModelAll) {
					list.add(mod.getInt("address"));
				}
			}
		} catch (Exception e) {
			return list;
		}
		MainActivity.showString("查找到了type " + type + " 类设备 " + list.size()
				+ "个",MultiTextBuffer.TYPE_DEVICE);
		return list;
	}

	@Override
	public List<Device> queryAllDevices() {
		try {
			List<Device> list = db.findAll(Device.class);
			return list;
		} catch (DbException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Device> queryAllOutDevices() {
		try {
			List<Device> list = db.findAll(Selector.from(Device.class).where(
				"type", "in", new int[] { 
					Device.TYPE_OUT, 
					Device.TYPE_GATEWAY, 
					Device.TYPE_COLOR_LAMP,
					Device.TYPE_OUT_WITH_IN,
					Device.TYPE_WINDOW }));
			return list;
		} catch (DbException e) {
			return null;
		}
	}

	@Override
	public List<Integer> getAllAddress() {
		List<Integer> list = new ArrayList<Integer>();
		try {
			List<DbModel> findDbModelAll = db.findDbModelAll(Selector.from(
					Device.class).groupBy("address"));
			if (findDbModelAll != null && findDbModelAll.size() > 0) {
				for (DbModel mod : findDbModelAll) {
					list.add(mod.getInt("address"));
				}
			}
		} catch (DbException e) {
			return list;
		}
		MainActivity.showString("查找到了地址" + list.size() + "个",
				MultiTextBuffer.TYPE_DEVICE);
		return list;
	}

	@Override
	public List<Device> getAllDevicesByAddress(int addr) {
		try {
			List<Device> list = db.findAll(Selector.from(Device.class).where(
					"address", "=", addr));
			return list;
		} catch (DbException e) {
			return null;
		}
	}

	@Override
	public void deleteDevice(Device dev) {
		try {
			MainActivity.showString("删除记录：" + dev.toString(),
					MultiTextBuffer.TYPE_DEVICE);
			db.delete(dev);
		} catch (DbException e) {
			MainActivity.showString("删除记录失败",
					MultiTextBuffer.TYPE_DEVICE);
		}
	}

	@Override
	public List<Device> getAllGateway() {
		try {
			List<Device> list = db.findAll(Selector.from(Device.class).where(
					"type", "number", new int[] { 12, 0 }));
			return list;
		} catch (DbException e) {
			return null;
		}
	}

	@Override
	public List<String> getAllAreaforDevice() {
		List<String> areas = new ArrayList<String>();
		try {
			List<DbModel> findDbModelAll = db.findDbModelAll(Selector.from(
					Device.class).groupBy("area"));
			if (findDbModelAll != null && findDbModelAll.size() > 0) {
				for (DbModel mod : findDbModelAll) {
					areas.add(mod.getString("area"));
				}
			}
		} catch (DbException e) {
			return null;
		}
		return areas;
	}
	
	@Override
	public boolean UpdateAreaAndNameOrDeleteForDevices(List<Device> list) {
		try {
			for (Device dtemp : list) {
				Device devicehad = db.findFirst(Selector.from(Device.class)
						.where("address", "=", dtemp.getAddress())
						.and("number", "=", dtemp.getNumber()));
				// 如果不存在，忽视之，已存在则更新
				if (devicehad == null) {
					// do nothing
				}else{
					if(dtemp.getArea().trim().equals("") 
							&& dtemp.getName().trim().equals("")){
						// 删除
						db.delete(devicehad);
						// 删除对应的关联表
						Device dev = new Device();
						dev.setAddress(dtemp.getAddress());
						dev.setNumber(dtemp.getNumber());
						RelationLinker.delRelation(dev,true);
						RelationLinker.delRelation(dev,false);
					} else {
						// 更新
						dtemp.setId(devicehad.getId());
						db.update(dtemp,UPDATE_AREA_NAME);
						MainActivity.showString("更新设备：" + dtemp.toString(),
								MultiTextBuffer.TYPE_DEVICE);
					}
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<Device> queryAllInDevices() {
		try {
			List<Device> list = db.findAll(Selector.from(Device.class).where(
				"type", "in", new int[] { 
					Device.TYPE_IN, Device.TYPE_OUT_WITH_IN }));
			return list;
		} catch (DbException e) {
			return null;
		}
	}

	@Override
	public Device getDeviceByAddrAndNumber(int addr, int number) {
		try {
			List<Device> list = db.findAll(Selector.from(Device.class).where(
					"address", "=", addr).and("number", "=", number));
			if(list != null && list.size() > 0){
				return list.get(0);
			} else {
				return null;
			}
		} catch (DbException e) {
			return null;
		}
	}

	@Override
	public List<Slave> getAllSlave() {
		List<Slave> list = new ArrayList<Slave>();
		
		try {
			List<DbModel> findDbModelAll = db.findDbModelAll(Selector.from(
					Device.class).groupBy("address"));
			if (findDbModelAll != null && findDbModelAll.size() > 0) {
				for (DbModel mod : findDbModelAll) {
					Slave slave = new Slave();
					slave.setAddr(mod.getInt("address"));
					slave.setType(0);
					list.add(slave);
				}
			}
			
			for(Slave slave:list){
				Device dev = db.findFirst(Selector.from(Device.class)
						.where("address", "=", slave.getAddr()));
				if(dev != null){
					slave.setType(dev.getType());
				}
			}
			
		} catch (DbException e) {
			return null;
		}
		
		return list;
	}

	@Override
	public void deleteSlave(int addr) {
		try {
			List<Device> list = db.findAll(
					Selector.from(Device.class).where("address", "=", addr));
			// 首先从关联表中移除
			for(Device dev:list){
				RelationLinker.delRelation(dev,true);
				RelationLinker.delRelation(dev,false);
			}
			// 最后从设备表中移除
			db.deleteAll(list);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

}
