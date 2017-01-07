package com.huiyun.ixhuiyunaxtion.master.cmd.command;

/**
 * 命令的声明
 * 2014.10.23  修改了CMD_IO_Notice()和CMD_Ask_IO_Notice()的功能码
 * 2014.10.30  目标地址位、本机地址位、功能码位改成用常量来标识
 * 2014.11.27  修改了所有的开关命令和设置命令
 * 2014.12.24  添加服务器信息登陆命令
 * 2015.1.8    添加红外控制相关的命令
 * 2015.1.12   添加获取红外码的方法
 */
public interface CMD {
	// 搜索设备及其返回
	public byte[] CMD_Search_Device(int local_addr, int type);
	public byte[] CMD_Ask_Search(int target_addr, int local_addr, byte[] device_info, byte[] CPUID, int key);
	
	// 给目标设备设置地址及其返回
	public byte[] CMD_Set_Target_Addr(int local_addr, int new_addr, byte[] CPUID, int key);
	public byte[] CMD_Ask_Set_Addr(int target_addr, int local_addr);
	
	// 使目标设备闪烁及其返回
	public byte[] CMD_Device_Blink(int local_addr, byte[] CPUID);
	public byte[] CMD_Ask_Device_Blink(int target_addr, int local_addr, byte[] CPUID);
	
	// 让目标设备手动确认及其返回
	public byte[] CMD_Set_Hand_Mode(int local_addr);
	public byte[] CMD_Hand_Mode_Device_Data(int target_addr, int local_addr, byte[] CPUID);
	
	// 开关量写入命令及其返回
	public byte[] CMD_CRTL_IO(int target_addr, int local_addr, int key,
							  int object, int data_size, byte[] data);
	public byte[] CMD_Ask_CRTL_IO(int target_addr, int local_addr, int key,
								  int object, int data_size, byte[] data);
	
	// 开关量读取命令及其返回
	public byte[] CMD_Read_IO(int target_addr, int local_addr, int key,
							  int object, int data_size, byte[] data);
	public byte[] CMD_Ask_Read_IO(int target_addr, int local_addr, int key,
								  int object, int data_size, byte[] data);
	
	// 开关量变更通知及其返回
	public byte[] CMD_IO_Notice(int target_addr, int local_addr, int key,
								int object, int data_size, byte[] data);
	public byte[] CMD_Ask_IO_Notice(int target_addr, int local_addr, int key,
									int object, int data_size, byte[] data);
	
	// 修改目标设备的功能设置及其返回
	public byte[] CMD_Set_Func(int target_addr, int local_addr, int key,
							   int object, int data_size, byte[] data);
	public byte[] CMD_Ask_Set_Func(int target_addr, int local_addr, int key,
								   int object, int data_size, byte[] data);
	
	// 读取目标设备的功能设置及其返回
	public byte[] CMD_Read_Func(int target_addr, int local_addr, int key,
								int object, int data_size, byte[] data);
	public byte[] CMD_Ask_Read_Func(int target_addr, int local_addr, int key,
									int object, int data_size, byte[] data);
	
	// 登陆服务器时发送的登陆信息
	public byte[] CMD_LOG_IN(int local_addr, byte[] CPUID);
	
	/** 得到Data
	 * @param cmd
	 * @return
	 */
	public byte[] GetData_CMD(byte[] cmd);
	
	/** 得到设备信息，仅限命令为搜索返回命令或手动确认命令
	 * @param cmd
	 * @return
	 */
	public byte[] GetDevInfo_CMD(byte[] cmd);
	
	/** 得到芯片id，仅限命令为搜索返回命令或手动确认命令
	 * @param cmd
	 * @return
	 */
	public byte[] GetChipId_CMD(byte[] cmd);
	
	/**
	 * 开启/关闭学习通道(旧的)
	 * @param local_addr 本主机的地址
	 * @param key 密码值
	 * @param switchVal 开启1/关闭0
	 * @return
	 */
	public byte[] CMD_Learn_Passway(int my_addr, int key, int switchVal);
	
	/**
	 * 学习红外遥控码的返回(旧的)
	 * @param my_addr 本主机的地址
	 * @return
	 */
	public byte[] CMD_Ask_Ray_Learn(int my_addr);
	
	/**
	 * 发送红外遥控码(旧的)
	 * @param target_addr 红外转发器的地址
	 * @param local_addr 本主机的地址
	 * @param key 密码值
	 * @param data_size 红外码的长度
	 * @param data 红外码的内容
	 * @return
	 */
	public byte[] CMD_Ray_Control(int target_addr, int local_addr, int key,
								  int data_size, byte[] data);
	
	/** 根据学习红外码的返回命令获得红外码(旧的)
	 * @param cmd
	 * @return
	 */
	public byte[] GetData_RayCode(byte[] cmd);
	
	/** 红外码学习
	 * @param cmd
	 * @return
	 */
	public byte[] CMD_RedCode_Learn(int target_addr, int local_addr, int key, int number);
	
	/** 红外码发送
	 * @param cmd
	 * @return
	 */
	public byte[] CMD_RedCode_Control(int target_addr, int local_addr, int key, int number);
	
	 /**
	 *  Function: WIFI状态命令
	 *  @author lzn 
	 *  2015-3-16 上午11:07:00
	 *  @param connected
	 *  @return
	 */
	public byte[] CMD_WIFI_State(int local_addr, boolean connected);
	
	
	 /**
	 *  Function:心跳包返回
	 *  @author lzn 
	 *  2015-4-30 下午2:32:15
	 *  @param local_addr
	 *  @return
	 */
	public byte[] CMD_Heartbeat_Return(int local_addr);
}
