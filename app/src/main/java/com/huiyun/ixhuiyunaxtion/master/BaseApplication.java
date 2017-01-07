package com.huiyun.ixhuiyunaxtion.master;

import java.util.ArrayList;



import android.app.Activity;
import android.app.Application;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

import com.huiyun.ixhuiyunaxtion.master.datatest.ReadMemory;
import com.huiyun.ixhuiyunaxtion.master.net.NetworkSupport;
import com.huiyun.ixhuiyunaxtion.master.net.udp.UDPConnect;
import com.huiyun.ixhuiyunaxtion.master.proc.ShellExecute;
import com.huiyun.ixhuiyunaxtion.master.utils.FileLogUtil;

/**
 * 程序运行后首先运行。 进行一些初始化操作
 * 
 * @author torah
 * 
 */
public class BaseApplication extends Application {
	private static BaseApplication mBaseApplication = null;
	private static Looper mMainThreadLooper = null;
	private static Handler mMainThreadHandler = null;
	private static int mMainThreadId;
	private static Thread mMainThread = null;
	private static boolean isDebug = true; // true:测试版,false:正式版
	private Thread udpConnect;

	ArrayList<Activity> list = new ArrayList<Activity>();

	public void init() {
		// 设置该CrashHandler为程序的默认处理器
		UnCeHandler catchExcep = new UnCeHandler(this);
		Thread.setDefaultUncaughtExceptionHandler(catchExcep);
	}

	/**
	 * Activity关闭时，删除Activity列表中的Activity对象
	 */
	public void removeActivity(Activity a) {
		list.remove(a);
	}

	/**
	 * 向Activity列表中添加Activity对象
	 */
	public void addActivity(Activity a) {
		list.add(a);
	}

	/**
	 * 关闭Activity列表中的所有Activity
	 */
	public void finishActivity() {
		for (Activity activity : list) {
			if (null != activity) {
				activity.finish();
			}
		}
		// 杀死该应用进程
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	@Override
	public void onCreate() {
		super.onCreate();
		BaseApplication.mBaseApplication = this;
		BaseApplication.mMainThreadLooper = getMainLooper();
		BaseApplication.mMainThreadHandler = new Handler();
		BaseApplication.mMainThreadId = android.os.Process.myTid();
		BaseApplication.mMainThread = Thread.currentThread();
		
		// 开启UDP监听线程
		startUDPThread();
		
		// 20秒后把重启信息打印到文件
		new Thread(){
			public void run() {
				SystemClock.sleep(20000);
				printRestartLog();
			};
		}.start();
		
		// 测试版与正式版的区别
		initSomeDebugInformation();
		
		// 获取SU权限并开启ADB远程调试
		ShellExecute.enableRemoteAdb();
	}
	
	 /**
	 *  Function: 把重启信息打印到文件
	 *  @author lzn 
	 *  2015-5-13 上午11:15:12
	 */
	private void printRestartLog(){
		String log = "master restart";
		FileLogUtil.printFileLog(log, Environment.getExternalStorageDirectory()
				+ "/huiyun/log.txt");
		}
	
	public static void printCPUDataLog(){
		String log = "running status:"+ ReadMemory.CPUMessage;
		FileLogUtil.printFileLog(log, Environment.getExternalStorageDirectory()
				+ "/huiyun/log.txt");
	}
	 /**
	 *  Function: 启动/重启udp接收线程类
	 *  @author lzn 
	 *  2015-5-13 下午2:31:47
	 *  @return
	 */
	public void startUDPThread(){
		udpConnect = new UDPConnect();
		udpConnect.start();
	}
	
	 /**
	 *  Function:初始化所有测试版与正式版有所不相同的参数
	 *  @author lzn 
	 *  2015-4-24 上午10:59:03
	 */
 	private void initSomeDebugInformation(){
		if(isDebug){
			// 测试版
			StaticValues.versionFile = 
					"http://192.1680.1.249:8080/JavaServers/index.txt";
			StaticValues.downLoadFile = 
					"http://192.168.1.249:8080/JavaServers/AxtionMaster2.apk";
			NetworkSupport.UDP_REQUEST_IP_FROM_PHONE_RETURN =
					"(ipAddr),TEST,AXTION";
			NetworkSupport.UDP_REQUEST_IP_FROM_WTR = 
					"wha is master,i am Wifi_to_RF Repeaters";
		} else {
			// 正式版
			StaticValues.versionFile = 
					"http://192.168.1.5:8080/JavaServers/index.txt";
			StaticValues.downLoadFile = 
					"http://192.168.1.5:8080/JavaServers/AxtionMaster2.apk";
			NetworkSupport.UDP_REQUEST_IP_FROM_PHONE_RETURN = 
					"(ipAddr),MASTER,AXTION";
			NetworkSupport.UDP_REQUEST_IP_FROM_WTR = 
					"who is master,i am Wifi_to_RF Repeaters";
		}
	}

	public static BaseApplication getApplication() {
		return mBaseApplication;
	}

	public static Looper getMainThreadLooper() {
		return mMainThreadLooper;
	}

	public static Handler getMainThreadHandler() {
		return mMainThreadHandler;
	}

	public static int getMainThreadId() {
		return mMainThreadId;
	}

	public static Thread getMainThread() {
		return mMainThread;
	}

}
