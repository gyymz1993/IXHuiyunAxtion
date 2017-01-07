package com.huiyun.ixhuiyunaxtion.master.net.tcp;

import com.huiyun.ixhuiyunaxtion.axtion.serialport.SerialPortConnect;
import com.huiyun.ixhuiyunaxtion.master.bean.json.BaseJsonObj;
import com.huiyun.ixhuiyunaxtion.master.cmd.command.Data;
import com.huiyun.ixhuiyunaxtion.master.cmd.command.JsonPack;
import com.huiyun.ixhuiyunaxtion.master.inner.OnReceiveCmdListener;
import com.huiyun.ixhuiyunaxtion.master.json.JsonUtil;
import com.huiyun.ixhuiyunaxtion.master.net.NetworkSupport;
import com.huiyun.ixhuiyunaxtion.master.net.thread.CommandProcessThread;
import com.huiyun.ixhuiyunaxtion.master.net.thread.ServerSendThread;

import java.io.IOException;
import java.net.Socket;


/**
 * 与服务器端的通信
 * @author lzn
 *
 */
public class TCPClientConnect {
	private OnReceiveCmdListener listener;
	private TCPClientReceive receive;
	
	/**
	 * 开始与服务器的TCP通信连接
	 */
	public static void startTCPClientConnect(){
		TCPClientConnect.getInstance();
	}
	
	private static TCPClientConnect instance;
	/**
	 * 单例模式
	 */
	public static TCPClientConnect getInstance(){
		synchronized(SerialPortConnect.class){
			if(instance == null){
				instance = new TCPClientConnect();
			}
			
			return instance;
		}
	}
	
	private TCPClientConnect(){
		startClientReceive();
	}
	
	
	 /**
	 *  Function:开启接收线程
	 *  @author lzn 
	 *  2015-2-12 上午10:21:08
	 */
	public void startClientReceive(){
		receive = null;
		receive = new TCPClientReceive();
		receive.setOnReceiveListener(new OnReceiveCmdListener() {

			@Override
			public void onReceive(final int socketId, final byte[] bytes) {
				if (listener != null) {
					if (bytes != null) {
						// 由命令处理线程处理
						CommandProcessThread.getInstance().getHandler()
								.post(new Runnable() {

									@Override
									public void run() {
										listener.onReceive(socketId, bytes);
									}
								});
					} else {
						startClientReceive();
					}
				}
			}
		});
		receive.start();
	}
	
	/**
	 *  Function:断掉旧的接收线程，重新开启接收线程
	 *  @author lzn 
	 *  2015-2-12 上午10:21:08
	 */
	public void restartClientReceive(){
		new Thread(){
			public void run() {
				try {
					if(receive.getSocket() != null && !receive.getSocket().isClosed()){
						receive.getSocket().close();
					}
					receive = null;
					receive = new TCPClientReceive();
					receive.setOnReceiveListener(new OnReceiveCmdListener() {

						@Override
						public void onReceive(final int socketId, final byte[] bytes) {
							if (listener != null) {
								if (bytes != null) {
									// 由命令处理线程处理
									CommandProcessThread.getInstance().getHandler()
											.post(new Runnable() {

												@Override
												public void run() {
													listener.onReceive(socketId, bytes);
												}
											});
								} else {
									startClientReceive();
								}
							}
						}
					});
					receive.start();
				} catch (IOException e) {
					e.printStackTrace();
				}
			};
		}.start();
	}
	
	public void setOnReceiveListener(OnReceiveCmdListener listener){
		this.listener = listener;
	}
	
	public void send(int socketId, final BaseJsonObj myJsonObj){
		if(NetworkSupport.getSocketId(receive.getSocket()) == socketId){
			final Socket socket = receive.getSocket();
			// 让命令发送线程处理
			ServerSendThread.getInstance().getHandler().post(new Runnable() {
				
				@Override
				public void run() {
					if(socket == null || socket.isClosed())
						return;
					try {
						myJsonObj.obj = "server";
						byte[] write = JsonPack.Data_Pack(
								Data.CUSTOM_CODE_RF, JsonUtil.jsonTobytes(myJsonObj));
						socket.getOutputStream().write(write);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
		}
	}
	
	public void send(final BaseJsonObj myJsonObj){
		
		final Socket socket = receive.getSocket();
		
		// 让命令发送线程处理
		ServerSendThread.getInstance().getHandler().post(new Runnable() {
			
			@Override
			public void run() {
				if(socket == null || socket.isClosed())
					return;
				try {
					myJsonObj.obj = "server";
					byte[] write = JsonPack.Data_Pack(
							Data.CUSTOM_CODE_RF, JsonUtil.jsonTobytes(myJsonObj));
					socket.getOutputStream().write(write);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public Socket getSocket(){
		if(receive != null)
			return  receive.getSocket();
		else
			return null;
	}
}
