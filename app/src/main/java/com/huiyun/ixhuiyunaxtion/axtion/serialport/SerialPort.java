package com.huiyun.ixhuiyunaxtion.axtion.serialport;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.util.Log;

/**
 * 串口通信
 *
 */
public class SerialPort {
	
	// TAG
	private static final String TAG = "SerialPort";

	/* Do not remove or rename the field mFd: it is used by native method close(); */
	private FileDescriptor mFd;
	private FileInputStream mFileInputStream;
	private FileOutputStream mFileOutputStream;

	
	 /**
	 * @param flags
	 */
	public SerialPort(int flags) throws SecurityException, IOException {
		File device = new File(SerialPortSupport.serialportPath);
		
		/* Check access permission */
		if (!device.canRead() || !device.canWrite()) {
			try {
				/* Missing read/write permission, trying to chmod the file */
				Process su;
				su = Runtime.getRuntime().exec("/system/bin/su");
				String cmd = "chmod 666 " + device.getAbsolutePath()
						+ "\n"	+ "exit\n";
				su.getOutputStream().write(cmd.getBytes());
				
				if ((su.waitFor() != 0) || !device.canRead()
						|| !device.canWrite()) {
					throw new SecurityException(); 
				}
			} catch (Exception e) {
				Log.e(TAG, "SecurityException");
				throw new SecurityException();
			}
		}

		mFd = open(device.getAbsolutePath(), SerialPortSupport.baudRate, flags);
		if (mFd == null) {
			Log.e(TAG, "native open returns null");
			throw new IOException();
		}
		mFileInputStream = new FileInputStream(mFd);
		mFileOutputStream = new FileOutputStream(mFd);
	}

	
	 /**
	 *  Function:获得串口的输入流
	 *  @return
	 */
	public InputStream getInputStream() {
		return mFileInputStream;
	}

	
	 /**
	 *  Function:获得串口的输出流
	 *  @return
	 */
	public OutputStream getOutputStream() {
		return mFileOutputStream;
	}
	
	// JNI
	public native static FileDescriptor open(String path, int baudrate, int flags);
	public native void close();
	
	static{
		System.loadLibrary("SerialPort");
	}
}
