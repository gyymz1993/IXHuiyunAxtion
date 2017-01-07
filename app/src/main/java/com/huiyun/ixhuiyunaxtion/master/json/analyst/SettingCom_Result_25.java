package com.huiyun.ixhuiyunaxtion.master.json.analyst;

import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;
import com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer;
import com.huiyun.ixhuiyunaxtion.master.bean.json.BaseJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.json.DataJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.json.DownJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.table.Device;
import com.huiyun.ixhuiyunaxtion.master.net.RelationLinker;
import com.huiyun.ixhuiyunaxtion.master.net.TokenManager;
import com.huiyun.ixhuiyunaxtion.master.net.tcp.TcpConnectionManager;

import java.util.List;
import java.util.Map;


/**   
 * @Description: 设置组合
 * @date 2015年1月21日 上午9:30:36 
 * @version V1.0   
 */
public class SettingCom_Result_25 extends BaseAnalyst {

	private final int SET_COM=25;
	private final int RE_SET_COM=26;
	
	@Override
	public void handleData(int socketId, BaseJsonObj jsonObj) {
		try {
			if (OBJ_MASTER.equals(jsonObj.getObj())
					&& SET_COM == jsonObj.getCode()) {
				setCom(jsonObj, socketId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			MainActivity.showString("请检查大参数", MultiTextBuffer.TYPE_OTHER);
		}
	}
	
	 /**
	 *  Function:
	 *  @author Yangshao 
	 *  2015年1月21日 上午9:34:55
	 *  @param jsonObj
	 *  @param socketId
	 */
	private void setCom(BaseJsonObj jsonObj, int socketId) {
		if (jsonObj instanceof DownJsonObj) {
			// 得到数据
			final DownJsonObj downJsonObj=(DownJsonObj) jsonObj;
			DownJsonObj.Data resultMap=downJsonObj.getData();
			
			// 生成返回的命令
			final DataJsonObj send_jsonObj = new DataJsonObj();
			send_jsonObj.setCode(RE_SET_COM);
			send_jsonObj.setObj(OBJ_PHONE);
			try {
				if (TokenManager.judgetToken(resultMap.token.toString()) == null) {
					// token错误
					send_jsonObj.setResult(RE_TOKEN_ERROR);
				} else {
					// 对数据进行处理
					Device in,out;
					int inAddr,inNum,outAddr,outNum,type;
					String sceneName;
					List<Map<String, String>> maplist= downJsonObj.data.list;
					if(maplist != null){
						for(Map<String,String> map:maplist){
							inAddr = Integer.valueOf(map.get("inAddr"));
							inNum = Integer.valueOf(map.get("inNum"));
							in = new Device();
							in.setAddress(inAddr);
							in.setNumber(inNum);
							type = Integer.valueOf(map.get("type"));
							switch(type){
							case 0: // 删除关联
								RelationLinker.delRelation(in,true);
								break;
							case 1: // 输入-输出关联
								outAddr = Integer.valueOf(map.get("outAddr"));
								outNum = Integer.valueOf(map.get("outNum"));
								out = new Device();
								out.setAddress(outAddr);
								out.setNumber(outNum);
								RelationLinker.linkRelation(in, out);
								break;
							case 2: // 输入-场景关联
								sceneName = map.get("sceneName");
								RelationLinker.linkSceneRelation(in, sceneName);
								break;
							}
							
						}
					}
					RelationLinker.print();
					// 成功
					send_jsonObj.setResult(RE_RESULT_SUCCESS);
				}
				
				// 返回
				TcpConnectionManager.getInstance().sendJson(socketId, send_jsonObj);
				
			} catch (NullPointerException e) {
				e.printStackTrace();
				// 参数错误
				MainActivity.showString("请检查传入参数",MultiTextBuffer.TYPE_OTHER);
				send_jsonObj.setResult(RE_ERROR);
				TcpConnectionManager.getInstance().sendJson(socketId, send_jsonObj);
			}
		}
	}

}
