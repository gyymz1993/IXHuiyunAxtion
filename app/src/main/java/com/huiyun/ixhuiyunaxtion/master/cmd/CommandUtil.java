package com.huiyun.ixhuiyunaxtion.master.cmd;


import com.huiyun.ixhuiyunaxtion.master.cmd.command.Data;
import com.huiyun.ixhuiyunaxtion.master.dao.database.ConfigureBean;
import com.huiyun.ixhuiyunaxtion.master.dao.database.ConfigureDao;
import com.huiyun.ixhuiyunaxtion.master.utils.UIUtils;

/**
 * 跟命令相关的配置信息
 *
 */
public class CommandUtil {
	
	/**
	 * 设备信息
	 */
	public static byte[] deviceInfo = new byte[6];
	
	/**
	 * CPUID
	 */
	public static byte[] myCPUID = new byte[12];
	public static boolean checkCPUID(byte[] b, int start, int len){
		boolean result = true;
		for(int i=0; i<len; i++){
			if(b[start+i] != myCPUID[i])
				result = false;
		}
		return result;
	}
	
	 /**
	 *  Function:获取主机地址
	 *  @author lzn 
	 *  2015-4-21 下午2:13:23
	 *  @return
	 */
	public static int getLocalAddr(){
		ConfigureDao dao = new ConfigureDao(UIUtils.getContext());
		ConfigureBean bean = dao.getConfigureInfo();
		dao.close();
		return bean.getLocalAddr();
	}
	
	
	 /**
	 *  Function: 设置主机地址
	 *  @author lzn 
	 *  2015-4-21 下午2:13:34
	 *  @param addr
	 */
	public static void setLocalAddr(int addr){
		ConfigureDao dao = new ConfigureDao(UIUtils.getContext());
		dao.updateConfigureVal(ConfigureDao.ROW_LOCAL_ADDR, 
				ConfigureDao.STR_NULL, addr);
		dao.close();
	}
	
	 /**
	 *  Function: 读取PAN_KEY值
	 *  @author lzn
	 *  2015-4-21 下午2:12:13
	 *  @return
	 */
	public static int getPanKey(){
		ConfigureDao dao = new ConfigureDao(UIUtils.getContext());
		ConfigureBean bean = dao.getConfigureInfo();
		dao.close();
		return bean.getPanKey();
	}
	
	 /**
	 *  Function: 设置PAN_KEY值
	 *  @author lzn 
	 *  2015-4-21 下午2:12:38
	 *  @param key
	 */
	public static void setPanKey(int key){
		ConfigureDao dao = new ConfigureDao(UIUtils.getContext());
		dao.updateConfigureVal(ConfigureDao.ROW_PAN_KEY, 
				ConfigureDao.STR_NULL, key);
		dao.close();
	}

	
	 /**
	 *  Function:对比PAN_KEY值是否相同
	 *  @param key
	 *  @return
	 */
	public static boolean checkPanKey(int key){
		if(key == getPanKey()){
			return true;
		} else {
			return false;
		}
	}
	
	 /**
	 *  Function: 将目标地址设为主机自己
	 *  @author lzn 
	 *  2015-3-19 下午3:06:27
	 *  @param cmd
	 */
	public static void setLocalAddrToMyself(byte[] cmd){
		cmd[Data.P_LOCAL_ADDR] = (byte) getLocalAddr();
	}
}
