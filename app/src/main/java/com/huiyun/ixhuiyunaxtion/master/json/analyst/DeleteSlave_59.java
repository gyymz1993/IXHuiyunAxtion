package com.huiyun.ixhuiyunaxtion.master.json.analyst;

import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;
import com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer;
import com.huiyun.ixhuiyunaxtion.master.bean.json.BaseJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.json.DataJsonObj;
import com.huiyun.ixhuiyunaxtion.master.dao.DeviceDao;
import com.huiyun.ixhuiyunaxtion.master.dao.impl.DeviceDaoImpl;
import com.huiyun.ixhuiyunaxtion.master.net.TokenManager;
import com.huiyun.ixhuiyunaxtion.master.net.tcp.TcpConnectionManager;
import com.huiyun.ixhuiyunaxtion.master.utils.UIUtils;

import java.util.Map;


/**   
 * @Description: 处理删除从机的json命令 
 */
public class DeleteSlave_59 extends BaseAnalyst {
	
	private final int DEL_SLAVE = 59;
	private final int DEL_SLAVE_RE = 60;
	private DeviceDao devDao = new DeviceDaoImpl(UIUtils.getContext());
	
	@Override
	public void handleData(int socketId, BaseJsonObj jsonObj) {
		try {
			// 判断Code码
			if (OBJ_MASTER.equals(jsonObj.getObj())
					&& DEL_SLAVE == jsonObj.getCode()) {
				delSlave(socketId, jsonObj);
			}
		} catch (Exception e) {
			e.printStackTrace();
			MainActivity.showString("请检查传入参数", MultiTextBuffer.TYPE_OTHER);
		}
	}
	
	private void delSlave(int socketId, BaseJsonObj jsonObj) {
		if (jsonObj instanceof DataJsonObj) {
			DataJsonObj result_jsonObj = (DataJsonObj) jsonObj;
			Map<String, String> resultMap = result_jsonObj.getData();
			// 要返回的信息
			final DataJsonObj send_jsonObj = new DataJsonObj();
			send_jsonObj.setCode(DEL_SLAVE_RE);
			send_jsonObj.setObj(OBJ_PHONE);
			try {
				MainActivity.showString("删除从机", MultiTextBuffer.TYPE_OTHER);
				// 判断token
				if (TokenManager.judgetToken(resultMap.get("token").toString()) != null) {
					// 获取要删除的从机的地址
					int addr = Integer.parseInt(resultMap.get("address"));
					// 执行删除操作
					devDao.deleteSlave(addr);
					// 返回操作成功的json命令
					send_jsonObj.setResult(RE_RESULT_SUCCESS);
					TcpConnectionManager.getInstance().sendJson(socketId, send_jsonObj);
				} else {
					// token错误
					send_jsonObj.setResult(RE_TOKEN_ERROR);
					TcpConnectionManager.getInstance().sendJson(socketId, send_jsonObj);
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
