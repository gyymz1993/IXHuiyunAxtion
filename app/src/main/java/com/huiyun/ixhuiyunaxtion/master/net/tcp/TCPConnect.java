package com.huiyun.ixhuiyunaxtion.master.net.tcp;

import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;
import com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer;
import com.huiyun.ixhuiyunaxtion.master.cmd.command.Data;
import com.huiyun.ixhuiyunaxtion.master.cmd.command.Pack;
import com.huiyun.ixhuiyunaxtion.master.cmd.container.CommandBuffer;
import com.huiyun.ixhuiyunaxtion.master.cmd.container.PackContainer;
import com.huiyun.ixhuiyunaxtion.master.inner.OnReceiveWTRCmdListener;
import com.huiyun.ixhuiyunaxtion.master.net.NetworkSupport;
import com.huiyun.ixhuiyunaxtion.master.net.WTR.WTRCommandSender;
import com.huiyun.ixhuiyunaxtion.master.utils.BytesStringUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;





public class TCPConnect extends Thread{

	protected Socket socket;
	protected List<Socket> listSocket;
	protected OnReceiveWTRCmdListener listener;
	
	public TCPConnect(final Socket socket, final List<Socket> listSocket){
		this.socket = socket;
		this.listSocket = listSocket;
	}
	
	@Override
	public void run() {
		try {
			System.out.println("创建TCPConnect线程，线程名为" + currentThread().getName());
			
			int lenRead;
			byte[] read, pack, cmd;
			read = new byte[Data.MAX_RECEIVE_LENGTH];
			CommandBuffer buffer = new CommandBuffer(Data.BUFFER_CAPACITY);
			PackContainer container = new PackContainer();
			BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
			
			// 每当有射频中转器刚连接上时，都会尝试去读取其设定
			WTRCommandSender.readSetting(NetworkSupport.getSocketId(socket));
			
			// 当socket一直不关闭时
			while(!socket.isClosed()){
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
					int customCode = b[1];
					cmd = Pack.Data_Unpack(b);
					if(cmd == null) continue;
					// 对解包后的命令进行处理
					MainActivity.showString("端口" + socket.getLocalPort() + 
							"收到命令" + BytesStringUtils.toStringShow(cmd),
							MultiTextBuffer.TYPE_SERIALPORT);
					if(listener != null){
						listener.onReceive(
							NetworkSupport.getSocketId(socket), customCode, cmd);
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
			
			// 从socket列表中移除
			listSocket.remove(socket);
			MainActivity.showString("与" + socket.getInetAddress().getHostAddress() + "的端口为" +
					socket.getLocalPort() + "的TCP连接已断开",
					MultiTextBuffer.TYPE_OTHER);
			
		} catch (IOException e) {
			// 从socket列表中移除
			listSocket.remove(socket);
			MainActivity.showString("与" + socket.getInetAddress().getHostAddress() + "的端口为" +
					socket.getLocalPort() + "的TCP连接已断开",
					MultiTextBuffer.TYPE_OTHER);
		}
	}
	
	public void setOnReceiveCmdListener(OnReceiveWTRCmdListener listener){
		this.listener = listener;
	}
}
