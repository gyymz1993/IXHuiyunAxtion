package com.huiyun.ixhuiyunaxtion.master.activity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.format.Formatter;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;


import com.huiyun.ixhuiyunaxtion.R;
import com.huiyun.ixhuiyunaxtion.axtion.serialport.SerialPortConnect;
import com.huiyun.ixhuiyunaxtion.master.BaseApplication;
import com.huiyun.ixhuiyunaxtion.master.StaticValues;
import com.huiyun.ixhuiyunaxtion.master.alarm.AlarmHelper;
import com.huiyun.ixhuiyunaxtion.master.alarm.AlarmUtils;
import com.huiyun.ixhuiyunaxtion.master.bean.json.BaseJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.json.DataJsonObj;
import com.huiyun.ixhuiyunaxtion.master.bean.table.Device;
import com.huiyun.ixhuiyunaxtion.master.bean.table.RedRay;
import com.huiyun.ixhuiyunaxtion.master.bean.table.Version;
import com.huiyun.ixhuiyunaxtion.master.cmd.StateStorage;
import com.huiyun.ixhuiyunaxtion.master.cmd.command.Data;
import com.huiyun.ixhuiyunaxtion.master.cmd.process.SerialCmdProcess;
import com.huiyun.ixhuiyunaxtion.master.cmd.redcode.RedCodeProcessor;
import com.huiyun.ixhuiyunaxtion.master.dao.RedRayDao;
import com.huiyun.ixhuiyunaxtion.master.dao.impl.RedRayDaoImpl;
import com.huiyun.ixhuiyunaxtion.master.datatest.NetWorkDetectionReceiver;
import com.huiyun.ixhuiyunaxtion.master.datatest.NetWorkHandler;
import com.huiyun.ixhuiyunaxtion.master.datatest.NetWorkHttpRequest;
import com.huiyun.ixhuiyunaxtion.master.datatest.ReadMemoryThread;
import com.huiyun.ixhuiyunaxtion.master.inner.OnReceiveCmdListener;
import com.huiyun.ixhuiyunaxtion.master.inner.OnReceiveWTRCmdListener;
import com.huiyun.ixhuiyunaxtion.master.inner.OnResultListener;
import com.huiyun.ixhuiyunaxtion.master.json.JsonUtil;
import com.huiyun.ixhuiyunaxtion.master.json.analyst.AnalystManager;
import com.huiyun.ixhuiyunaxtion.master.json.analyst.BaseAnalyst;
import com.huiyun.ixhuiyunaxtion.master.net.NetworkSupport;
import com.huiyun.ixhuiyunaxtion.master.net.WTR.WTRCommandHandler;
import com.huiyun.ixhuiyunaxtion.master.net.errorUpload.ErrorFileUploadUtil;
import com.huiyun.ixhuiyunaxtion.master.net.tcp.TcpConnectionManager;
import com.huiyun.ixhuiyunaxtion.master.net.thread.CommandProcessThread;
import com.huiyun.ixhuiyunaxtion.master.net.thread.PhoneSendThread;
import com.huiyun.ixhuiyunaxtion.master.net.thread.ServerSendThread;
import com.huiyun.ixhuiyunaxtion.master.push.BodyInductionJPushThread;
import com.huiyun.ixhuiyunaxtion.master.utils.FileLogUtil;
import com.huiyun.ixhuiyunaxtion.master.utils.SpUtils;
import com.huiyun.ixhuiyunaxtion.master.utils.UIUtils;
import com.huiyun.ixhuiyunaxtion.master.utils.UpdateUtil;
import com.umeng.analytics.MobclickAgent;

public class MainActivity extends Activity {

	private static Handler handler = new AxtionHandler();
	private static TextView etShow;
	private static CheckBox cbShow;
	private static Spinner spnType;
	/** 储存所有的Json分析员 */
	SparseArray<BaseAnalyst> analysts = new SparseArray<BaseAnalyst>();
	public static MainActivity context;
	public AlarmHelper alarmHelper;

	/** 跟界面打印的内容相关 */
	public static MultiTextBuffer mtbuffer = new MultiTextBuffer();
	public static int currentType = MultiTextBuffer.TYPE_ALL;

	/**
	 * 是否跟随
	 */
	static boolean follow = true;
	private static ScrollView sv_show;
	NetWorkDetectionReceiver receiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = this;
		initViews();// 初始化界面
		init();// 初始化逻辑线程
		/**
		 * 初始化添加红外遥控器
		 */
		initAddInfraredDevice();
		// BaseApplication application = (BaseApplication) getApplication();
		// application.init();
		// application.addActivity(this);
		initHeartBeatilng();// 初始化心跳包
		ErrorFileUploadUtil.startUploadErroFile();
		registerReceiver();
		isNetConnect();

		ReadMemoryThread myThread = new ReadMemoryThread();
		myThread.start();
	}

	/**
	 * Function:检测系统可用内存
	 * 
	 * @author YangShao 2015年5月28日 下午4:11:43
	 * @return
	 */
	public String getAvailMemory() {
		ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo();
		am.getMemoryInfo(mi);
		// mi.availMem; 当前系统的可用内存
		return Formatter.formatFileSize(getBaseContext(), mi.availMem);// 将获取的内存大小规格化

	}

	/**
	 * Function:获得系统内存
	 * 
	 * @author Yangshao 2015年5月28日 下午5:34:28
	 * @return
	 */
	public String getTotalMemory() {
		String str1 = "/proc/meminfo";// 系统内存信息文件
		String str2;
		String[] arrayOfString;
		long initial_memory = 0;
		try {
			FileReader localFileReader = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(
					localFileReader, 8192);
			str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小
			arrayOfString = str2.split("\\s+");
			for (String num : arrayOfString) {
				Log.i(str2, num + "\t");
			}
			initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
			localBufferedReader.close();
		} catch (IOException e) {
		}
		return Formatter.formatFileSize(getBaseContext(), initial_memory);// Byte转换为KB或者MB，内存大小规格化

	};

	/**
	 * Function:获得本得的IP
	 * 
	 * @author Yangshao 2015年5月27日 下午2:57:32
	 * @return
	 */
	public String getGayIp() {
		WifiManager my_wifiManager = ((WifiManager) getSystemService("wifi"));
		DhcpInfo dhcpInfo = my_wifiManager.getDhcpInfo();
		String ip = NetWorkHttpRequest.getHttpRequest().getGateWay(
				dhcpInfo.gateway);
		return ip;
	}

	/**
	 * Function:檢查網絡是否可用
	 * 
	 * @author Yangshao 2015年5月26日 下午5:09:20
	 */
	public void isNetConnect() {
		NetWorkHttpRequest.currentNetWorIP = getGayIp();
		NetWorkHandler handler = new NetWorkHandler();
		handler.sendMessage();
		NetWorkHttpRequest.getHttpRequest().setListener(
				new OnResultListener<Boolean>() {
					@Override
					public void onResult(boolean succsess, Boolean obg) {
						if (succsess) {
							if (obg) {
								// System.out.println("当前网络可用");
								MainActivity.showString("当前网络可用", MultiTextBuffer.TYPE_OTHER);
							} else {
								FileLogUtil.printFileLog(
										"network invalid",
										Environment
												.getExternalStorageDirectory()
												+ "/huiyun/log.txt");
								MainActivity.showString("当前网络不可用", MultiTextBuffer.TYPE_OTHER);
							}
							NetWorkHandler.NET_STATUS = obg;
						}
					}
				});

	}

	/**
	 * 
	 * Function:注册检测网络广播
	 * 
	 * @author Yangshao 2015年5月26日 下午3:11:21
	 */
	private void registerReceiver() {
		IntentFilter filter = new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION);
		receiver = new NetWorkDetectionReceiver();
		this.registerReceiver(receiver, filter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO 自动生成的方法存根
		super.onDestroy();
		unregisterReceiver();
	}

	private void unregisterReceiver() {
		this.unregisterReceiver(receiver);
	}

	/**
	 * for test
	 */
	public void restartAll() {
		BaseApplication application = (BaseApplication) getApplication();
		application.init();
		application.addActivity(this);
	}

	/**
	 * 初始化界面
	 */
	private void initViews() {
		sv_show = (ScrollView) findViewById(R.id.sv_show);
		etShow = (TextView) findViewById(R.id.et_show);
		cbShow = (CheckBox) findViewById(R.id.cb_show);
		cbShow.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				Message msg = new Message();
				msg.obj = null;
				msg.arg1 = 1;
				msg.arg2 = currentType;
				handler.sendMessage(msg);
			}
		});
		CheckBox cb_follow = (CheckBox) findViewById(R.id.cb_follow);
		cb_follow.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				follow = isChecked;
			}
		});
		spnType = (Spinner) findViewById(R.id.spn_type);
		BaseAdapter spnAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, 0, new String[] { "全部",
						"串口", "JSON", "设备", "其它" });
		spnType.setAdapter(spnAdapter);
		spnType.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					currentType = MultiTextBuffer.TYPE_ALL;
					break;
				case 1:
					currentType = MultiTextBuffer.TYPE_SERIALPORT;
					break;
				case 2:
					currentType = MultiTextBuffer.TYPE_JSON;
					break;
				case 3:
					currentType = MultiTextBuffer.TYPE_DEVICE;
					break;
				case 4:
					currentType = MultiTextBuffer.TYPE_OTHER;
					break;
				}

				Message msg = new Message();
				msg.obj = null;
				msg.arg1 = 1;
				msg.arg2 = currentType;
				handler.sendMessage(msg);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	/**
	 * 检查版本更新 Function:
	 * 
	 * @author Yangshao 2015年4月21日 下午5:36:19
	 */
	private void update() {
		UpdateUtil.setOnResultListener(new OnResultListener<Version>() {
			@Override
			public void onResult(boolean isSecceed, Version obj) {
				if (isSecceed == true && null != obj) {
					int serviceCode = obj.getVersion_id();
					// 版本判断
					if (serviceCode > UpdateUtil.getVersionCode(context)) {
						System.out.println("更新ry");
						// 弹出提示框警告
						if (UpdateUtil.isExis()) {
							UpdateUtil.apkfile.delete();
						}
						// handler.notifyAll();
						UpdateUtil.downLoadNewApk(context);
					}
				}
				UpdateUtil.setOnResultListener(null);
			}
		});
		UpdateUtil.getVersion();
	}

	/**
	 * 初始化心跳包相关
	 */
	private void initHeartBeatilng() {
		Message msg = Message.obtain();
		msg.what = 1;
		heartBeatingHandler.sendMessageDelayed(msg, 60000);
		// 收到心跳包返回后置为true
		AnalystManager.getInstance().analyst_222
				.setListener(new OnResultListener<Object>() {

					@Override
					public void onResult(boolean isSucceed, Object obj) {
						if (isSucceed) {
							heartBeatingHandler.bearting = true;
						}
					}
				});
	}

	/**
	 * 心跳包的Handler
	 */
	HeartBeatingHandler heartBeatingHandler = new HeartBeatingHandler();
	private CommandProcessThread commandProcessThread;
	private PhoneSendThread phoneSendThread;
	private ServerSendThread serverSendThread;
	private BodyInductionJPushThread bodyInductionJPushThread;

	/**
	 * Function:
	 * 
	 * @author Yangshao 2015年2月2日 上午9:27:32
	 */
	private void initAddInfraredDevice() {
		RedRayDao infraredDao = new RedRayDaoImpl(UIUtils.getContext());
		RedRay data = new RedRay();
		data.setR_name("电视");
		data.setPageType(1);

		RedRay data1 = new RedRay();
		data1.setR_name("空调");
		data1.setPageType(2);

		RedRay data2 = new RedRay();
		data2.setR_name("音响");
		data2.setPageType(3);

		RedRay data3 = new RedRay();
		data3.setR_name("小米盒子");
		data3.setPageType(4);

		List<RedRay> codes = new ArrayList<RedRay>();
		codes.add(data);
		codes.add(data1);
		codes.add(data2);
		codes.add(data3);
		infraredDao.saveAndupdates(codes);

	}

	/**
	 * 添加一个处理模块
	 * 
	 * @param code
	 * @param analyst
	 */
	public void setAnalyst(int code, BaseAnalyst analyst) {
		analysts.put(code, analyst);
	}

	/**
	 * 将文字打印到界面上
	 * 
	 * @param str
	 */
	public static void showString(String info, int type) {
		Message msg = Message.obtain();
		msg.obj = info;
		msg.arg1 = 1;
		msg.arg2 = type;
		handler.sendMessage(msg);
		// FileLogUtil.fileLog(info, 1);
	}

	/**
	 * 每次接收或者发送命令时，把信息打印到界面上
	 * 
	 */
	public static class AxtionHandler extends Handler {
		List<String> listShow = new ArrayList<String>();

		@Override
		public void handleMessage(Message msg) {

			if (msg.arg1 == 1) {
				if (cbShow.isChecked()) {
					mtbuffer.add((String) msg.obj, msg.arg2);
					mtbuffer.add((String) msg.obj, MultiTextBuffer.TYPE_ALL);

					// 设置光标位置到最新
					if (currentType == msg.arg2
							|| currentType == MultiTextBuffer.TYPE_ALL) {
						// 更新编辑框内容
						String show = mtbuffer.getText(currentType);
						if (show != null) {
							etShow.setText(show);
						}
						if (follow)
							sv_show.scrollTo(0, etShow.getMeasuredHeight()
									- sv_show.getMeasuredHeight());
						// etShow.setSelection(etShow.length());
					}
				} else {
					// 清空内容
					etShow.setText("");
				}
			}
		}
	}

	/**
	 * 初始化程序
	 */
	private void init() {
		update();// 查询主机是否需要更新
		/**
		 * 初始化定时任务
		 */
		new Thread(new Runnable() {
			@Override
			public void run() {
				SystemClock.sleep(1000 * 10);
				MainActivity.showString(
						"当前系统时间："
								+ AlarmUtils.getDateOfString(String
										.valueOf(AlarmUtils
												.getNowTimeMinuties())),
						MultiTextBuffer.TYPE_OTHER);
				alarmHelper = new AlarmHelper(context);
				alarmHelper.setAlarmTimes(context);
			}
		}).start();

		commandProcessThread = CommandProcessThread.getInstance();
		commandProcessThread.start();
		phoneSendThread = PhoneSendThread.getInstance();
		phoneSendThread.start();
		serverSendThread = ServerSendThread.getInstance();
		serverSendThread.start();
		bodyInductionJPushThread = BodyInductionJPushThread.getInstance();
		bodyInductionJPushThread.start();

		/* 从sp中获取芯片ID */
		StaticValues.MASTER_ID = SpUtils.getValues("masterId");
		MainActivity.showString("从SP读取后得知，主机的芯片ID为" + StaticValues.MASTER_ID,
				MultiTextBuffer.TYPE_DEVICE);

		/* 初始化所有的Json命令分析员 */
		AnalystManager.getInstance().initAnalysts();

		/* 初始化线程并设置监听 */
		SerialPortConnect.getInstance().setOnReceiveListener(
				new OnReceiveCmdListener() {

					@Override
					public void onReceive(int customCode, byte[] bytes) {
						// 处理串口发送过来的命令
						if (customCode == Data.CUSTOM_CODE_RF) {
							SerialCmdProcess.getInstance().handleRFCommand(
									bytes);
						} else if (customCode == Data.CUSTOM_CODE_RAY) {
							// 处理从机发送过来的红外命令
							// (旧红外处理)RayProcessor.getInstance().processRayCommand(bytes);
							RedCodeProcessor.getInstance().handleCommand(bytes);
						}
					}
				});
		TcpConnectionManager.getInstance().setWTRRecievedListener(
				new OnReceiveWTRCmdListener() {

					@Override
					public void onReceive(int socketId, int customCode,
							byte[] bytes) {
						// 处理射频中转器发过来的命令
						WTRCommandHandler.handleWTRCommand(socketId,
								customCode, bytes);
					}
				});
		TcpConnectionManager.getInstance().setPhoneJsonRecievedListener(
				new OnReceiveCmdListener() {
					@Override
					public void onReceive(int socketId, byte[] bytes) {
						// 处理客户端发过来的json命令
						netResult(socketId, bytes);
					}
				});

		deviceStateChangeNotification();

		/* 检查当前网络状态 */
		new Thread() {
			public void run() {
				// 5秒后启动
				SystemClock.sleep(1000 * 10);
				NetworkSupport.checkWifiState();
			};
		}.start();

		/* 更新StateStorage */
		new Thread() {
			public void run() {
				// 3秒后进行状态更新
				SystemClock.sleep(1000 * 3);
				StateStorage.getInstance().update();
			};
		}.start();

	}

	/**
	 * 接收到网络命令后调用此方法
	 * 
	 * @param bytes
	 *            解包以后的数据
	 */
	private void netResult(int socketId, byte[] bytes) {
		BaseJsonObj jsonObj = JsonUtil.analyzeBytes(bytes);
		if (jsonObj != null) {
			BaseAnalyst analyst = analysts.get(jsonObj.code);
			if (analyst != null)
				analyst.handleData(socketId, jsonObj);// 分发给相应的模块处理
			else
				MainActivity.showString(jsonObj.code + "码没有模块处理",
						MultiTextBuffer.TYPE_JSON);
		}
	}

	/**
	 * Function: 设备状态改变变更通知
	 * 
	 * @author Yangshao 2015年1月15日 下午2:19:57
	 */
	public void deviceStateChangeNotification() {
		StateStorage.getInstance().setOnStateChangeListener(
				new OnResultListener<Device>() {
					@Override
					public void onResult(boolean isSucceed, Device obj) {
						Map<String, String> resultMap = new HashMap<String, String>();
						DataJsonObj send_jsonObj = new DataJsonObj();
						if (isSucceed) {
							send_jsonObj.setCode(30);
							send_jsonObj.setObj("phone");
							send_jsonObj.setResult(1);
							resultMap.put("phoneCode", obj.getPhoneCode() + "");
							resultMap.put("state", obj.getState() + "");
							send_jsonObj.setData(resultMap);
						} else {
							send_jsonObj.setResult(2);
						}
						TcpConnectionManager.getInstance().sendJsonAll(
								send_jsonObj);
					}
				});
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
