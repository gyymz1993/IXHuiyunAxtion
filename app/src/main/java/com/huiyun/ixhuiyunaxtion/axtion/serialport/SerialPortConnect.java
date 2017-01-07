package com.huiyun.ixhuiyunaxtion.axtion.serialport;

import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;
import com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer;
import com.huiyun.ixhuiyunaxtion.master.cmd.command.Pack;
import com.huiyun.ixhuiyunaxtion.master.inner.OnReceiveCmdListener;
import com.huiyun.ixhuiyunaxtion.master.net.WTR.WTRCommandSender;
import com.huiyun.ixhuiyunaxtion.master.net.thread.CommandProcessThread;
import com.huiyun.ixhuiyunaxtion.master.utils.BytesStringUtils;

import java.io.IOException;



/**
 * 串口通信的工具类
 * 
 * @author lzn
 * 
 */
public class SerialPortConnect {

	private SerialPort serialPort;
	private OnReceiveCmdListener listener;

	/**
	 * 信息发送间隔时间
	 */
	public static final int COMMAND_SEND_INTERVAL = 300;

	private static SerialPortConnect instance;

	/**
	 * 单例模式
	 */
	public static SerialPortConnect getInstance() {
		synchronized (SerialPortConnect.class) {
			if (instance == null) {
				instance = new SerialPortConnect();
			}
			return instance;
		}
	}

	private SerialPortConnect() {
		try {
			serialPort = new SerialPort(0);
			MainActivity
					.showString("串口连接创建成功", MultiTextBuffer.TYPE_SERIALPORT);
		} catch (SecurityException e) {
			MainActivity.showString("串口连接创建失败 ==> \nSecurityException",
					MultiTextBuffer.TYPE_SERIALPORT);
		} catch (IOException e) {
			MainActivity.showString("串口连接创建失败 ==> IOException",
					MultiTextBuffer.TYPE_SERIALPORT);
		}
		SerialPortReceive receive = new SerialPortReceive(serialPort);
		receive.setOnReceiveListener(new OnReceiveCmdListener() {

			@Override
			public void onReceive(final int socketId, final byte[] bytes) {
				// 在命令处理线程执行
				CommandProcessThread.getInstance().getHandler()
						.post(new Runnable() {

							@Override
							public void run() {
								if (listener != null) {
									listener.onReceive(socketId, bytes);
								}
							}
						});
				// shirt+alt+contr+t
			}
		});
		receive.start();
		SerialPortSendThread.getInstance().start();
	}

	public void setOnReceiveListener(OnReceiveCmdListener listener) {
		this.listener = listener;
	}

	
	 /**
	 *  Function: 切换到串口命令发送线程发送命令，命令之间有时间间隔
	 *  @param customCode 用户自定义码（打包时使用）
	 *  @param cmd 射频命令
	 */
	public void send(int customCode, byte[] cmd) {
		final byte[] write = Pack.Data_Pack(customCode, cmd);
		if (cmd.length < 50) {
			MainActivity.showString(
					"串口发送命令" + BytesStringUtils.toStringShow(cmd) + ",自定义码为"
							+ customCode, MultiTextBuffer.TYPE_SERIALPORT);
		} else {
			MainActivity.showString("串口发送命令，长度为" + cmd.length,
					MultiTextBuffer.TYPE_SERIALPORT);
		}
		try {
			SerialPortSendThread.getInstance().getHandler()
					.post(new Runnable() {

						@Override
						public void run() {
							try {
								if (serialPort == null) {
									return;
								}

								synchronized (SerialPortConnect.class) {
									serialPort.getOutputStream().write(write);
									try {
										Thread.sleep(COMMAND_SEND_INTERVAL);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 同时向所有射频中转器发送同样的命令
		WTRCommandSender.sendCommand(cmd);
	}
	
	
	
	 /**
	 *  发送命令
	 *  不会更换到串口发送线程，也没有时间间隔（谨慎使用）
	 *  @param customCode 用户自定义码（打包时使用）
	 *  @param cmd 射频命令
	 */
	public void sendWithoutInterval(int customCode, byte[] cmd) {
		final byte[] write = Pack.Data_Pack(customCode, cmd);
		if (cmd.length < 50) {
			MainActivity.showString(
					"串口发送命令" + BytesStringUtils.toStringShow(cmd) + ",自定义码为"
							+ customCode, MultiTextBuffer.TYPE_SERIALPORT);
		} else {
			MainActivity.showString("串口发送命令，长度为" + cmd.length,
					MultiTextBuffer.TYPE_SERIALPORT);
		}
		try {
			if (serialPort == null) {
				return;
			}

			synchronized (SerialPortConnect.class) {
				serialPort.getOutputStream().write(write);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 同时向所有射频中转器发送同样的命令
		WTRCommandSender.sendCommand(cmd);
	}
}
