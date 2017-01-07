package com.huiyun.ixhuiyunaxtion.master.json.analyst;

import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;
import com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer;
import com.huiyun.ixhuiyunaxtion.master.bean.GatewayConfigure;
import com.huiyun.ixhuiyunaxtion.master.bean.json.BaseJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.json.DataJsonObj;
import com.huiyun.ixhuiyunaxtion.master.cmd.GatewayProcessor;
import com.huiyun.ixhuiyunaxtion.master.inner.OnResultListener;
import com.huiyun.ixhuiyunaxtion.master.net.TokenManager;
import com.huiyun.ixhuiyunaxtion.master.net.tcp.TcpConnectionManager;

import java.util.Map;


/**   
* @Package com.huiyun.ixconfig.net.jsonMod 
* @Description: 设置网关
* @author lzn  
* @date 2015年1月15日 下午4:25:13 
* @version V1.0   
*/
public class SettingGatewayResult_33 extends BaseAnalyst{
	private final int SET_GATEWAY=33;
	private final int RE_SET_GATEWAY=34;
	
	@Override
	public void handleData(int socketId, BaseJsonObj jsonObj) {
		try {
			if (OBJ_MASTER.equals(jsonObj.getObj())
					&& SET_GATEWAY == jsonObj.getCode()) {
				setGateWay(jsonObj, socketId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			MainActivity.showString("请检查大参数", MultiTextBuffer.TYPE_OTHER);
		}
	}
	
	 /**
	 *  Function:
	 *  @author Yangshao 
	 *  2015年1月21日 上午9:34:26
	 *  @param jsonObj
	 *  @param socketId
	 */
	private void setGateWay(BaseJsonObj jsonObj, final int socketId) {
		if (jsonObj instanceof DataJsonObj) {
			DataJsonObj result_jsonObj = (DataJsonObj) jsonObj;
			final Map<String, String> resultMap = result_jsonObj.getData();
			final DataJsonObj send_jsonObj = new DataJsonObj();
			send_jsonObj.setCode(RE_SET_GATEWAY);
			send_jsonObj.setObj(OBJ_PHONE);
			try {
				if (TokenManager.judgetToken(resultMap.get("token").toString()) == null) {
					send_jsonObj.setResult(RE_TOKEN_ERROR);
					TcpConnectionManager.getInstance().sendJson(socketId, send_jsonObj);
				}else{
					resultMap.remove("token");
					// 写入网关配置
					GatewayConfigure config = new GatewayConfigure();
					String details = resultMap.get("details");
					MainActivity.showString("网关配置里，detials为" + (details==null?"":details),
							MultiTextBuffer.TYPE_OTHER);
					config.setDetails(resultMap.get("details"));
					config.setName(resultMap.get("name"));
					config.setAddress(Integer.valueOf(resultMap.get("address")));
					config.setCommType(Integer.valueOf(resultMap.get("comm_type")));
					config.setDetailType(Integer.valueOf(resultMap.get("details_type")));
					config.setExecId(Integer.valueOf(resultMap.get("exec_id")));
					config.setSwitchId(Integer.valueOf(resultMap.get("switch_id")));
					if(config.getCommType() == 0 && config.getDetailType() == 0 &&
						config.getExecId() == 0 && config.getSwitchId() == 0){
						// 清空配置
						GatewayProcessor.getInstance().setOnResultListener(
								new OnResultListener<GatewayConfigure>() {
							
							@Override
							public void onResult(boolean isSucceed, GatewayConfigure obj) {
								if(isSucceed){
									send_jsonObj.setResult(RE_RESULT_SUCCESS);
									TcpConnectionManager.getInstance().sendJson(socketId, send_jsonObj);
									GatewayProcessor.getInstance().setOnResultListener(null);
								}
							}
						});
						GatewayProcessor.getInstance().cleanConfigure(config.getAddress());
					} else {
						// 添加配置
						GatewayProcessor.getInstance().setOnResultListener(
								new OnResultListener<GatewayConfigure>() {
							
							@Override
							public void onResult(boolean isSucceed, GatewayConfigure obj) {
								if(isSucceed){
									send_jsonObj.setResult(RE_RESULT_SUCCESS);
									TcpConnectionManager.getInstance().sendJson(socketId, send_jsonObj);
									GatewayProcessor.getInstance().setOnResultListener(null);
								}
							}
						});
						GatewayProcessor.getInstance().addConfigure(config);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				MainActivity.showString("请检查传入参数",MultiTextBuffer.TYPE_OTHER);
				send_jsonObj.setResult(RE_ERROR);
				TcpConnectionManager.getInstance().sendJson(socketId, send_jsonObj);
			}
		}
	}

}
