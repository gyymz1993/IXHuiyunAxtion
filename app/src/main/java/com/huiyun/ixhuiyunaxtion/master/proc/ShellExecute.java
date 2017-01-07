package com.huiyun.ixhuiyunaxtion.master.proc;

import com.huiyun.ixhuiyunaxtion.master.utils.ShellUtils;

import java.util.ArrayList;
import java.util.List;


/**   
 * @Description: shell命令执行 
 */
public class ShellExecute {
	public static void enableRemoteAdb(){
		new Thread(){
			public void run() {
				List<String> lstCommand = new ArrayList<String>();
				lstCommand.add("su");
				lstCommand.add("setprop service.adb.tcp.port 5555");
				lstCommand.add("stop adbd");
				lstCommand.add("start adbd");
				ShellUtils.execCommand(lstCommand, true, true);
			};
		}.start();
	}
}
