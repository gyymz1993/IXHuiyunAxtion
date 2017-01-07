package com.huiyun.ixhuiyunaxtion.master.cmd.command;


import com.huiyun.ixhuiyunaxtion.master.utils.BytesStringUtils;

/**
 * 命令的实现
 */
public class ICMD implements CMD {

	@Override
	public byte[] CMD_Search_Device(int local_addr, int type) {
		byte[] buffer = new byte[4];

		buffer[Data.P_TARGET_ADDR] = 0;
		buffer[Data.P_LOCAL_ADDR] = (byte) local_addr;
		buffer[Data.P_FUNC_CODE] = (byte) Data.FUNC_CODE_SEARCH;
		buffer[3] = (byte) type;

		return buffer;
	}

	@Override
	public byte[] CMD_Ask_Search(int target_addr, int local_addr,
			byte[] device_info, byte[] CPUID, int key) {
		byte[] buffer = new byte[4 + Data.LENGTH_DEV_INFO + Data.LENGTH_CPUID];

		buffer[Data.P_TARGET_ADDR] = (byte) target_addr;
		buffer[Data.P_LOCAL_ADDR] = (byte) local_addr;
		buffer[Data.P_FUNC_CODE] = (byte) Data.FUNC_CODE_SEARCH_RETURN;
		for (int i = 0; i < Data.LENGTH_DEV_INFO; i++)
			buffer[3 + i] = device_info[i];
		for (int i = 0; i < Data.LENGTH_CPUID; i++)
			buffer[3 + Data.LENGTH_DEV_INFO + i] = CPUID[i];
		buffer[3 + Data.LENGTH_DEV_INFO + Data.LENGTH_CPUID] = (byte) key;

		return buffer;
	}

	@Override
	public byte[] CMD_Set_Target_Addr(int local_addr, int new_addr,
			byte[] CPUID, int key) {
		byte[] buffer = new byte[5 + Data.LENGTH_CPUID];

		buffer[Data.P_TARGET_ADDR] = 0;
		buffer[Data.P_LOCAL_ADDR] = (byte) local_addr;
		buffer[Data.P_FUNC_CODE] = (byte) Data.FUNC_CODE_SET_ADDR;
		buffer[3] = (byte) new_addr;
		for (int i = 0; i < Data.LENGTH_CPUID; i++)
			buffer[4 + i] = CPUID[i];
		buffer[4 + Data.LENGTH_CPUID] = (byte) key;

		return buffer;
	}

	@Override
	public byte[] CMD_Ask_Set_Addr(int target_addr, int local_addr) {
		byte[] buffer = new byte[3];

		buffer[Data.P_TARGET_ADDR] = (byte) target_addr;
		buffer[Data.P_LOCAL_ADDR] = (byte) local_addr;
		buffer[Data.P_FUNC_CODE] = (byte) Data.FUNC_CODE_SET_ADDR_RETURN;

		return buffer;
	}

	@Override
	public byte[] CMD_Device_Blink(int local_addr, byte[] CPUID) {
		byte[] buffer = new byte[3 + Data.LENGTH_CPUID];

		buffer[Data.P_TARGET_ADDR] = 0;
		buffer[Data.P_LOCAL_ADDR] = (byte) local_addr;
		buffer[Data.P_FUNC_CODE] = (byte) Data.FUNC_CODE_DEV_BLINK;
		for (int i = 0; i < Data.LENGTH_CPUID; i++)
			buffer[3 + i] = CPUID[i];

		return buffer;
	}

	@Override
	public byte[] CMD_Ask_Device_Blink(int target_addr, int local_addr,
			byte[] CPUID) {
		byte[] buffer = new byte[3 + Data.LENGTH_CPUID];

		buffer[Data.P_TARGET_ADDR] = (byte) target_addr;
		buffer[Data.P_LOCAL_ADDR] = (byte) local_addr;
		buffer[Data.P_FUNC_CODE] = (byte) Data.FUNC_CODE_DEV_BLINK_RETURN;
		for (int i = 0; i < Data.LENGTH_CPUID; i++)
			buffer[3 + i] = CPUID[i];

		return buffer;
	}

	@Override
	public byte[] CMD_Set_Hand_Mode(int local_addr) {
		byte[] buffer = new byte[3];

		buffer[Data.P_TARGET_ADDR] = 0;
		buffer[Data.P_LOCAL_ADDR] = (byte) local_addr;
		buffer[Data.P_FUNC_CODE] = (byte) Data.FUNC_CODE_DEV_HAND;

		return buffer;
	}

	@Override
	public byte[] CMD_Hand_Mode_Device_Data(int target_addr, int local_addr,
			byte[] CPUID) {
		byte[] buffer = new byte[3 + Data.LENGTH_CPUID];

		buffer[Data.P_TARGET_ADDR] = (byte) target_addr;
		buffer[Data.P_LOCAL_ADDR] = (byte) local_addr;
		buffer[Data.P_FUNC_CODE] = (byte) Data.FUNC_CODE_DEV_HAND_RETURN;
		for (int i = 0; i < Data.LENGTH_CPUID; i++)
			buffer[3 + i] = CPUID[i];

		return buffer;
	}

	@Override
	public byte[] CMD_CRTL_IO(int target_addr, int local_addr, int key,
			int object, int data_size, byte[] data) {
		byte[] buffer = new byte[Data.P_DATA + data_size];

		buffer[Data.P_TARGET_ADDR] = (byte) target_addr;
		buffer[Data.P_LOCAL_ADDR] = (byte) local_addr;
		buffer[Data.P_FUNC_CODE] = (byte) Data.FUNC_CODE_IO_W;
		buffer[Data.P_PAN_KEY] = (byte) key;
		buffer[Data.P_OBJECT_NUM] = (byte) object;
		for (int i = 0; i < data_size; i++)
			buffer[Data.P_DATA + i] = data[i];

		return buffer;
	}

	@Override
	public byte[] CMD_Ask_CRTL_IO(int target_addr, int local_addr, int key,
			int object, int data_size, byte[] data) {
		byte[] buffer = new byte[Data.P_DATA + data_size];

		buffer[Data.P_TARGET_ADDR] = (byte) target_addr;
		buffer[Data.P_LOCAL_ADDR] = (byte) local_addr;
		buffer[Data.P_FUNC_CODE] = (byte) Data.FUNC_CODE_IO_W_RETURN;
		buffer[Data.P_PAN_KEY] = (byte) key;
		buffer[Data.P_OBJECT_NUM] = (byte) object;
		for (int i = 0; i < data_size; i++)
			buffer[Data.P_DATA + i] = data[i];

		return buffer;
	}

	@Override
	public byte[] CMD_Read_IO(int target_addr, int local_addr, int key,
			int object, int data_size, byte[] data) {
		byte[] buffer = new byte[Data.P_DATA + data_size];

		buffer[Data.P_TARGET_ADDR] = (byte) target_addr;
		buffer[Data.P_LOCAL_ADDR] = (byte) local_addr;
		buffer[Data.P_FUNC_CODE] = (byte) Data.FUNC_CODE_IO_R;
		buffer[Data.P_PAN_KEY] = (byte) key;
		buffer[Data.P_OBJECT_NUM] = (byte) object;
		for (int i = 0; i < data_size; i++)
			buffer[Data.P_DATA + i] = data[i];

		return buffer;
	}

	@Override
	public byte[] CMD_Ask_Read_IO(int target_addr, int local_addr, int key,
			int object, int data_size, byte[] data) {
		byte[] buffer = new byte[Data.P_DATA + data_size];

		buffer[Data.P_TARGET_ADDR] = (byte) target_addr;
		buffer[Data.P_LOCAL_ADDR] = (byte) local_addr;
		buffer[Data.P_FUNC_CODE] = (byte) Data.FUNC_CODE_IO_R_RETURN;
		buffer[Data.P_PAN_KEY] = (byte) key;
		buffer[Data.P_OBJECT_NUM] = (byte) object;
		for (int i = 0; i < data_size; i++)
			buffer[Data.P_DATA + i] = data[i];

		return buffer;
	}

	@Override
	public byte[] CMD_IO_Notice(int target_addr, int local_addr, int key,
			int object, int data_size, byte[] data) {
		byte[] buffer = new byte[Data.P_DATA + data_size];

		buffer[Data.P_TARGET_ADDR] = (byte) target_addr;
		buffer[Data.P_LOCAL_ADDR] = (byte) local_addr;
		buffer[Data.P_FUNC_CODE] = (byte) Data.FUNC_CODE_IO_NOTICE;
		buffer[Data.P_PAN_KEY] = (byte) key;
		buffer[Data.P_OBJECT_NUM] = (byte) object;
		for (int i = 0; i < data_size; i++)
			buffer[Data.P_DATA + i] = data[i];

		return buffer;
	}

	@Override
	public byte[] CMD_Ask_IO_Notice(int target_addr, int local_addr, int key,
			int object, int data_size, byte[] data) {
		byte[] buffer = new byte[Data.P_DATA + data_size];

		buffer[Data.P_TARGET_ADDR] = (byte) target_addr;
		buffer[Data.P_LOCAL_ADDR] = (byte) local_addr;
		buffer[Data.P_FUNC_CODE] = (byte) Data.FUNC_CODE_IO_NOTICE_RETURN;
		buffer[Data.P_PAN_KEY] = (byte) key;
		buffer[Data.P_OBJECT_NUM] = (byte) object;
		for (int i = 0; i < data_size; i++)
			buffer[Data.P_DATA + i] = data[i];

		return buffer;
	}

	@Override
	public byte[] CMD_Set_Func(int target_addr, int local_addr, int key,
			int object, int data_size, byte[] data) {
		byte[] buffer = new byte[Data.P_DATA + data_size];

		buffer[Data.P_TARGET_ADDR] = (byte) target_addr;
		buffer[Data.P_LOCAL_ADDR] = (byte) local_addr;
		buffer[Data.P_FUNC_CODE] = (byte) Data.FUNC_CODE_SET_W;
		buffer[Data.P_PAN_KEY] = (byte) key;
		buffer[Data.P_OBJECT_NUM] = (byte) object;
		for (int i = 0; i < data_size; i++)
			buffer[Data.P_DATA + i] = data[i];

		return buffer;
	}

	@Override
	public byte[] GetData_CMD(byte[] cmd) {
		if (cmd.length <= Data.P_DATA)
			return null;
		int len = cmd.length - Data.P_DATA;
		byte[] data = new byte[len];
		for (int i = 0; i < len; i++)
			data[i] = cmd[Data.P_DATA + i];
		return data;
	}

	@Override
	public byte[] CMD_Ask_Set_Func(int target_addr, int local_addr, int key,
			int object, int data_size, byte[] data) {
		byte[] buffer = new byte[Data.P_DATA + data_size];

		buffer[Data.P_TARGET_ADDR] = (byte) target_addr;
		buffer[Data.P_LOCAL_ADDR] = (byte) local_addr;
		buffer[Data.P_FUNC_CODE] = (byte) Data.FUNC_CODE_SET_W_RETURN;
		buffer[Data.P_PAN_KEY] = (byte) key;
		buffer[Data.P_OBJECT_NUM] = (byte) object;
		for (int i = 0; i < data_size; i++)
			buffer[Data.P_DATA + i] = data[i];

		return buffer;
	}

	@Override
	public byte[] CMD_Read_Func(int target_addr, int local_addr, int key,
			int object, int data_size, byte[] data) {
		byte[] buffer = new byte[Data.P_DATA + data_size];

		buffer[Data.P_TARGET_ADDR] = (byte) target_addr;
		buffer[Data.P_LOCAL_ADDR] = (byte) local_addr;
		buffer[Data.P_FUNC_CODE] = (byte) Data.FUNC_CODE_SET_R;
		buffer[Data.P_PAN_KEY] = (byte) key;
		buffer[Data.P_OBJECT_NUM] = (byte) object;
		for (int i = 0; i < data_size; i++)
			buffer[Data.P_DATA + i] = data[i];

		return buffer;
	}

	@Override
	public byte[] CMD_Ask_Read_Func(int target_addr, int local_addr, int key,
			int object, int data_size, byte[] data) {
		byte[] buffer = new byte[Data.P_DATA + data_size];

		buffer[Data.P_TARGET_ADDR] = (byte) target_addr;
		buffer[Data.P_LOCAL_ADDR] = (byte) local_addr;
		buffer[Data.P_FUNC_CODE] = (byte) Data.FUNC_CODE_SET_R_RETURN;
		buffer[Data.P_PAN_KEY] = (byte) key;
		buffer[Data.P_OBJECT_NUM] = (byte) object;
		for (int i = 0; i < data_size; i++)
			buffer[Data.P_DATA + i] = data[i];

		return buffer;
	}

	@Override
	public byte[] CMD_LOG_IN(int local_addr, byte[] CPUID) {
		byte[] buffer = new byte[3 + Data.LENGTH_CPUID];

		buffer[0] = 0;
		buffer[1] = (byte) local_addr;
		buffer[2] = 0;
		for (int i = 0; i < Data.LENGTH_CPUID; i++) {
			buffer[3 + i] = CPUID[i];
		}

		return buffer;
	}

	@Override
	public byte[] CMD_Learn_Passway(int my_addr, int key, int switchVal) {
		byte[] buffer = new byte[6];

		buffer[Data.P_TARGET_ADDR] = (byte) Data.ADDR_SELF;
		buffer[Data.P_LOCAL_ADDR] = (byte) my_addr;
		buffer[Data.P_FUNC_CODE] = (byte) Data.FUNC_CODE_LEARN_PASSWAY;
		buffer[Data.P_PAN_KEY] = (byte) key;
		buffer[Data.P_OBJECT_NUM] = (byte) 0;
		buffer[5] = (byte) switchVal;

		return buffer;
	}

	@Override
	public byte[] CMD_Ask_Ray_Learn(int my_addr) {
		byte[] buffer = new byte[3];

		buffer[Data.P_TARGET_ADDR] = (byte) my_addr;
		buffer[Data.P_LOCAL_ADDR] = (byte) Data.ADDR_SELF;
		buffer[Data.P_FUNC_CODE] = (byte) Data.FUNC_CODE_RAY_LEARN_RETURN;

		return buffer;
	}

	@Override
	public byte[] CMD_Ray_Control(int target_addr, int local_addr, int key,
			int data_size, byte[] data) {
		byte[] buffer = new byte[Data.P_DATA + data_size];

		buffer[Data.P_TARGET_ADDR] = (byte) target_addr;
		buffer[Data.P_LOCAL_ADDR] = (byte) local_addr;
		buffer[Data.P_FUNC_CODE] = (byte) Data.FUNC_CODE_RAY_CONTROL;
		buffer[Data.P_PAN_KEY] = (byte) key;
		buffer[Data.P_OBJECT_NUM] = (byte) 0;
		for (int i = 0; i < data_size; i++)
			buffer[Data.P_DATA + i] = data[i];

		return buffer;
	}

	@Override
	public byte[] GetData_RayCode(byte[] cmd) {
		if (cmd.length < Data.LENGTH_REDRAY + 5)
			return null;
		if (BytesStringUtils.toUnsignedInt(cmd[Data.P_FUNC_CODE]) != Data.FUNC_CODE_RAY_CONTROL
				&& BytesStringUtils.toUnsignedInt(cmd[Data.P_FUNC_CODE]) != Data.FUNC_CODE_RAY_LEARN) {
			return null;
		}

		byte[] raycode = BytesStringUtils.subBytes(cmd, 5, Data.LENGTH_REDRAY);

		return raycode;
	}

	@Override
	public byte[] GetDevInfo_CMD(byte[] cmd) {
		if (cmd.length < 22)
			return null;
		int funcCode = BytesStringUtils.toUnsignedInt(cmd[Data.P_FUNC_CODE]);
		if (funcCode != Data.FUNC_CODE_SEARCH_RETURN
			&& funcCode != Data.FUNC_CODE_DEV_HAND_RETURN) {
			return null;
		}

		byte[] devInfo = BytesStringUtils.subBytes(cmd, 3, 6);

		return devInfo;
	}

	@Override
	public byte[] GetChipId_CMD(byte[] cmd) {
		if (cmd.length < 22)
			return null;
		int funcCode = BytesStringUtils.toUnsignedInt(cmd[Data.P_FUNC_CODE]);
		if ((funcCode == Data.FUNC_CODE_SEARCH_RETURN)
				|| (funcCode == Data.FUNC_CODE_DEV_HAND_RETURN)) {
			byte[] chipId = BytesStringUtils.subBytes(cmd, 9, 12);
			return chipId;
		}
		return null;
	}

	@Override
	public byte[] CMD_WIFI_State(int local_addr, boolean connected) {
		byte[] buffer = new byte[4];

		buffer[0] = (byte) Data.ADDR_SELF;
		buffer[1] = (byte) local_addr;
		buffer[2] = (byte) Data.FUNC_CODE_WIFI_STATE;
		if (connected) {
			buffer[3] = 1;
		} else {
			buffer[3] = 0;
		}

		return buffer;
	}

	@Override
	public byte[] CMD_RedCode_Learn(int target_addr, int local_addr, int key,
			int number) {
		byte[] cmd = new byte[5];
		cmd[0] = (byte) target_addr;
		cmd[1] = (byte) local_addr;
		cmd[2] = Data.FUNC_CODE_REDCODE_LEARN;
		cmd[3] = (byte) key;
		cmd[4] = (byte) number;
		
		return cmd;
	}

	@Override
	public byte[] CMD_RedCode_Control(int target_addr, int local_addr, int key,
			int number) {
		byte[] cmd = new byte[5];
		cmd[0] = (byte) target_addr;
		cmd[1] = (byte) local_addr;
		cmd[2] = Data.FUNC_CODE_REDCODE_CONTROL;
		cmd[3] = (byte) key;
		cmd[4] = (byte) number;
		
		return cmd;
	}

	
	
	@Override
	public byte[] CMD_Heartbeat_Return(int local_addr) {
		byte[] cmd = new byte[3];
		cmd[0] = (byte) 254;
		cmd[1] = (byte) local_addr;
		cmd[2] = (byte) Data.FUNC_CODE_HEART_BEAT_RETURN;
		
		return cmd;
	}
}
