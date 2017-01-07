package com.huiyun.ixhuiyunaxtion.master.cmd.process;

import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;
import com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer;

import java.util.Timer;
import java.util.TimerTask;


/**   
 * @Description: 初始状态为关、开启了之后会在一定时间内关闭的开关
 * 注1：在已经开启的状态下再次被启动时，关闭的延时会被重置
 * @date 2015-4-14 上午10:27:43 
 * @version V1.0   
 */
public class AgingSwitch {
	private boolean open; // 开关值
	private int time; // 关闭延时，按秒数计
	Timer timer; // 关闭延时计时器
	
	
	 /**
	 * @param second 关闭延时，按秒数计
	 */
	public AgingSwitch(int second){
		open = false; // 初始状态为关
		time = second;
	}
	
	
	 /**
	 *  Function:开启开关
	 */
	public void open(){
		MainActivity.showString(
				"AgingSwitch开启", MultiTextBuffer.TYPE_SERIALPORT);
		open = true;
		if(timer != null){
			timer.cancel();
		}
		timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				MainActivity.showString(
						"AgingSwitch关闭", MultiTextBuffer.TYPE_SERIALPORT);
				open = false;
			}
		}, time * 1000l);
	}
	
	
	 /**
	 *  Function:查看开关当前状态
	 *  @return
	 */
	public boolean isOpen(){
		return open;
	}
}
