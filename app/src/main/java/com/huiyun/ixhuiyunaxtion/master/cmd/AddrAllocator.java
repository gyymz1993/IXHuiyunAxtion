package com.huiyun.ixhuiyunaxtion.master.cmd;

import com.huiyun.ixhuiyunaxtion.axtion.serialport.SerialPortConnect;
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
 * 地址分配器
 * 专门负责给设备分配新地址
 * 单例模式
 * 
 * @author lzn
 *
 */
public class AddrAllocator {
	
	private List<Box> listBox;
	private CMD icmd = new ICMD();
	private OnResultListener<Box> listener;
	
	private static AddrAllocator instance;
	/** 单例模式
	 * @return
	 */
	public static AddrAllocator getInstance(){
		synchronized (AddrAllocator.class) {
			if(instance == null){
				instance = new AddrAllocator();
			}
			
			return instance;
		}
	}
	private AddrAllocator(){
		listBox = new ArrayList<Box>();
	}
	
	/** 获取一个新的地址，该地址不能与数据库的重复，而且也必须在指定的范围内
	 * @return
	 */
	private int getNewSlaveAddr(){
		DeviceDao ddao = new DeviceDaoImpl(UIUtils.getContext());
		List<Integer> list = ddao.getAllAddress(); // 从数据库中获取所有地址
		for(int addr = Data.ADDR_SLAVE_MIN; addr<= Data.ADDR_SLAVE_MAX; addr++){
			if(!list.contains(Integer.valueOf(addr)) && 
					!listBox.contains(new Box(addr))){
				// 如果该地址未被使用，返回该地址
				return addr;
			}
		}
		
		// 如果所有范围内的地址都被使用，返回0
		return 0;
	}
	
	/** 处理搜索返回命令和手动确认返回命令
	 * @param cmd
	 */
	public void handleSearchOrHandReturnCommand(byte[] cmd){
		synchronized (AddrAllocator.class) {
			byte[] chipId = icmd.GetChipId_CMD(cmd); // 芯片id
			int addr = BytesStringUtils.toUnsignedInt(cmd[Data.P_LOCAL_ADDR]); // 设备地址
			if (!hasAddr(chipId,addr)) {
				// 如果已经在分配列表内，重新发送地址变更命令
				for(Box box:listBox){
					if(addr == box.addr &&
							BytesStringUtils.compareByteArray(chipId, box.chipId)){
						// 发送串口命令以更新地址
						byte[] cmdNewAddr = icmd.CMD_Set_Target_Addr(CommandUtil.getLocalAddr(), 
								box.addr, box.chipId, CommandUtil.getPanKey());
						SerialPortConnect.getInstance().send(Data.CUSTOM_CODE_RF, cmdNewAddr);
						return;
					}
				}
				
				// 如果不在分配列表内，分配个新地址给它
				Box box = new Box();
				box.addr = getNewSlaveAddr();
				box.devInfo = icmd.GetDevInfo_CMD(cmd);
				box.chipId = icmd.GetChipId_CMD(cmd);
				// 添加到box列表里
				listBox.add(box);
				// 发送串口命令以更新地址
				byte[] cmdNewAddr = icmd.CMD_Set_Target_Addr(CommandUtil.getLocalAddr(), 
						box.addr, box.chipId, CommandUtil.getPanKey());
				SerialPortConnect.getInstance().send(Data.CUSTOM_CODE_RF, cmdNewAddr);
			}
		}
	}
	
	/** 处理设置地址返回命令
	 * @param cmd
	 */
	public void handleSetAddrReturnCommand(byte[] cmd){
		synchronized (AddrAllocator.class) {
			// 找到对应地址的box，将其回调并从列表中移除
			int addr = BytesStringUtils.toUnsignedInt(cmd[Data.P_LOCAL_ADDR]);
			for(Box b:listBox){
				if(addr == b.addr){
					if(listener != null){
						listener.onResult(true, b);
					}
					listBox.remove(b);
					return;
				}
			}
		}
	}
	
	/** 判断设备是否已分配地址
	 * @param cmd
	 * @return
	 */
	public boolean hasAddr(byte[] chipId, int addr){
		if(addr >= Data.ADDR_SLAVE_MIN && addr <= Data.ADDR_SLAVE_MAX){
			// 如果数据库里存在地址和芯片id都相同的记录，返回true；否则，返回false
			DeviceDao ddao = new DeviceDaoImpl(UIUtils.getContext());
			// 从数据库中找记录
			List<Device> listDev = ddao.queryAllDevices();
			if (listDev != null) {
				for (Device dev : listDev) {
					if (addr == dev.getAddress()
							&& BytesStringUtils.compareByteArray(chipId,
									dev.getChipIds())) {
						// 找到记录，返回true
						return true;
					}
				}
			}
			
			// 没有记录，返回false
			return false;
		} else {
			// 设备地址不在规定的范围内，返回false
			return false;
		}
	}
	
	public void setOnAddrAllocateListener(OnResultListener<Box> listener) {
		this.listener = listener;
	}

	public class Box{
		public int addr;
		public byte[] devInfo;
		public byte[] chipId;
		
		public Box(){
			
		}
		
		public Box(int addr){
			this.addr = addr;
		}
		
		@Override
		public int hashCode() {
			return addr;
		}
		
		@Override
		public boolean equals(Object o) {
			if(!(o instanceof Box))
				return false;
			Box b = (Box) o;
			return b.addr == this.addr;
		}
		
	}
}
