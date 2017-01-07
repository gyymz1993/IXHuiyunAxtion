package com.huiyun.ixhuiyunaxtion.master.cmd.process;

import java.util.List;

import android.os.Environment;
import android.os.Message;
import android.os.SystemClock;

import com.huiyun.ixhuiyunaxtion.axtion.serialport.SerialPortConnect;
import com.huiyun.ixhuiyunaxtion.axtion.serialport.SerialPortSendThread;
import com.huiyun.ixhuiyunaxtion.master.StaticValues;
import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;
import com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer;
import com.huiyun.ixhuiyunaxtion.master.bean.table.Device;
import com.huiyun.ixhuiyunaxtion.master.bean.table.SceneItem;
import com.huiyun.ixhuiyunaxtion.master.cmd.AddrAllocator;
import com.huiyun.ixhuiyunaxtion.master.cmd.CommandUtil;
import com.huiyun.ixhuiyunaxtion.master.cmd.DeviceInfoProcessor;
import com.huiyun.ixhuiyunaxtion.master.cmd.GatewayProcessor;
import com.huiyun.ixhuiyunaxtion.master.cmd.PhoneCodeAllocator;
import com.huiyun.ixhuiyunaxtion.master.cmd.StateStorage;
import com.huiyun.ixhuiyunaxtion.master.cmd.command.CMD;
import com.huiyun.ixhuiyunaxtion.master.cmd.command.Data;
import com.huiyun.ixhuiyunaxtion.master.cmd.command.ICMD;
import com.huiyun.ixhuiyunaxtion.master.cmd.container.IOCtrlQueue;
import com.huiyun.ixhuiyunaxtion.master.dao.DeviceDao;
import com.huiyun.ixhuiyunaxtion.master.dao.SceneDao;
import com.huiyun.ixhuiyunaxtion.master.dao.database.RelationBean;
import com.huiyun.ixhuiyunaxtion.master.dao.database.RelationDao;
import com.huiyun.ixhuiyunaxtion.master.dao.impl.DeviceDaoImpl;
import com.huiyun.ixhuiyunaxtion.master.dao.impl.SceneDaoImpl;
import com.huiyun.ixhuiyunaxtion.master.datatest.NetWorkHandler;
import com.huiyun.ixhuiyunaxtion.master.inner.OnResultListener;
import com.huiyun.ixhuiyunaxtion.master.push.BodyInductionJPushThread;
import com.huiyun.ixhuiyunaxtion.master.utils.BytesStringUtils;
import com.huiyun.ixhuiyunaxtion.master.utils.FileLogUtil;
import com.huiyun.ixhuiyunaxtion.master.utils.SpUtils;
import com.huiyun.ixhuiyunaxtion.master.utils.UIUtils;


/**
 * 处理从MCU接收的命令
 * 
 * @author lzn
 */
public class SerialCmdProcess {
	/**
	 * byte型转无符号int型：int i = toUnisgnedInt(b); (无论有无符号)int型转byte型： byte b =
	 * (byte)(i & 0xff); 当然，实际上int转byte只要byte b = (byte) i
	 * 就可以了，写成上述那样只是为了方便理解(byte占用1个字节，int要占用4个字节)。
	 */

	private static SerialCmdProcess instance;

	/**
	 * 单例模式
	 * 
	 * @return
	 */
	public static SerialCmdProcess getInstance() {
		synchronized (SerialCmdProcess.class) {
			if (instance == null) {
				instance = new SerialCmdProcess();
			}

			return instance;
		}
	}

	/**
	 * 输入队列
	 */
	public IOCtrlQueue queue = new IOCtrlQueue();
	/**
	 * 命令反馈检查器
	 */
	public CmdFeedbackChecker checker = new CmdFeedbackChecker();
	private CMD icmd = new ICMD();

	/**
	 * 一定时间内生效的开关，用来判断设备信息命令是否处理
	 */
	private AgingSwitch agingSwitch1, agingSwitch2;

	/**
	 * Function: 获取搜索反馈命令的开关
	 * 
	 * @author lzn 2015-4-14 上午10:49:54
	 * @return
	 */
	public AgingSwitch getAgingSwitch1() {
		return agingSwitch1;
	}

	/**
	 * Function: 获取手动确认反馈命令的开关
	 * 
	 * @author lzn 2015-4-14 上午10:50:28
	 * @return
	 */
	public AgingSwitch getAgingSwitch2() {
		return agingSwitch2;
	}

	private SerialCmdProcess() {
		// 搜索反馈命令的失效延迟为120秒
		agingSwitch1 = new AgingSwitch(120);
		// 手动确认反馈命令的失效延迟为30秒
		agingSwitch2 = new AgingSwitch(30);
	}

	/**
	 * 处理来自串口的命令
	 * 
	 * @param cmd
	 */
	public void handleRFCommand(byte[] cmd) {

		int funcCode = toUnsignedInt(cmd[Data.P_FUNC_CODE]);

		switch (funcCode) {
		case Data.FUNC_CODE_SEARCH_RETURN: // 设备信息命令1(即搜索设备的反馈命令)
			handleDeviceInfoCommand(cmd);
			break;
		case Data.FUNC_CODE_SET_ADDR_RETURN: // 设置新地址的反馈命令
			handleSetAddrReturnCommand(cmd);
			break;
		case Data.FUNC_CODE_IO_W_RETURN: // 开关写入的反馈命令
			handleIOWriteReturnCommand(cmd);
			break;
		case Data.FUNC_CODE_IO_R_RETURN: // 开关读出的反馈命令
			handleIOReadReturnCommand(cmd);
			break;
		case Data.FUNC_CODE_IO_NOTICE_RETURN: // 开关变更通知的反馈命令
			handleIONoticeReturnCommand(cmd);
			break;
		case Data.FUNC_CODE_SET_W_RETURN: // 设置写入的反馈命令
			handleGatewaySetFunReturnCommand(cmd);
			break;
		case Data.FUNC_CODE_SET_R_RETURN: // 设置读出的反馈命令
			handleGatewayReadFunReturnCommand(cmd);
			break;
		case Data.FUNC_CODE_IO_W: // 开关写入命令
			handleIOWriteCommand(cmd);
			break;
		case Data.FUNC_CODE_IO_NOTICE: // 开关变更通知命令
			handleIONoticeCommand(cmd);
			break;
		case Data.FUNC_CODE_DEV_HAND_RETURN: // 设备信息命令2(手动确认的反馈命令)
			handleDeviceHandReturnCommand(cmd);
			break;
		case Data.FUNC_CODE_HEART_BEAT: // 心跳命令
			handleHeartBeatCommand(cmd);
			break;
		}
	}

	/**
	 * 处理来自WIFI的命令
	 * 
	 * @param cmd
	 */
	public void handleWifiCommand(byte[] cmd) {
		int funcCode = toUnsignedInt(cmd[Data.P_FUNC_CODE]); // 功能码

		switch (funcCode) {
		case Data.FUNC_CODE_IO_W: // 开关写入命令
			handleIOWriteCommand(cmd);
			break;
		case Data.FUNC_CODE_IO_R: // 开关读出命令
			handleIOReadCommand(cmd);
			break;
		}
	}

	/**
	 * 处理开关写入命令
	 * 
	 * 查表，然后逐个发送开关写入命令
	 */
	private void handleIOWriteCommand(byte[] cmd) {
		// PAN KEY不对，不作任何处理
		if (!CommandUtil.checkPanKey(toUnsignedInt(cmd[Data.P_PAN_KEY]))) {
			return;
		}

		// 目标地址不是本主机地址时，不作任何处理
		if (toUnsignedInt(cmd[Data.P_TARGET_ADDR]) != CommandUtil
				.getLocalAddr()) {
			return;
		}

		// 获得输入的地址和编号
		int inputAddr = toUnsignedInt(cmd[Data.P_LOCAL_ADDR]), state;
		int inputObj = toUnsignedInt(cmd[Data.P_OBJECT_NUM]);

		// 如果是热式红外人体感应
		DeviceDao ddao = new DeviceDaoImpl(UIUtils.getContext());
		List<Device> listDev = ddao.getAllDevicesByAddress(inputAddr);
		if (listDev != null && listDev.size() > 0) {
			if (listDev.get(0).getType() == Device.TYPE_BODY_INDUCTION) {
				state = toUnsignedInt(cmd[Data.P_DATA]);
				if (state > 0) {
					new Thread() {
						public void run() {
							// 推送信息
							Message msg = new Message();
							msg.arg1 = 1;
							BodyInductionJPushThread.getInstance().getHandler()
									.sendMessage(msg);
						};
					}.start();
				}
				return;
			}
		}

		// 开关控制
		if (inputAddr == Data.ADDR_WIFI) {
			switch (StateStorage.getInstance().getTypeByPhoneCode(inputObj)) {
			case Device.TYPE_COLOR_LAMP:
				// 调色灯
				state = ColorState.getRGBValue(
						toUnsignedInt(cmd[Data.P_DATA + 1]),
						toUnsignedInt(cmd[Data.P_DATA + 2]),
						toUnsignedInt(cmd[Data.P_DATA]));
				break;
			default:
				// 其它
				state = toUnsignedInt(cmd[Data.P_DATA]);
				break;
			}
		} else {
			state = toUnsignedInt(cmd[Data.P_DATA]);
		}

		// 查表，找出所有对应的输出地址和输出编号并发送出去
		RelationDao rdao = new RelationDao(UIUtils.getContext());
		List<RelationBean> list = rdao
				.findAllByInput(inputAddr, inputObj);

		for (RelationBean bean : list) {
			// 如果该输出属于场景
			if (bean.getOutputType() == RelationBean.OUT_TYPE_SCENE) {
				SceneDao sceneDao = new SceneDaoImpl(UIUtils.getContext());
				String sceneName = bean.getSceneName();
				List<SceneItem> sceneItems = sceneDao
						.queryPhoneCodeforScene(sceneName);
				if (sceneItems != null) {
					for (SceneItem item : sceneItems) {
						StateStorage.getInstance().writeState(
								item.getPhone_code(), item.getState());
					}
				}
				// 为了要让场景开关保持开启状态，只有开的命令会返回
				if (state > 0) {
					byte[] send = icmd.CMD_Ask_CRTL_IO(inputAddr,
							CommandUtil.getLocalAddr(),
							CommandUtil.getPanKey(), inputObj, 0, null);
					sendCommand(send);
				}
				continue;
			}

			// 加入队列
			int outputAddr = bean.getOutputAddr();
			int outputObj = bean.getOutputObj();
			queue.delete(inputAddr, inputObj, outputAddr, outputObj, state);
			queue.add(inputAddr, inputObj, outputAddr, outputObj, state);

			// 从状态储存器中获取设备信息
			Device dev = StateStorage.getInstance().readState(outputAddr,
					outputObj);
			if (dev != null) {
				// 发送命令
				int obj;
				byte[] data;
				switch (dev.getType()) {
				case Device.TYPE_GATEWAY:
					// 网关
					obj = 0;
					data = new byte[2];
					data[0] = (byte) (outputObj & 0xff);
					data[1] = (byte) (state & 0xff);
					break;
				case Device.TYPE_OUT_WITH_IN:
				case Device.TYPE_OUT:
					// 普通输出(如普通灯、窗帘)
					obj = outputObj;
					data = new byte[1];
					data[0] = (byte) (state & 0xff);
					break;
				case Device.TYPE_COLOR_LAMP:
					// 调色灯
					obj = outputObj;
					data = new byte[3];
					data[0] = cmd[Data.P_DATA];
					data[1] = cmd[Data.P_DATA + 1];
					data[2] = cmd[Data.P_DATA + 2];
					break;
				default:
					// 默认
					obj = outputObj;
					data = new byte[1];
					data[0] = (byte) (state & 0xff);
					break;
				}

				final byte[] send = icmd.CMD_CRTL_IO(outputAddr,
						CommandUtil.getLocalAddr(), CommandUtil.getPanKey(),
						obj, data.length, data);
				// 重发机制下的发送命令
				SerialPortSendThread.getInstance().getHandler()
						.post(new Runnable() {

							@Override
							public void run() {
								int addr = toUnsignedInt(send[Data.P_TARGET_ADDR]);
								
								// 地址不属于从机地址时
								if(!isAddrSlave(addr)){
									return;
								}
								
								int funcCode = toUnsignedInt(send[Data.P_FUNC_CODE]);
								checker.add(addr, Data.FUNC_CODE_IO_W_RETURN);

								int count = 5;
								while (checker.have(addr,
										Data.FUNC_CODE_IO_W_RETURN)
										&& count > 0) {
									sendCommandTypeSecond(send);
									count--;
								}
								
								if(count < 4){
									String log = "resend " + (4-count) + 
											" times, target address is " + addr +
											", function code is " + funcCode;
									FileLogUtil.printFileLog(log, Environment.getExternalStorageDirectory()
											+ "/huiyun/log.txt");
								}
							}
						});
			}

		}
		// 关闭DAO
		rdao.close();
	}

	/**
	 * 处理开关读出命令
	 * 
	 */
	private void handleIOReadCommand(byte[] cmd) {
		// 密码不对,不进行处理
		if (!CommandUtil.checkPanKey(toUnsignedInt(cmd[Data.P_PAN_KEY]))) {
			return;
		}

		CommandUtil.setLocalAddrToMyself(cmd);
		sendCommand(cmd);
	}

	/**
	 * 处理开关变更通知命令
	 * 
	 * 查表，然后逐个发送开关变更通知命令
	 */
	private void handleIONoticeCommand(byte[] cmd) {
		// 密码不对,不进行处理
		if (!CommandUtil.checkPanKey(toUnsignedInt(cmd[Data.P_PAN_KEY]))) {
			return;
		}

		// 获取输出地址、编号
		int outputAddr = toUnsignedInt(cmd[Data.P_LOCAL_ADDR]), outputObj, state;
		switch (StateStorage.getInstance().getTypeByAddress(outputAddr)) {
		case Device.TYPE_GATEWAY:
			// 网关
			outputObj = toUnsignedInt(cmd[Data.P_DATA]);
			state = toUnsignedInt(cmd[Data.P_DATA + 1]);
			break;
		case Device.TYPE_OUT_WITH_IN:
		case Device.TYPE_OUT:
			// 普通输出(如普通灯、窗帘)
			outputObj = toUnsignedInt(cmd[Data.P_OBJECT_NUM]);
			state = toUnsignedInt(cmd[Data.P_DATA]);
			break;
		case Device.TYPE_COLOR_LAMP:
			// 调色灯
			if (cmd.length > Data.P_DATA + 2) {
				outputObj = toUnsignedInt(cmd[Data.P_OBJECT_NUM]);
				state = ColorState.getRGBValue(
						toUnsignedInt(cmd[Data.P_DATA + 1]),
						toUnsignedInt(cmd[Data.P_DATA + 2]),
						toUnsignedInt(cmd[Data.P_DATA]));
			} else {
				outputObj = toUnsignedInt(cmd[Data.P_OBJECT_NUM]);
				state = toUnsignedInt(cmd[Data.P_DATA]);
			}
			break;
		default:
			// 默认
			outputObj = toUnsignedInt(cmd[Data.P_OBJECT_NUM]);
			state = toUnsignedInt(cmd[Data.P_DATA]);
			break;
		}
		// 发送通知的反馈命令
		// 注：除了目标地址和本机地址反过来以及功能码有改变外，其它原样返回
		byte[] cmdReturn = new byte[cmd.length];
		for (int i = 0; i < cmd.length; i++) {
			cmdReturn[i] = cmd[i];
		}
		cmdReturn[Data.P_TARGET_ADDR] = cmd[Data.P_LOCAL_ADDR];
		cmdReturn[Data.P_LOCAL_ADDR] = cmd[Data.P_TARGET_ADDR];
		cmdReturn[Data.P_FUNC_CODE] = Data.FUNC_CODE_IO_NOTICE_RETURN;
		sendCommand(cmdReturn);

		// 查表，找出所有对应的输入地址和输入编号
		RelationDao rdao = new RelationDao(UIUtils.getContext());
		List<RelationBean> list = rdao.findAllByOutput(outputAddr,
				outputObj);

		// 通知StateStorage
		StateStorage.getInstance().getUpdateInfoFromRF(outputAddr, outputObj,
				state);

		// 给关联的设备发送通知信息
		for (RelationBean bean : list) {
			final byte[] send = icmd.CMD_IO_Notice(bean.getInputAddr(),
					CommandUtil.getLocalAddr(), cmd[Data.P_PAN_KEY],
					bean.getInputObj(), 1, new byte[] { (byte) state });
			// 重发机制下的发送命令
			SerialPortSendThread.getInstance().getHandler()
					.post(new Runnable() {

						@Override
						public void run() {
							int addr = toUnsignedInt(send[Data.P_TARGET_ADDR]);
							
							// 地址不属于从机地址时
							if(!isAddrSlave(addr)){
								return;
							}
							
							int funcCode = toUnsignedInt(send[Data.P_FUNC_CODE]);
							checker.add(addr, Data.FUNC_CODE_IO_NOTICE_RETURN);

							int count = 5;
							while (checker.have(addr,
									Data.FUNC_CODE_IO_NOTICE_RETURN)
									&& count > 0) {
								sendCommandTypeSecond(send);
								count--;
							}
							
							if(count < 4){
								String log = "resend " + (4-count) + 
										" times, target address is " + addr +
										", function code is " + funcCode;
								FileLogUtil.printFileLog(log, Environment.getExternalStorageDirectory()
										+ "/huiyun/log.txt");
							}
						}
					});
		}

		// 如果属于同时兼具输入和输出类型的设备，还需要额外处理
		if (StateStorage.getInstance().getTypeByAddress(outputAddr) == Device.TYPE_OUT_WITH_IN) {
			List<RelationBean> lst = rdao.findAllByInput(outputAddr,
					outputObj);
			for (RelationBean bean : lst) {
				switch (StateStorage.getInstance().getTypeByAddress(
						bean.getOutputAddr())) {
				case Device.TYPE_GATEWAY:
					final byte[] send = icmd.CMD_CRTL_IO(bean.getOutputAddr(),
							CommandUtil.getLocalAddr(), cmd[Data.P_PAN_KEY], 0,
							2, new byte[] { (byte) bean.getOutputObj(),
									(byte) state });
					// 重发机制下的发送命令
					SerialPortSendThread.getInstance().getHandler()
							.post(new Runnable() {

								@Override
								public void run() {
									int addr = toUnsignedInt(send[Data.P_TARGET_ADDR]);
									
									// 地址不属于从机地址时
									if(!isAddrSlave(addr)){
										return;
									}
									
									int funcCode = toUnsignedInt(send[Data.P_FUNC_CODE]);
									checker.add(addr,
											Data.FUNC_CODE_IO_W_RETURN);

									int count = 5;
									while (checker.have(addr,
											Data.FUNC_CODE_IO_W_RETURN)
											&& count > 0) {
										sendCommandTypeSecond(send);
										count--;
									}
									
									if(count < 4){
										String log = "resend " + (4-count) + 
												" times, target address is " + addr +
												", function code is " + funcCode;
										FileLogUtil.printFileLog(log, Environment.getExternalStorageDirectory()
												+ "/huiyun/log.txt");
									}
								}
							});
					break;
				case Device.TYPE_OUT_WITH_IN:
					break;
				default:
					final byte[] send2 = icmd
							.CMD_CRTL_IO(bean.getOutputAddr(),
									CommandUtil.getLocalAddr(),
									cmd[Data.P_PAN_KEY], bean.getOutputObj(),
									1, new byte[] { (byte) state });
					// 重发机制下的发送命令
					SerialPortSendThread.getInstance().getHandler()
							.post(new Runnable() {

								@Override
								public void run() {
									int addr = toUnsignedInt(send2[Data.P_TARGET_ADDR]);
									
									// 地址不属于从机地址时
									if(!isAddrSlave(addr)){
										return;
									}
									
									int funcCode = toUnsignedInt(send2[Data.P_FUNC_CODE]);
									checker.add(addr,
											Data.FUNC_CODE_IO_W_RETURN);

									int count = 5;
									while (checker.have(addr,
											Data.FUNC_CODE_IO_W_RETURN)
											&& count > 0) {
										sendCommandTypeSecond(send2);
										count--;
									}
									
									if(count < 4){
										String log = "resend " + (4-count) + 
												" times, target address is " + addr +
												", function code is " + funcCode;
										FileLogUtil.printFileLog(log, Environment.getExternalStorageDirectory()
												+ "/huiyun/log.txt");
									}
								}
							});
					break;
				}
			}
		}

		rdao.close();
	}

	/**
	 * 处理开关写入反馈命令
	 */
	private void handleIOWriteReturnCommand(byte[] cmd) {
		// 密码不对,不进行处理
		if (!CommandUtil.checkPanKey(toUnsignedInt(cmd[Data.P_PAN_KEY]))) {
			return;
		}

		// 从检查器移除指定的记录
		int addr = toUnsignedInt(cmd[Data.P_LOCAL_ADDR]);
		checker.remove(addr, Data.FUNC_CODE_IO_W_RETURN);

		// 获取输出地址
		int outputAddr = toUnsignedInt(cmd[Data.P_LOCAL_ADDR]), outputObj = -1;
		switch (StateStorage.getInstance().getTypeByAddress(outputAddr)) {
		case Device.TYPE_GATEWAY:
			// 网关
			if (cmd.length > Data.P_DATA) {
				outputObj = toUnsignedInt(cmd[Data.P_DATA]);
			}
			break;
		case Device.TYPE_OUT_WITH_IN:
		case Device.TYPE_OUT:
			// 普通输出(如普通灯、窗帘)
			if (cmd.length > Data.P_OBJECT_NUM) {
				outputObj = toUnsignedInt(cmd[Data.P_OBJECT_NUM]);
			}
			break;
		case Device.TYPE_COLOR_LAMP:
			// 调色灯
			if (cmd.length > Data.P_OBJECT_NUM) {
				outputObj = toUnsignedInt(cmd[Data.P_OBJECT_NUM]);
			}
			break;
		default:
			// 默认
			if (cmd.length > Data.P_OBJECT_NUM) {
				outputObj = toUnsignedInt(cmd[Data.P_OBJECT_NUM]);
			}
			break;
		}

		// 查表，找出所有对应的输入地址和输入编号并发送出去
		RelationDao rdao = new RelationDao(UIUtils.getContext());
		List<RelationBean> list = rdao.findAllByOutput(outputAddr,
				outputObj);

		// 获取队列中第一个匹配的，并获得其开关量
		int state = -1;
		for (RelationBean bean : list) {
			state = queue.getVal(bean.getInputAddr(), bean.getInputObj(),
					outputAddr, outputObj);
			if (state > -1) {
				break;
			}
		}

		// 通知StateStorage
		if (state == -1) {
			return;
		}
		StateStorage.getInstance().getUpdateInfoFromRF(outputAddr, outputObj,
				state);

		// 逐个发送通知或者返回命令
		byte[] send;
		for (RelationBean bean : list) {
			if (queue.contain(bean.getInputAddr(), bean.getInputObj(),
					outputAddr, outputObj, state)) {
				/* 如果该输入在队列中，说明当初开关写入命令是由该输入发出的，那么就得给该输入发送返回命令 */
				// 从队列中删除
				queue.delete(bean.getInputAddr(), bean.getInputObj(),
						outputAddr, outputObj, state);

				// 发送信息
				send = icmd.CMD_Ask_CRTL_IO(bean.getInputAddr(),
						CommandUtil.getLocalAddr(), cmd[Data.P_PAN_KEY],
						bean.getInputObj(), 1, new byte[] { (byte) state });
				sendCommand(send);
			} else {
				/* 如果该输入不在队列中，说明写入命令不是由该输入发出，但由于该输入与输出有关联，需要给它发送开关变更通知命令 */
				// 发送信息
				final byte[] send2 = icmd.CMD_IO_Notice(bean.getInputAddr(),
						CommandUtil.getLocalAddr(), cmd[Data.P_PAN_KEY],
						bean.getInputObj(), 1, new byte[] { (byte) state });
				// 重发机制下的发送命令
				SerialPortSendThread.getInstance().getHandler()
						.post(new Runnable() {

							@Override
							public void run() {
								int addr = toUnsignedInt(send2[Data.P_TARGET_ADDR]);
								
								// 地址不属于从机地址时
								if(!isAddrSlave(addr)){
									return;
								}
								
								int funcCode = toUnsignedInt(send2[Data.P_FUNC_CODE]);
								checker.add(addr,
										Data.FUNC_CODE_IO_NOTICE_RETURN);

								int count = 5;
								while (checker.have(addr,
										Data.FUNC_CODE_IO_NOTICE_RETURN)
										&& count > 0) {
									sendCommandTypeSecond(send2);
									count--;
								}
								
								if(count < 4){
									String log = "resend " + (4-count) + 
											" times, target address is " + addr +
											", function code is " + funcCode;
									FileLogUtil.printFileLog(log, Environment.getExternalStorageDirectory()
											+ "/huiyun/log.txt");
								}
							}
						});
			}
		}

		// 如果属于同时兼具输入和输出类型的设备，还需要额外处理
		if (StateStorage.getInstance().getTypeByAddress(outputAddr) == Device.TYPE_OUT_WITH_IN) {
			List<RelationBean> lst = rdao.findAllByInput(outputAddr,
					outputObj);
			for (RelationBean bean : lst) {
				switch (StateStorage.getInstance().getTypeByAddress(
						bean.getOutputAddr())) {
				case Device.TYPE_GATEWAY:
					final byte[] send2 = icmd.CMD_CRTL_IO(bean.getOutputAddr(),
							CommandUtil.getLocalAddr(), cmd[Data.P_PAN_KEY], 0,
							2, new byte[] { (byte) bean.getOutputObj(),
									(byte) state });
					// 重发机制下的发送命令
					SerialPortSendThread.getInstance().getHandler()
							.post(new Runnable() {

								@Override
								public void run() {
									int addr = toUnsignedInt(send2[Data.P_TARGET_ADDR]);
									
									// 地址不属于从机地址时
									if(!isAddrSlave(addr)){
										return;
									}
									
									int funcCode = toUnsignedInt(send2[Data.P_FUNC_CODE]);
									checker.add(addr,
											Data.FUNC_CODE_IO_W_RETURN);

									int count = 5;
									while (checker.have(addr,
											Data.FUNC_CODE_IO_W_RETURN)
											&& count > 0) {
										sendCommandTypeSecond(send2);
										count--;
									}
									
									if(count < 4){
										String log = "resend " + (4-count) + 
												" times, target address is " + addr +
												", function code is " + funcCode;
										FileLogUtil.printFileLog(log, Environment.getExternalStorageDirectory()
												+ "/huiyun/log.txt");
									}
								}
							});
					break;
				case Device.TYPE_OUT_WITH_IN:
					break;
				default:
					final byte[] send3 = icmd
							.CMD_CRTL_IO(bean.getOutputAddr(),
									CommandUtil.getLocalAddr(),
									cmd[Data.P_PAN_KEY], bean.getOutputObj(),
									1, new byte[] { (byte) state });
					// 重发机制下的发送命令
					SerialPortSendThread.getInstance().getHandler()
							.post(new Runnable() {

								@Override
								public void run() {
									int addr = toUnsignedInt(send3[Data.P_TARGET_ADDR]);
									
									// 地址不属于从机地址时
									if(!isAddrSlave(addr)){
										return;
									}
									
									int funcCode = toUnsignedInt(send3[Data.P_FUNC_CODE]);
									checker.add(addr,
											Data.FUNC_CODE_IO_W_RETURN);

									int count = 5;
									while (checker.have(addr,
											Data.FUNC_CODE_IO_W_RETURN)
											&& count > 0) {
										sendCommandTypeSecond(send3);
										count--;
									}
									
									if(count < 4){
										String log = "resend " + (4-count) + 
												" times, target address is " + addr +
												", function code is " + funcCode;
										FileLogUtil.printFileLog(log, Environment.getExternalStorageDirectory()
												+ "/huiyun/log.txt");
									}
								}
							});
					break;
				}
			}
		}

		rdao.close();
	}

	/**
	 * 处理开关读出反馈命令
	 */
	private void handleIOReadReturnCommand(byte[] cmd) {
		// PAN KEY不对，不作任何处理
		if (!CommandUtil.checkPanKey(toUnsignedInt(cmd[Data.P_PAN_KEY]))) {
			return;
		}

		// 处理读取返回
		int outputAddr = toUnsignedInt(cmd[Data.P_LOCAL_ADDR]);
		int outputObj, type;
		type = StateStorage.getInstance().getTypeByAddress(outputAddr);
		switch (type) {
		case Device.TYPE_GATEWAY:
			// 网关
			outputObj = toUnsignedInt(cmd[Data.P_DATA]);
			break;
		case Device.TYPE_OUT_WITH_IN:
		case Device.TYPE_OUT:
		case Device.TYPE_COLOR_LAMP:
			// 调色灯 & 普通输出(如普通灯、窗帘)
			outputObj = toUnsignedInt(cmd[Data.P_OBJECT_NUM]);
			break;
		default:
			outputObj = toUnsignedInt(cmd[Data.P_OBJECT_NUM]);
			break;
		}

		int state;
		Device dev = StateStorage.getInstance()
				.readState(outputAddr, outputObj);
		if (dev != null) {
			switch (dev.getType()) {
			case Device.TYPE_GATEWAY:
				// 网关
				state = toUnsignedInt(cmd[Data.P_DATA + 1]);
				StateStorage.getInstance().getUpdateInfoFromRF(outputAddr,
						outputObj, state);
				break;
			case Device.TYPE_OUT:
				// 普通输出(如普通灯、窗帘)
				state = toUnsignedInt(cmd[Data.P_DATA]);
				StateStorage.getInstance().getUpdateInfoFromRF(outputAddr,
						outputObj, state);
				break;
			case Device.TYPE_COLOR_LAMP:
				// 调色灯
				if (cmd.length >= Data.P_DATA + 3) {
					state = ColorState.getRGBValue(
							toUnsignedInt(cmd[Data.P_DATA + 1]),
							toUnsignedInt(cmd[Data.P_DATA + 2]),
							toUnsignedInt(cmd[Data.P_DATA]));
				} else {
					state = toUnsignedInt(cmd[Data.P_DATA]);
				}
				StateStorage.getInstance().getUpdateInfoFromRF(outputAddr,
						outputObj, state);
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 处理开关变更通知反馈命令
	 */
	private void handleIONoticeReturnCommand(byte[] cmd) {
		// 密码不对,不进行处理
		if (!CommandUtil.checkPanKey(toUnsignedInt(cmd[Data.P_PAN_KEY]))) {
			return;
		}

		// 从检查器移除指定的记录
		int addr = toUnsignedInt(cmd[Data.P_LOCAL_ADDR]);
		checker.remove(addr, Data.FUNC_CODE_IO_NOTICE_RETURN);
	}

	/**
	 * 处理设置新地址的反馈命令
	 */
	private void handleSetAddrReturnCommand(byte[] cmd) {
		AddrAllocator.getInstance().handleSetAddrReturnCommand(cmd);
	}

	/**
	 * 处理网关的设置写入反馈命令
	 */
	private void handleGatewaySetFunReturnCommand(byte[] cmd) {
		// PAN KEY不对，不作任何处理
		if (!CommandUtil.checkPanKey(toUnsignedInt(cmd[Data.P_PAN_KEY]))) {
			return;
		}

		// 首先得确认下命令是否由网关返回的
		int addr = toUnsignedInt(cmd[Data.P_LOCAL_ADDR]);
		DeviceDao ddao = new DeviceDaoImpl(UIUtils.getContext());
		// 根据地址获取对应的device
		List<Device> listDev = ddao.getAllDevicesByAddress(addr);
		if (listDev == null)
			return;
		if (listDev.size() < 1)
			return;
		// 查看device的类型，如果不是网关直接结束判断
		if (listDev.get(0).getType() != 12)
			return;
		// 设定类型不是2时直接结束判断
		if (cmd.length < 7)
			return;
		if (toUnsignedInt(cmd[5]) != 2)
			return;
		// 命令处理
		int count = toUnsignedInt(cmd[6]);
		if (count == 0) {
			// 说明网关配置已被清空
			for (Device dev : listDev) {
				if (dev.getNumber() != 0) {
					ddao.deleteDevice(dev);
				}
			}
		} else if (count > listDev.size() - 1) {
			// 数量大于当前数据库存到的指定的网关device数量时（不包括编号为0的网关本身）
			for (int i = listDev.size(); i < count + 1; i++) {
				Device dev = new Device();
				dev.setAddress(listDev.get(0).getAddress());
				dev.setChipId(listDev.get(0).getChipId());
				dev.setType(12);
				dev.setName("网关");
				dev.setDetails("网关");
				dev.setNumber(i);
				listDev.add(dev);
				MainActivity.showString("添加新的网关设备:" + dev.toString(),
						MultiTextBuffer.TYPE_DEVICE);
			}
			ddao.saveOrUpdateNoPhoneCodeDevices(listDev);
			// 给新加入的设备分配手机按键码
			PhoneCodeAllocator.allocatePhoneCode();
		}

		// 最后处理一下配置信息
		GatewayProcessor.getInstance().processSetFuncRtnCommandFromRF(cmd);
	}

	/**
	 * 处理网关的设置读取反馈命令
	 */
	private void handleGatewayReadFunReturnCommand(byte[] cmd) {
		// PAN KEY不对，不作任何处理
		if (!CommandUtil.checkPanKey(toUnsignedInt(cmd[Data.P_PAN_KEY]))) {
			return;
		}

		// 首先得确认下命令是否由网关返回的
		int addr = toUnsignedInt(cmd[Data.P_LOCAL_ADDR]);
		DeviceDao ddao = new DeviceDaoImpl(UIUtils.getContext());
		// 根据地址获取对应的device
		List<Device> listDev = ddao.getAllDevicesByAddress(addr);
		if (listDev == null)
			return;
		if (listDev.size() < 1)
			return;
		// 查看device的类型，如果不是网关直接结束判断
		if (listDev.get(0).getType() != 12)
			return;
		// 设定类型不是2时直接结束判断
		if (toUnsignedInt(cmd[5]) != 2)
			return;
		// 交给网关配置信息处理器处理
		GatewayProcessor.getInstance().processReadFuncRtnCommandFromRF(cmd);
	}

	/**
	 * 处理设备信息命令1(即搜索设备的反馈命令)
	 */
	private void handleDeviceInfoCommand(byte[] cmd) {
		int localAddr = toUnsignedInt(cmd[Data.P_LOCAL_ADDR]);
		if (localAddr == Data.ADDR_SELF) {
			// 返回的是主机自己的信息时
			for (int i = 0; i < Data.LENGTH_DEV_INFO; i++) {
				CommandUtil.deviceInfo[i] = cmd[3 + i];
			}
			for (int i = 0; i < Data.LENGTH_CPUID; i++) {
				CommandUtil.myCPUID[i] = cmd[3 + Data.LENGTH_DEV_INFO + i];
			}

			StaticValues.MASTER_ID = // 存入static value的chipId里
			BytesStringUtils.toStringShowWithoutSpace(cmd,
					3 + Data.LENGTH_DEV_INFO, Data.LENGTH_CPUID);
			SpUtils.save("masterId", StaticValues.MASTER_ID); // 同时存入sp里
			MainActivity.showString("搜索后得知，主机的芯片ID为" + StaticValues.MASTER_ID,
					MultiTextBuffer.TYPE_DEVICE);
			// 给MCU发送修改本机地址的命令
			byte[] cmdSend = icmd.CMD_Set_Target_Addr(
					CommandUtil.getLocalAddr(), CommandUtil.getLocalAddr(),
					CommandUtil.myCPUID, CommandUtil.getPanKey());
			sendCommand(cmdSend);
		}

		if (!agingSwitch1.isOpen()) {
			return;
		}

		if (localAddr != Data.ADDR_SELF) {
			// 返回的是从机设备时
			byte[] chipId = icmd.GetChipId_CMD(cmd); // 芯片id
			int addr = BytesStringUtils.toUnsignedInt(cmd[Data.P_LOCAL_ADDR]); // 设备地址
			if (chipId == null)
				return;

			if (AddrAllocator.getInstance().hasAddr(chipId, addr)) {
				// 该设备有地址时
				MainActivity.showString(
						"芯片ID为"
								+ BytesStringUtils.toStringShow(icmd
										.GetChipId_CMD(cmd)) + "的设备已分配过地址",
						MultiTextBuffer.TYPE_DEVICE);
				DeviceInfoProcessor.handleDeviceInfoCmd(cmd);
			} else {
				// 该设备无地址时，给他分配新地址
				MainActivity.showString(
						"芯片ID为"
								+ BytesStringUtils.toStringShow(icmd
										.GetChipId_CMD(cmd))
								+ "的设备未分配地址，即将给它分配一个地址",
						MultiTextBuffer.TYPE_DEVICE);
				AddrAllocator.getInstance().setOnAddrAllocateListener(
						new OnResultListener<AddrAllocator.Box>() {

							@Override
							public void onResult(boolean isSucceed, AddrAllocator.Box box) {
								// 生成新的搜索返回命令
								byte[] send = icmd.CMD_Ask_Search(
										CommandUtil.getLocalAddr(), box.addr,
										box.devInfo, box.chipId,
										CommandUtil.getPanKey());
								// 改好地址后发送新的搜索返回命令
								MainActivity.showString(
										"给芯片ID为"
												+ BytesStringUtils
														.toStringShow(box.chipId)
												+ "、设备信息为"
												+ BytesStringUtils
														.toStringShow(box.devInfo)
												+ "的设备分配的地址为" + box.addr,
										MultiTextBuffer.TYPE_DEVICE);
								DeviceInfoProcessor.handleDeviceInfoCmd(send);
							}
						});
				AddrAllocator.getInstance()
						.handleSearchOrHandReturnCommand(cmd);
			}
		}
	}

	/**
	 * 处理设备信息命令2(即手动确认的反馈命令)
	 */
	private void handleDeviceHandReturnCommand(byte[] cmd) {
		if (!agingSwitch2.isOpen()) {
			return;
		}

		try {
			byte[] chipId = icmd.GetChipId_CMD(cmd); // 芯片id
			int addr = toUnsignedInt(cmd[Data.P_LOCAL_ADDR]); // 设备地址
			if (chipId == null) {
				MainActivity
						.showString("获得芯片id失败", MultiTextBuffer.TYPE_DEVICE);
				return;
			}
			if (AddrAllocator.getInstance().hasAddr(chipId, addr)) {
				// 该设备有地址时
				MainActivity.showString(
						"芯片ID为"
								+ BytesStringUtils.toStringShow(icmd
										.GetChipId_CMD(cmd)) + "的设备已分配过地址",
						MultiTextBuffer.TYPE_DEVICE);
				DeviceInfoProcessor.handleDeviceInfoCmd(cmd);
			} else {
				// 该设备无地址时，给他分配新地址
				MainActivity.showString(
						"芯片ID为"
								+ BytesStringUtils.toStringShow(icmd
										.GetChipId_CMD(cmd))
								+ "的设备未分配地址，即将给它分配一个地址",
						MultiTextBuffer.TYPE_DEVICE);
				AddrAllocator.getInstance().setOnAddrAllocateListener(
						new OnResultListener<AddrAllocator.Box>() {

							@Override
							public void onResult(boolean isSucceed, AddrAllocator.Box box) {
								try {
									// 生成新的搜索返回命令
									byte[] send = icmd.CMD_Ask_Search(
											CommandUtil.getLocalAddr(),
											box.addr, box.devInfo, box.chipId,
											CommandUtil.getPanKey());
									// 改好地址后发送新的搜索返回命令
									MainActivity.showString(
											"给芯片ID为"
													+ BytesStringUtils
															.toStringShow(box.chipId)
													+ "、设备信息为"
													+ BytesStringUtils
															.toStringShow(box.devInfo)
													+ "的设备分配的地址为" + box.addr,
											MultiTextBuffer.TYPE_DEVICE);
									DeviceInfoProcessor
											.handleDeviceInfoCmd(send);
								} catch (Exception e) {
									MainActivity.showString(
											"出错原因：" + e.getMessage() + "\n"
													+ e.getLocalizedMessage(),
											MultiTextBuffer.TYPE_DEVICE);
								}
							}
						});
				AddrAllocator.getInstance()
						.handleSearchOrHandReturnCommand(cmd);
			}
		} catch (Exception e) {
			MainActivity.showString(
					"出错原因：" + e.getMessage() + "\n" + e.getLocalizedMessage(),
					MultiTextBuffer.TYPE_DEVICE);
		}
	}

	/**
	 * 处理心跳命令
	 */
	private void handleHeartBeatCommand(byte[] cmd) {
		// 心跳返回命令    如果网络不可用则
		if (!NetWorkHandler.IS_NOT_NET) {
			byte[] send = icmd.CMD_Heartbeat_Return(CommandUtil.getLocalAddr());
			sendCommand(send);
		}
	}

	/**
	 * 发送串口命令,同时向所有射频发送几乎同样的命令
	 */
	private void sendCommand(byte[] cmd) {
		int targetAddr = toUnsignedInt(cmd[Data.P_TARGET_ADDR]);
		if (targetAddr != Data.ADDR_WIFI) {
			// 目标地址不是移动端时，发送串口命令
			SerialPortConnect.getInstance().send(Data.CUSTOM_CODE_RF, cmd);
		}
	}

	/**
	 * 发送串口命令,同时向所有射频发送几乎同样的命令 不会自主转移到串口发送线程
	 */
	public void sendCommandTypeSecond(byte[] cmd) {
		// 发送串口命令
		SerialPortConnect.getInstance().sendWithoutInterval(
				Data.CUSTOM_CODE_RF, cmd);
		SystemClock.sleep(SerialPortConnect.COMMAND_SEND_INTERVAL);
	}

	private boolean isAddrSlave(int addr){
		if (addr >= Data.ADDR_SLAVE_MIN && addr <= Data.ADDR_SLAVE_MAX &&
			addr != Data.ADDR_WIFI) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * 将byte转换成无符号的int型
	 */
	private static int toUnsignedInt(byte b) {
		int i = b;
		if (i < 0)
			i += 256;
		return i;
	}
}
