package com.huiyun.ixhuiyunaxtion.master.json.analyst;

import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;
import com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer;
import com.huiyun.ixhuiyunaxtion.master.bean.json.BaseJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.json.DataJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.json.DownJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.table.Webcam;
import com.huiyun.ixhuiyunaxtion.master.dao.WebCamDao;
import com.huiyun.ixhuiyunaxtion.master.dao.impl.WebCamDaoImpl;
import com.huiyun.ixhuiyunaxtion.master.net.TokenManager;
import com.huiyun.ixhuiyunaxtion.master.net.tcp.TcpConnectionManager;
import com.huiyun.ixhuiyunaxtion.master.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;



/**
 * @Description: 上传摄像头
 * @date 2015年2月6日 下午4:01:16
 * @version V1.0
 */
public class UploadWebcam_61 extends BaseAnalyst {

	private final int DOWN_WEBCAM = 61;
	private final int RE_DOWN_WEBCAM = 62;
	private WebCamDao camDao = new WebCamDaoImpl(UIUtils.getContext());

	@Override
	public void handleData(int socketId, BaseJsonObj jsonObj) {
		try {
			if (OBJ_MASTER.equals(jsonObj.getObj())
					&& DOWN_WEBCAM == jsonObj.getCode()) {
				uploadWebcam(socketId, jsonObj);
			}
		} catch (Exception e) {
			e.printStackTrace();
			MainActivity.showString("请检查传入参数", MultiTextBuffer.TYPE_OTHER);
		}

	}

	/**
	 * Function:
	 * 
	 * @author Yangshao 2015年1月19日 下午6:04:26
	 * @param socketId
	 * @param jsonObj
	 */
	private void uploadWebcam(int socketId, BaseJsonObj jsonObj) {
		if (jsonObj instanceof DownJsonObj) {
			final DownJsonObj downJsonObj = (DownJsonObj) jsonObj;
			DownJsonObj.Data resultMap = downJsonObj.getData();

			final DataJsonObj send_jsonObj = new DataJsonObj();
			send_jsonObj.setCode(RE_DOWN_WEBCAM);
			send_jsonObj.setObj(OBJ_PHONE);
			try {

				if (TokenManager.judgetToken(resultMap.token.toString()) == null) {
					send_jsonObj.setResult(RE_TOKEN_ERROR);
				} else {
					List<Map<String, String>> keymaps = downJsonObj.data.list;
					List<Webcam> webcams = new ArrayList<Webcam>();
					for (Map<String, String> map : keymaps) {
						Webcam webcam = new Webcam();
						webcam.setDid(map.get("did"));
						webcam.setArea(map.get("area"));
						webcams.add(webcam);
					}
					if (camDao.addAndUpdateWebCam(webcams)) {
						send_jsonObj.setResult(RE_RESULT_SUCCESS);
					} else {
						send_jsonObj.setResult(RE_ERROR);
					}
				}
				TcpConnectionManager.getInstance().sendJson(socketId, send_jsonObj);
			} catch (Exception e) {
				e.printStackTrace();
				MainActivity.showString("请检查传入参数",MultiTextBuffer.TYPE_OTHER);
				send_jsonObj.setResult(RE_ERROR);
				TcpConnectionManager.getInstance().sendJson(socketId, send_jsonObj);
			}
		}
	}

}
