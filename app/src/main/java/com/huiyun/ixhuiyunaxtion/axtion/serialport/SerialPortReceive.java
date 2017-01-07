package com.huiyun.ixhuiyunaxtion.axtion.serialport;

import java.io.FileInputStream;
import java.io.IOException;


import android.util.Log;

import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;
import com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer;
import com.huiyun.ixhuiyunaxtion.master.cmd.CmdSender;
import com.huiyun.ixhuiyunaxtion.master.cmd.command.Data;
import com.huiyun.ixhuiyunaxtion.master.cmd.command.Pack;
import com.huiyun.ixhuiyunaxtion.master.cmd.container.CommandBuffer;
import com.huiyun.ixhuiyunaxtion.master.cmd.container.PackContainer;
import com.huiyun.ixhuiyunaxtion.master.inner.OnReceiveCmdListener;
import com.huiyun.ixhuiyunaxtion.master.utils.BytesStringUtils;

/**
 * 串口通信中的命令接收
 *
 */
public class SerialPortReceive extends Thread {
	private SerialPort serialPort;
	private OnReceiveCmdListener listener;
	
	public SerialPortReceive(SerialPort serialPort){
		this.serialPort = serialPort;
	}
	
	@Override
	public void run() {
		System.out.println("创建SerialPortReceive线程，线程名为" + currentThread().getName());
		
		int lenRead;
		byte[] read, pack, cmd;
		read = new byte[Data.MAX_RECEIVE_LENGTH];
		CommandBuffer buffer = new CommandBuffer(Data.BUFFER_CAPACITY);
		PackContainer container = new PackContainer();

		// 向MCU发送搜索命令，以获取自己的CPUID
		CmdSender.readMyChipId();
		
		while (serialPort != null) {
			// 接收数据
			FileInputStream fis = (FileInputStream) serialPort.getInputStream();
			try {
				lenRead = fis.read(read);
			} catch (IOException e) {
				lenRead = 0;
				Log.e("SerialPortReceiveThread", "IOException");
			}
			
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
				if(!Pack.Check_CustomCode(b, Data.CUSTOM_CODE_RF) &&
				   !Pack.Check_CustomCode(b, Data.CUSTOM_CODE_RAY)) continue;
				cmd = Pack.Data_Unpack(b);
				if(cmd == null) continue;
				// 对解包后的命令进行处理
				if(listener != null){
					MainActivity.showString("串口收到命令" + BytesStringUtils.toStringShow(cmd)
							+ ",协议码为" + BytesStringUtils.toUnsignedInt(b[1]),
							MultiTextBuffer.TYPE_SERIALPORT);
					listener.onReceive(Pack.Get_CustomCode(b), cmd);
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
	}
	
	/**
	 * 需要注意的是，这里的监听里的socketId返回的是customCode
	 * @param listener
	 */
	public void setOnReceiveListener(OnReceiveCmdListener listener){
		this.listener = listener;
	}
}
