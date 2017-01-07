package com.huiyun.ixhuiyunaxtion.master.net.tcp;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;



import android.os.SystemClock;

import com.huiyun.ixhuiyunaxtion.master.StaticValues;
import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;
import com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer;
import com.huiyun.ixhuiyunaxtion.master.cmd.command.Data;
import com.huiyun.ixhuiyunaxtion.master.cmd.command.JsonPack;
import com.huiyun.ixhuiyunaxtion.master.cmd.container.CommandBuffer;
import com.huiyun.ixhuiyunaxtion.master.cmd.container.JsonCommandBuffer;
import com.huiyun.ixhuiyunaxtion.master.cmd.container.PackContainer;
import com.huiyun.ixhuiyunaxtion.master.inner.OnReceiveCmdListener;
import com.huiyun.ixhuiyunaxtion.master.json.JsonSendData;
import com.huiyun.ixhuiyunaxtion.master.net.NetworkSupport;

/**
 * 与服务器端的通信的信息接收
 *
 */
public class TCPClientReceive extends Thread{
	private Socket socket;
	private OnReceiveCmdListener listener;
	
	private final static int RESTART_MINUTE = 1;
	
	public TCPClientReceive(){
		socket = new Socket();
	}
	
	@Override
	public void run() {
		try {
			System.out.println("创建与服务器连接的线程，线程名为" + currentThread().getName());
			// 建立连接
			SocketAddress addr = new InetSocketAddress(
					StaticValues.SERVER_IP_ADDRESS, NetworkSupport.TCP_CLIENT_PORT);
			socket.connect(addr, 5000);
			MainActivity.showString("与服务器连接成功，服务器地址" + StaticValues.SERVER_IP_ADDRESS,
					MultiTextBuffer.TYPE_OTHER);
			
			int lenRead;
			byte[] read, pack, cmd;
			read = new byte[Data.MAX_RECEIVE_LENGTH];
			JsonCommandBuffer buffer = new JsonCommandBuffer(Data.BUFFER_CAPACITY);
			PackContainer container = new PackContainer();
			BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
			
			// 上传信息给服务器
			new Thread(){
				public void run() {
					JsonSendData data = new JsonSendData();
					
					// 发送登陆信息
					MainActivity.showString("准备发送远程登录信息",MultiTextBuffer.TYPE_OTHER);
					data.masterLogin(NetworkSupport.getSocketId(socket));
				};
			}.start();
			
			while(socket != null && !socket.isClosed()){
				// 接收数据
				//lenRead = socket.getInputStream().read(read);
				
				lenRead = bis.read(read);
				
				if(lenRead <= 0) break;
				
				// 将接受到的数据添进缓存
				buffer.importByte(read, lenRead);
				
				// 从缓存中提取命令，放到容器中
				while((pack=buffer.exportPack()) != null){
					container.add(pack);
				}
				
				// 从容器中逐个提取指令并处理，最后清空容器
				for(byte[] b:container.getContainer()){
					// 解包
					if(!JsonPack.Check_CustomCode(b, Data.CUSTOM_CODE_RF)) continue;
					cmd = JsonPack.Data_Unpack(b);
					if(cmd == null) continue;
					
					// 对解包后的命令进行处理
					if(listener != null){
						listener.onReceive(NetworkSupport.getSocketId(socket), cmd);
					}
				}
				container.clear();
				
				// 检查缓存的状态，处理异常
				switch(buffer.check()){
				case CommandBuffer.RESULT_ERROR_DATA:
					buffer.clean();
					break;
				}
			}
			
		} catch (IOException e) {
			
		} finally {
			MainActivity.showString("与服务器的连接断开",MultiTextBuffer.TYPE_OTHER);
			try {
				socket.close();
			} catch (IOException e) {

			} finally {
				System.out.println("关闭与服务器连接的线程，线程名为" + currentThread().getName());
			}
		}
	}
	
	public void setOnReceiveListener(OnReceiveCmdListener listener){
		this.listener = listener;
	}
	
	public Socket getSocket(){
		return socket;
	}
	
	/**
	 * 重新进行连接
	 * @param socket
	 * @param handler
	 */
	@SuppressWarnings("unused")
	private void restart(final Socket socket){
		new Thread(){
			@Override
			public void run() {
				SystemClock.sleep(1000*60*RESTART_MINUTE);
				if(listener != null){
					// 请求重新连接
					listener.onReceive(-1, null);
				}
			}
		}.start();
	}
}
