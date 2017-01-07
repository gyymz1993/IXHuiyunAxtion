package com.huiyun.ixhuiyunaxtion.master.cmd;


import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;
import com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer;
import com.huiyun.ixhuiyunaxtion.master.bean.table.Device;
import com.huiyun.ixhuiyunaxtion.master.cmd.command.Data;
import com.huiyun.ixhuiyunaxtion.master.dao.DeviceDao;
import com.huiyun.ixhuiyunaxtion.master.dao.impl.DeviceDaoImpl;
import com.huiyun.ixhuiyunaxtion.master.utils.BytesStringUtils;
import com.huiyun.ixhuiyunaxtion.master.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 处理设备信息返回的串口命令
 * 
 * @author lzy_torah
 * 
 */
public class DeviceInfoProcessor {

	private final static int DEVICE_TYPE_GATEWAY = 1;
	private final static int DEVICE_TYPE_PANEL = 2;
	@SuppressWarnings("unused")
	private final static int DEVICE_TYPE_MASTER = 3;
	private final static int DEVICE_TYPE_RELAY = 4;
	private final static int DEVICE_TYPE_DRAFTFAN = 5;
	private final static int DEVICE_TYPE_INFRARED = 6;
	private final static int DEVICE_TYPE_COLORLAMP = 7;
	@SuppressWarnings("unused")
	private final static int DEVICE_TYPE_WTR = 9;
	private final static int DEVICE_TYPE_BODYINDUCTION = 10;
	
	// 返回所有的主机
	// 一个chipid对应的个数
	private static int count;

	// 设备信息为3-8位
	private static String details;
	private static Device device;
	private static DeviceDao dao = new DeviceDaoImpl(UIUtils.getContext());

	/**
	 * 查找设备返回后调用此方法分析
	 * 
	 * @param bytes
	 */
	public static void handleDeviceInfoCmd(byte[] data) {
		// 功能码必须对上
		if (BytesStringUtils.toUnsignedInt(data[Data.P_FUNC_CODE]) != Data.FUNC_CODE_SEARCH_RETURN)
			return;
		// 搜索到的设备的地址
		int address = (int) data[1] & 0xff;
		// 芯片ID
		byte[] chips = BytesStringUtils.subBytes(data, 9, 12);
		String chipId = BytesStringUtils.toStringShow(chips);
		// 设备信息 6byte
		/**
		 * 判断设备
		 */
		switch (BytesStringUtils.toUnsignedInt(data[3])) {
		case DEVICE_TYPE_GATEWAY:
			if (((int) data[4] & 0xff) == 1) {
				device = new Device();
				details = "网关(一代)";
				device.setChipId(chipId);
				device.setDetails(details);
				device.setCount(count);
				device.setAddress(address);
				device.setName(details);
				device.setType(Device.TYPE_OUT);
				device.setNumber(0);
				decomDeviceAndSaveSQL(device);
			} else if (((int) data[4] & 0xff) == 2) {
				device = new Device();
				details = "网关";
				device.setChipId(chipId);
				device.setDetails(details);
				device.setCount(count);
				device.setAddress(address);
				device.setName(details);
				device.setType(Device.TYPE_GATEWAY);
				device.setNumber(0);
				decomDeviceAndSaveSQL(device);
			}
			break;
		case DEVICE_TYPE_PANEL:
			if (((int) data[4] & 0xff) == 1) {
				device = new Device();
				details = "灯";
				device.setChipId(chipId);
				device.setDetails(details);
				device.setName(details);
				device.setCount(BytesStringUtils.toUnsignedInt(data[5]));
				device.setAddress(address);
				device.setType(Device.TYPE_OUT_WITH_IN);
				device.setNumber(0);
				decomDeviceAndSaveSQL(device);
			} else if (((int) data[4] & 0xff) == 2) {
				device = new Device();
				details = "开关";
				device.setChipId(chipId);
				device.setDetails(details);
				device.setName(details);
				device.setCount(BytesStringUtils.toUnsignedInt(data[5]));
				device.setAddress(address);
				device.setType(Device.TYPE_IN);
				device.setNumber(0);
				decomDeviceAndSaveSQL(device);
			} else if (((int) data[4] & 0xff) == 3) {
				device = new Device();
				details = "窗帘";
				device.setChipId(chipId);
				device.setDetails(details);
				device.setAddress(address);
				device.setName(details);
				device.setCount(2);
				device.setType(Device.TYPE_WINDOW);
				device.setNumber(0);
				decomDeviceAndSaveSQL(device);
			}
			break;
		case DEVICE_TYPE_RELAY:
			if (((int) data[4] & 0xff) == 1) {
				device = new Device();
				details = "继电器组";
				device.setChipId(chipId);
				device.setDetails(details);
				device.setCount(BytesStringUtils.toUnsignedInt(data[5]));
				device.setAddress(address);
				device.setName(details);
				device.setType(Device.TYPE_OUT);
				device.setNumber(0);
				decomDeviceAndSaveSQL(device);
			} else if (((int) data[4] & 0xff) == 2) {
				device = new Device();
				details = "惠芸执行器"; // RS485总线+RF433射频
				device.setChipId(chipId);
				device.setDetails(details);
				device.setCount(BytesStringUtils.toUnsignedInt(data[5]));
				device.setAddress(address);
				device.setName(details);
				device.setType(Device.TYPE_OUT);
				device.setNumber(0);
				decomDeviceAndSaveSQL(device);
			}
			break;
		case DEVICE_TYPE_DRAFTFAN:
			device = new Device();
			details = "抽风机";
			device.setChipId(chipId);
			device.setDetails(details);
			device.setAddress(address);
			device.setName(details);
			device.setType(Device.TYPE_OUT);
			device.setCount(3);
			device.setNumber(0);
			decomDeviceAndSaveSQL(device);
			break;
		case DEVICE_TYPE_INFRARED:
			device = new Device();
			details = "红外转发";
			device.setChipId(chipId);
			device.setDetails(details);
			device.setAddress(address);
			device.setName(details);
			device.setType(Device.TYPE_RED_RAY);
			device.setNumber(0);
			decomDeviceAndSaveSQL(device);
			break;
		case DEVICE_TYPE_COLORLAMP:
			device = new Device();
			details = "调色灯";
			device.setChipId(chipId);
			device.setDetails(details);
			device.setAddress(address);
			device.setName(details);
			device.setCount(4);
			device.setType(Device.TYPE_COLOR_LAMP);
			device.setNumber(0);
			decomDeviceAndSaveSQL(device);
			break;
		case DEVICE_TYPE_BODYINDUCTION:
			device = new Device();
			details = "热释红外人体感应";
			device.setChipId(chipId);
			device.setDetails(details);
			device.setAddress(address);
			device.setName(details);
			device.setCount(1);
			device.setType(Device.TYPE_BODY_INDUCTION);
			device.setNumber(0);
			decomDeviceAndSaveSQL(device);
			break;
		default:
			break;
		}
	}

	/**
	 * Function: 如果个数大于1将设备分开存入数据库
	 * 
	 * @author Yangshao 2015年1月8日 下午4:13:52
	 * @param d
	 * @return 成功返回true
	 */
	private static boolean decomDeviceAndSaveSQL(Device d) {
		// 真正存入数据库中的数据
		List<Device> insertDataBase = new ArrayList<Device>();
		if (d.getCount() > 1) {
			for (int j = 0; j < d.getCount(); j++) {
				/**
				 * 根据个数设置编号
				 */
				if (j == 0) {
					d.setNumber(j);
					insertDataBase.add(d);
				} else {
					device = new Device();
					device.setNumber(j);
					device.setChipId(d.getChipId());
					device.setAddress(d.getAddress());
					device.setType(d.getType());
					device.setName(d.getName());
					device.setDetails(d.getDetails());
					insertDataBase.add(device);
				}
			}
		} else {
			insertDataBase.add(d);
		}
		if (dao.saveOrUpdateDevices(insertDataBase)) {
			for (int i = 0; i < insertDataBase.size(); i++) {
				MainActivity.showString(insertDataBase.get(i).toString(),
						MultiTextBuffer.TYPE_DEVICE);
			}
			// 设置手机按键码
			PhoneCodeAllocator.allocatePhoneCode();
			return true;
		} else {
			return false;
		}
	}
}
