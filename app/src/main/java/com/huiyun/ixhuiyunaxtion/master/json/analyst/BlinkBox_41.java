package com.huiyun.ixhuiyunaxtion.master.json.analyst;

import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;
import com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer;
import com.huiyun.ixhuiyunaxtion.master.bean.json.BaseJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.json.DataJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.table.Device;
import com.huiyun.ixhuiyunaxtion.master.cmd.CmdSender;
import com.huiyun.ixhuiyunaxtion.master.dao.DeviceDao;
import com.huiyun.ixhuiyunaxtion.master.dao.impl.DeviceDaoImpl;
import com.huiyun.ixhuiyunaxtion.master.net.TokenManager;
import com.huiyun.ixhuiyunaxtion.master.utils.UIUtils;

import java.util.List;
import java.util.Map;



/**
 * @Description: 处理让设备闪烁的json命令
 * @date 2015-1-21 下午2:58:12
 * @version V1.0
 */
public class BlinkBox_41 extends BaseAnalyst {

	private final int BLINK_BOX = 41;
	private DeviceDao devDao = new DeviceDaoImpl(UIUtils.getContext());

	@Override
	public void handleData(int socketId, BaseJsonObj jsonObj) {
		try {
			if (OBJ_MASTER.equals(jsonObj.getObj())
					&& BLINK_BOX == jsonObj.getCode()) {
				blinkBox(socketId, jsonObj);
			}
		} catch (Exception e) {
			e.printStackTrace();
			MainActivity.showString("请检查传入参数", MultiTextBuffer.TYPE_OTHER);
		}
	}

	private void blinkBox(int socketId, BaseJsonObj jsonObj) {
		if (jsonObj instanceof DataJsonObj) {
			DataJsonObj result_jsonObj = (DataJsonObj) jsonObj;
			Map<String, String> resultMap = result_jsonObj.getData();
			try {
				MainActivity.showString("让指定设备闪烁", MultiTextBuffer.TYPE_OTHER);
				if (TokenManager.judgetToken(resultMap.get("token").toString()) != null) {
					int address = Integer.valueOf(resultMap.get("address"));
					List<Device> allDev = devDao
							.getAllDevicesByAddress(address);
					if (allDev != null && allDev.size() > 0) {
						byte[] chipId = allDev.get(0).getChipIds();
						CmdSender.blinkBox(chipId);
						MainActivity.showString("让芯片ID为"
								+ allDev.get(0).getChipId() + "的设备闪烁",
								MultiTextBuffer.TYPE_DEVICE);
					}
				}
			} catch (NullPointerException e) {
				e.printStackTrace();
				MainActivity.showString("请检查传入参数", MultiTextBuffer.TYPE_OTHER);
			} catch (Exception e) {
				e.printStackTrace();
				MainActivity.showString("请检查传入参数", MultiTextBuffer.TYPE_OTHER);
			}
		}
	}
}
