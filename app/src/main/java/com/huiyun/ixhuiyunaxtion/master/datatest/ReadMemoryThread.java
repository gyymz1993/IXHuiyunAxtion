package com.huiyun.ixhuiyunaxtion.master.datatest;
public class ReadMemoryThread extends Thread {
	  ReadMemoryHandlers memory=new ReadMemoryHandlers();
        public void run() {
                while (true) {
                       memory.sendMessage();
                    }
                }
        }