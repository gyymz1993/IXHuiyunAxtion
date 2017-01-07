package com.huiyun.ixhuiyunaxtion.master.cmd;


import com.huiyun.ixhuiyunaxtion.axtion.serialport.SerialPortConnect;
import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;
import com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer;
import com.huiyun.ixhuiyunaxtion.master.bean.GatewayConfigure;
import com.huiyun.ixhuiyunaxtion.master.bean.table.Device;
import com.huiyun.ixhuiyunaxtion.master.cmd.command.CMD;
import com.huiyun.ixhuiyunaxtion.master.cmd.command.Data;
import com.huiyun.ixhuiyunaxtion.master.cmd.command.ICMD;
import com.huiyun.ixhuiyunaxtion.master.dao.DeviceDao;
import com.huiyun.ixhuiyunaxtion.master.dao.impl.DeviceDaoImpl;
import com.huiyun.ixhuiyunaxtion.master.inner.OnResultListener;
import com.huiyun.ixhuiyunaxtion.master.utils.BytesStringUtils;
import com.huiyun.ixhuiyunaxtion.master.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 处理网关配置信息
 * @author lzn
 * @date 2015-1-16 上午9:36:13 
 * @version V1.0   
 */
public class GatewayProcessor {

	private static GatewayProcessor instance;
	
	 /**
	 *  Function: 单例模式
	 *  @author lzn 
	 *  2015-1-16 上午10:22:08
	 *  @return
	 */
	public static GatewayProcessor getInstance(){
		synchronized (GatewayProcessor.class) {
			if(instance == null){
				instance = new GatewayProcessor();
			}
			
			return instance;
		}
	}
	private GatewayProcessor(){
		
	}
	
	private OnResultListener<GatewayConfigure> listener;
	private CMD icmd = new ICMD();
	
	
	/**
	 * 准备列表，命令成功发送后将配置从该列表移到listConfirm
	 */
	private List<GatewayConfigure> listPrepare = new ArrayList<GatewayConfigure>();
	
	
	/**
	 * 确认列表，命令确认返回后将配置从该列表移除
	 */
	private List<GatewayConfigure> listConfirm = new ArrayList<GatewayConfigure>();
	
	
	 /**
	 *  Function: 设置监听
	 *  添加网关配置成功和清空网关配置成功时有回调
	 *  @author lzn 
	 *  2015-1-16 上午9:37:40
	 *  @param listener
	 */
	public void setOnResultListener(OnResultListener<GatewayConfigure> listener){
		this.listener = listener;
	}
	
	
	 /**
	 *  Function: 添加网关配置
	 *  @author lzn 
	 *  2015-1-16 上午9:50:51
	 *  @param configure 网关配置信息，属性除了number都需要填写
	 */
	public void addConfigure(GatewayConfigure configure){
		// 请求查询网关内的配置信息数量
		queryConfigureCount(configure.getAddress());
		// 把配置信息加入等待队列
		if(!listPrepare.contains(configure)){
			listPrepare.add(configure);
		}
	}
	
	
	 /**
	 *  Function: 清空网关配置
	 *  @author lzn 
	 *  2015-1-16 上午9:51:07
	 *  @param gatewayAddr 网关地址
	 */
	public void cleanConfigure(int gatewayAddr){
		byte[] cmd = icmd.CMD_Set_Func(gatewayAddr, CommandUtil.getLocalAddr(), 
				CommandUtil.getPanKey(), 0, 2, new byte[]{2, 0});
		SerialPortConnect.getInstance().send(Data.CUSTOM_CODE_RF, cmd);
	}
	
	
	 /**
	 *  Function: 查询网关配置信息的数量
	 *  @author lzn 
	 *  2015-1-16 上午10:04:51
	 *  @param gatewayAddr 网关地址
	 */
	private void queryConfigureCount(int gatewayAddr){
		byte[] cmd = icmd.CMD_Read_Func(gatewayAddr, CommandUtil.getLocalAddr(), 
				CommandUtil.getPanKey(), 0, 2, new byte[]{2, 1});
		SerialPortConnect.getInstance().send(Data.CUSTOM_CODE_RF, cmd);
	}
	
	
	 /**
	 *  Function: 处理设置功能写入的返回命令
	 *  @author lzn 
	 *  2015-1-16 上午10:19:10
	 *  @param cmd
	 */
	public void processSetFuncRtnCommandFromRF(byte[] cmd){
		// 获得地址和编号
		int addr = BytesStringUtils.toUnsignedInt(cmd[Data.P_LOCAL_ADDR]);
		int number = BytesStringUtils.toUnsignedInt(cmd[6]);
		
		if(number == 0){
			// 编号为0，说明配置被清空，返回个除了地址外其它属性全为0的配置对象
			GatewayConfigure gc = new GatewayConfigure();
			gc.setAddress(addr);
			gc.setNumber(0);
			gc.setCommType(0);
			gc.setDetailType(0);
			gc.setExecId(0);
			gc.setSwitchId(0);
			MainActivity.showString("地址为" + addr + "的网关的配置已被清空",
					MultiTextBuffer.TYPE_DEVICE);
			if(listener != null){
				// 回调，表示清空成功
				listener.onResult(true, gc);
			}
		} else {
			for(GatewayConfigure config:listConfirm){
				// 找到对应地址和编号的配置
				if(config.getAddress() == addr && config.getNumber() == number){
					// 更新数据库中对应的设备的区域和名称
					DeviceDao ddao = new DeviceDaoImpl(UIUtils.getContext());
					List<Device> allDev = ddao.getAllDevicesByAddress(addr);
					for(Device dev:allDev){
						if(dev.getNumber() == number){
							dev.setDetails(config.getDetails());
							dev.setName(config.getName());
							MainActivity.showString(
								"GatewayProcessor更新了网关设备信息：" + dev.toString(),
								MultiTextBuffer.TYPE_DEVICE);
						}
					}
					ddao.saveOrUpdateDevices(allDev);
					
					if(listener != null){
						// 回调，表示修改成功
						listener.onResult(true, config);
					}
					// 从确认列表中移除
					listConfirm.remove(config);
					return;
				}
			}
		}
	}
	
	
	 /**
	 *  Function:处理设置功能读取的返回命令
	 *  @author lzn 
	 *  2015-1-16 上午10:50:03
	 *  @param cmd
	 */
	public void processReadFuncRtnCommandFromRF(byte[] cmd){
		synchronized (GatewayConfigure.class) {
			// 网关地址
			int addr = BytesStringUtils.toUnsignedInt(cmd[Data.P_LOCAL_ADDR]);
			// 当前总编号数
			int number = BytesStringUtils.toUnsignedInt(cmd[6]);
			// 找出对应网关地址的所有配置信息
			List<GatewayConfigure> listAdd = new ArrayList<GatewayConfigure>();
			for (GatewayConfigure config : listPrepare) {
				if(config.getAddress() == addr){
					listAdd.add(config);
				}
			}
			// 将找出的配置信息移出准备队列
			listPrepare.removeAll(listAdd); 
			// 逐个发送配置写入命令
			byte[] send;
			for(GatewayConfigure config: listAdd){
				number++;
				config.setNumber(number);
				send = icmd.CMD_Set_Func(addr, CommandUtil.getLocalAddr(), 
						CommandUtil.getPanKey(), 0, 6, new byte[]{
					2, (byte) config.getNumber(), 
					(byte) config.getCommType(), (byte) config.getDetailType(), 
					(byte) config.getExecId(), (byte) config.getSwitchId()
				});
				SerialPortConnect.getInstance().send(Data.CUSTOM_CODE_RF, send);
			}
			// 将找出的配置信息移入确认队列
			listConfirm.addAll(listAdd);
		}
	}
}
