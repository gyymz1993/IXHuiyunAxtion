package com.huiyun.ixhuiyunaxtion.master.net.tcp;

import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;
import com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer;
import com.huiyun.ixhuiyunaxtion.master.inner.OnReceiveWTRCmdListener;
import com.huiyun.ixhuiyunaxtion.master.net.NetworkSupport;
import com.huiyun.ixhuiyunaxtion.master.net.thread.CommandProcessThread;
import com.huiyun.ixhuiyunaxtion.master.net.thread.PhoneSendThread;
import com.huiyun.ixhuiyunaxtion.master.utils.BytesStringUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


/**
 * 与射频中转器的通信
 * @author lzn
 * 
 */
public class TCPServerConnect {
	
	protected ServerSocket server;
	protected List<Socket> listSocket; // 所有正在进行连接的Socket
	protected OnReceiveWTRCmdListener listener;
	
	// 信息发送间隔时间
	public static final int COMMAND_SEND_INTERVAL = 200;
	
	public TCPServerConnect(int port){
		try {
			server = new ServerSocket(port);
			listSocket = new ArrayList<Socket>();
			// 建立一个线程不断监听tcp连接请求
			new Thread(){
				public void run() {
					System.out.println("创建TCPServerConnect线程，线程名为" + currentThread().getName());
					
					while(!server.isClosed()){
						// 不断监听是否有设备请求tcp连接
						Socket socket;
						try {
							socket = server.accept();
							listSocket.add(socket);
							MainActivity.showString("成功建立TCP连接，对方ip地址：" +
									socket.getInetAddress().getHostAddress() + 
									",端口为" + socket.getLocalPort(),
									MultiTextBuffer.TYPE_OTHER);
							// 每个socket连接都有自己的线程
							TCPConnect connect = new TCPConnect(socket,listSocket);
							connect.setOnReceiveCmdListener(new OnReceiveWTRCmdListener() {
								
								@Override
								public void onReceive(final int socketId, final int customCode, final byte[] bytes) {
									// 由命令处理线程处理
									CommandProcessThread.getInstance().getHandler().post(new Runnable() {
										
										@Override
										public void run() {
											if(listener != null){
												listener.onReceive(socketId, customCode, bytes);
											}
										}
									});
								}
							});
							connect.start();
						} catch (IOException e) {
							
						}
					}
				};
			}.start();
			
		} catch (IOException e) {

		}
	}
	
	protected TCPServerConnect(){
		
	}
	
	public void setOnReceiveListener(OnReceiveWTRCmdListener listener){
		this.listener = listener;
	}
	
	/**
	 * 发送命令
	 * @param socketId
	 * @param cmd
	 */
	public void send(final int socketId, final byte[] cmd){
		// 在命令发送线程发送命令
		PhoneSendThread.getInstance().getHandler().post(new Runnable(){
			
			@Override
			public void run() {
				synchronized(TCPJsonServerConnect.class){
					// 需要用一个临时socket列表来遍历
					List<Socket> listSocketTemp = new ArrayList<Socket>();
					for(Socket socket:listSocket){
						listSocketTemp.add(socket);
					}
					for(Socket socket:listSocketTemp){
						// socket不可用
						if(socket == null || socket.isClosed())
							continue;
						// 如果id相同
						if(NetworkSupport.getSocketId(socket) == socketId){
							try {
								socket.getOutputStream().write(cmd);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			};
		});
	}
	
	/**
	 * 向所有客户端发送命令
	 * @param cmd
	 */
	public void sendToAll(final byte[] cmd){
		// 在命令发送线程发送命令
		PhoneSendThread.getInstance().getHandler().post(new Runnable(){
			
			@Override
			public void run() {
				synchronized(TCPJsonServerConnect.class){
					// 需要用一个临时socket列表来遍历
					List<Socket> listSocketTemp = new ArrayList<Socket>();
					for(Socket socket:listSocket){
						listSocketTemp.add(socket);
					}
					for(Socket socket:listSocketTemp){
						try {
							if(socket != null && !socket.isClosed()){
								MainActivity.showString(
										"ip地址为" + socket.getPort() + 
										"、端口48900发送打包后的命令"
										+ BytesStringUtils.toStringShow(cmd),
										MultiTextBuffer.TYPE_SERIALPORT);
								socket.getOutputStream().write(cmd);
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
	}
}
