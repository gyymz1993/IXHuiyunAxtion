package com.huiyun.ixhuiyunaxtion.master.net.tcp;

import com.google.gson.Gson;
import com.huiyun.ixhuiyunaxtion.master.StaticValues;
import com.huiyun.ixhuiyunaxtion.master.bean.json.BaseJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.json.DataJsonObj;
import com.huiyun.ixhuiyunaxtion.master.inner.OnReceiveCmdListener;
import com.huiyun.ixhuiyunaxtion.master.inner.OnReceiveWTRCmdListener;
import com.huiyun.ixhuiyunaxtion.master.json.JsonUtil;
import com.huiyun.ixhuiyunaxtion.master.net.NetworkSupport;


public class TcpConnectionManager {

	private OnReceiveCmdListener jsonListener;
	private OnReceiveWTRCmdListener cmdListener;
	private TCPServerConnect tcpCnt00;
	private TCPJsonServerConnect tcpCnt01;
	
	private static TcpConnectionManager instance;
	/**
	 * 单例模式
	 * @return
	 */
	public static TcpConnectionManager getInstance(){
		synchronized(TcpConnectionManager.class){
			if(instance == null){
				instance = new TcpConnectionManager();
			}
			return instance;
		}
	}
	
	private TcpConnectionManager(){
		tcpCnt00 = new TCPServerConnect(NetworkSupport.TCP_PORT);
		tcpCnt01 = new TCPJsonServerConnect(NetworkSupport.TCP_PORT_2);
		// 48900端口的监听(与射频中转器的通信)
		tcpCnt00.setOnReceiveListener(new OnReceiveWTRCmdListener() {
			
			@Override
			public void onReceive(int socketId, int customCode, byte[] bytes) {
				if(cmdListener != null){
					cmdListener.onReceive(socketId, customCode, bytes);
				}
			}
		});
		// 48901端口的监听(与手机的通信)
		tcpCnt01.setOnReceiveListener(new OnReceiveCmdListener() {
			
			@Override
			public void onReceive(int socketId, byte[] bytes) {
				if(jsonListener != null){
					jsonListener.onReceive(socketId, bytes);
				}
			}
		});
		// 服务器8989端口的监听
		TCPClientConnect.getInstance().setOnReceiveListener(
				new OnReceiveCmdListener() {

					@Override
					public void onReceive(int socketId, byte[] bytes) {
						if(jsonListener != null){
							jsonListener.onReceive(socketId, bytes);
						}
					}
				});
	}
	
	Gson gson=new Gson();
	/** 发送json命令
	 * @param myJsonObj
	 */
	public void sendJson(int socketId, BaseJsonObj myJsonObj){
		byte[] bytes = JsonUtil.jsonTobytes(myJsonObj);
		tcpCnt01.send(socketId, bytes);
		
		TCPClientConnect.getInstance().send(socketId, myJsonObj);
	}
	
	/** 向所有客户端发送json命令(也包括向服务器发送)
	 * @param myJsonObj
	 */
	public void sendJsonAll(BaseJsonObj myJsonObj){
		byte[] bytes = JsonUtil.jsonTobytes(myJsonObj);
		tcpCnt01.sendToAll(bytes);
		
		if(myJsonObj instanceof DataJsonObj){
			DataJsonObj myDataJsonObj = (DataJsonObj) myJsonObj;
			if(myDataJsonObj.data != null){
				myDataJsonObj.data.put("token", StaticValues.SERVER_TOKEN);
			} 
			TCPClientConnect.getInstance().send(myDataJsonObj);
		} else {
			TCPClientConnect.getInstance().send(myJsonObj);
		}
		
	}
	
	/** 向所有客户端发送json命令
	 * @param myJsonObj
	 */
	public void sendJsonAllWithoutServer(BaseJsonObj myJsonObj){
		byte[] bytes = JsonUtil.jsonTobytes(myJsonObj);
		tcpCnt01.sendToAll(bytes);
	}
	
	/** 给所有WTR发送命令
	 * @param bytes
	 */
	public void sendWTRCmdAll(byte[] bytes){
		tcpCnt00.sendToAll(bytes);
	}
	
	/** 给指定WTR发送命令
	 * @param bytes
	 */
	public void sendWTRCmd(int socketId, byte[] bytes){
		tcpCnt00.send(socketId, bytes);
	}
	
	/**设置收到json命令的监听
	 * @param listener
	 */
	public void setPhoneJsonRecievedListener(OnReceiveCmdListener listener){
		jsonListener = listener;
	}
	/**设置收到射频中转器发过来的命令的监听
	 * @param listener
	 */
	public void setWTRRecievedListener(OnReceiveWTRCmdListener listener){
		cmdListener = listener;
	}
	
}
