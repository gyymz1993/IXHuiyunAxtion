package com.huiyun.ixhuiyunaxtion.master.datatest;


import android.os.Handler;
import android.os.Message;

import com.huiyun.ixhuiyunaxtion.master.BaseApplication;
import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;
import com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer;

public class ReadMemoryHandlers extends Handler  {
	public static final int REFRESH = 0x000001;
	       @Override
	       public void handleMessage(Message msg) {
	       	if (msg.what == REFRESH) {
	       		freshUsage();
	       		ReadMemory.CPUMessage="mTotle: " + MainActivity.context.getTotalMemory() + ", " + "midle: "
	      				 + MainActivity.context.getAvailMemory()+","+"cusage:"+ Math.round(this.getUsage())+"%,"
	      				 +"process usage:"+ Math.round(this.getProcessUsage())+"%,";
	       				//+"CPU Temperature"+ReadMemory.getTempeter()+"C";
	       		MainActivity.showString(ReadMemory.CPUMessage, MultiTextBuffer.TYPE_OTHER);
	       		BaseApplication.printCPUDataLog();
	       	}
	      }
	
	       public void freshUsage(){
	    	   ReadMemory.readUsage();
	       }
	       
	       public double getUsage()
	       {
	           return ReadMemory.usage_total;
	       }	
	       
	       public double getProcessUsage(){
	    	   return ReadMemory.usage_process;
	       }
	       
	       public void sendMessage(){
	    		 Message msg = new Message();
	    	     msg.what = REFRESH;
	    	     msg.obj = this;
	    	     this.sendMessage(msg);
	    	     try {   
	    	         Thread.sleep(60*1000);    
	    	    } catch (InterruptedException e) {   
	    	         Thread.currentThread().interrupt();   
	    	    }   
	    	}
	    		
}

