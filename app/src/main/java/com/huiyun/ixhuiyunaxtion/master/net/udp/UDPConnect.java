package com.huiyun.ixhuiyunaxtion.master.net.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

import android.os.SystemClock;

import com.huiyun.ixhuiyunaxtion.master.BaseApplication;
import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;
import com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer;
import com.huiyun.ixhuiyunaxtion.master.net.NetworkSupport;


/**
 * UDP通信 自动初始化
 * 
 * @author lzn
 */
public class UDPConnect extends Thread {

	DatagramSocket socket;

	public UDPConnect() {
		try {
			/**
			 * 避免端口占用
			 */
			DatagramSocket msocket = new DatagramSocket(null);
			msocket.setReuseAddress(true);
			msocket.bind(new InetSocketAddress(NetworkSupport.UDP_PORT));
			this.socket = msocket;
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		System.out.println("创建UDPConnect线程，线程名为" + currentThread().getName());

		while (socket != null && !socket.isClosed()) {
			try {
				byte[] receive = new byte[64];
				DatagramPacket packet = new DatagramPacket(receive,
						receive.length);
				socket.receive(packet);
				String msg = new String(packet.getData(), "UTF-8");
				if (msg != null) {
					MainActivity.showString("主机收到UDP命令 " + msg,
							MultiTextBuffer.TYPE_OTHER);
				}
				if (msg.trim().equals(NetworkSupport.UDP_REQUEST_IP_FROM_PHONE)) {
					sendIpAddrMsgToPhone(packet);
				} else if (msg.trim().equals(
						NetworkSupport.UDP_REQUEST_IP_FROM_WTR)) {
					sendIpAddrMsgToWTR(packet);
				}
			} catch (IOException e) {
				MainActivity.showString(
						"UDP线程抛出异常", MultiTextBuffer.TYPE_OTHER);
			}
		}
		MainActivity.showString(
				"UDP线程关闭", MultiTextBuffer.TYPE_OTHER);
		
		// 在该线程上请求重启UDP连接
		BaseApplication.getMainThreadHandler()
			.post(new Runnable() {
			
			@Override
			public void run() {
				BaseApplication.getApplication().startUDPThread();
			}
		});
	}

	// 将带有ip地址的信息反馈给发送源
	public void sendIpAddrMsgToPhone(final DatagramPacket packet) {
		new Thread() {
			public void run() {
				int[] addr = new int[4];
				int _addr = NetworkSupport.getWifiInfo().getIpAddress();
				addr[0] = (_addr >> 24) & 0xff;
				addr[1] = (_addr >> 16) & 0xff;
				addr[2] = (_addr >> 8) & 0xff;
				addr[3] = _addr & 0xff;
				String ipAddr = String.valueOf(addr[3]) + "."
						+ String.valueOf(addr[2]) + "."
						+ String.valueOf(addr[1]) + "."
						+ String.valueOf(addr[0]);

				String msgSend = NetworkSupport.UDP_REQUEST_IP_FROM_PHONE_RETURN
						.replace("(ipAddr)", ipAddr);
				byte[] send = msgSend.getBytes();
				DatagramPacket pack = new DatagramPacket(send, send.length,
						packet.getAddress(), packet.getPort());

				try {
					socket.send(pack);
					SystemClock.sleep(200);
					socket.send(pack);
					SystemClock.sleep(200);
					socket.send(pack);
					SystemClock.sleep(200);
					socket.send(pack);
					SystemClock.sleep(200);
					socket.send(pack);
					MainActivity.showString("主机发送UDP命令 " + msgSend,
							MultiTextBuffer.TYPE_OTHER);
				} catch (IOException e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	public void sendIpAddrMsgToWTR(final DatagramPacket packet) {
		new Thread() {
			public void run() {
				int _addr = NetworkSupport.getWifiInfo().getIpAddress();
				int[] addr = new int[4];
				addr[0] = (_addr >> 24) & 0xff;
				addr[1] = (_addr >> 16) & 0xff;
				addr[2] = (_addr >> 8) & 0xff;
				addr[3] = _addr & 0xff;
				String ipAddr = String.valueOf(addr[3]) + "."
						+ String.valueOf(addr[2]) + "."
						+ String.valueOf(addr[1]) + "."
						+ String.valueOf(addr[0]);

				String msgSend = NetworkSupport.UDP_REQUEST_IP_FROM_WTR_RETURN
						.replace("(ipAddr)", ipAddr);
				byte[] send;
				try {
					send = endWith0D(msgSend.getBytes("UTF-8"));
					DatagramPacket pack = new DatagramPacket(send, send.length,
							packet.getAddress(), packet.getPort());
					socket.send(pack);
					SystemClock.sleep(200);
					socket.send(pack);
					SystemClock.sleep(200);
					socket.send(pack);
					SystemClock.sleep(200);
					socket.send(pack);
					SystemClock.sleep(200);
					socket.send(pack);
					MainActivity.showString("主机发送UDP命令 " + msgSend,
							MultiTextBuffer.TYPE_OTHER);
				} catch (IOException e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	private byte[] endWith0D(byte[] b) {
		byte[] b2 = new byte[b.length + 1];
		for (int i = 0; i < b.length; i++) {
			b2[i] = b[i];
		}
		b2[b.length] = 0x0d;
		return b2;
	}
}
