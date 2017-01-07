package com.huiyun.ixhuiyunaxtion.master.datatest;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.widget.TextView;
public class ReadMemory {
	public  static String CPUMessage="";
    public TextView tv = null;
    static  long total = 0;
    static long idle = 0;
    static long process = 0;
    public static double usage_total = 0;
    public static double usage_process = 0;
    public void CPULoad( )
    {
        readUsage( );
    }
   public static void readUsage( )
    {
        try
        {
        	// 从文件中读取系统信息
            BufferedReader reader = new BufferedReader(
            		new InputStreamReader(new FileInputStream("/proc/stat")), 1000);
            String load = reader.readLine();
            reader.close();     
            String[] toks = load.split(" ");
            
            BufferedReader reader_process = new BufferedReader(
            		new InputStreamReader( new FileInputStream(
            		"/proc/" + android.os.Process.myPid() + "/stat")), 1000);
            String load_process = reader_process.readLine();
            reader_process.close();     
            String[] toks_process = load_process.split(" ");
            
            // 获取总的CPU活动时间
            long currTotal = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4]);
            long currIdle = Long.parseLong(toks[5]);
            // 获取进程的CPU活动时间
            long currProcess = Long.parseLong(toks_process[13]) + Long.parseLong(toks_process[14]) +
            		Long.parseLong(toks_process[15]) + Long.parseLong(toks_process[16]);
            
            usage_total =(currTotal - total) * 100.0f / (currTotal - total + currIdle - idle);
            usage_process = (currProcess - process) * 100.0f / (currTotal - total + currIdle - idle);
            total = currTotal;
            idle = currIdle;
            process = currProcess;
        }
        catch( IOException ex )
        {
            ex.printStackTrace();           
        }
        
    }
   
   // 获取CPU温度
//   public static String getTempeter() {
//           String result = "";
//           ProcessBuilder cmd;
//           try {
//                   String[] args = {  "/system/bin/cat",
//                   		"/sys/class/thermal/thermal_zone0/temp"};
//                   cmd = new ProcessBuilder(args);
//                   Process process = cmd.start();
//                   InputStream in = process.getInputStream();
//                   byte[] re = new byte[24];
//                   while (in.read(re) != -1) {
//                           result = result + new String(re);
//                   }
//                   in.close();
//           } catch (IOException ex) {
//                   ex.printStackTrace();
//                   result = "N/A";
//           }
//           return result.trim();
//   }

}





