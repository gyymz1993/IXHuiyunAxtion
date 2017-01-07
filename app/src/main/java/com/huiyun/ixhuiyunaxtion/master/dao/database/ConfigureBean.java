package com.huiyun.ixhuiyunaxtion.master.dao.database;

/**
 * configure表的bean
 *
 */
public class ConfigureBean {
	private int localAddr;
	private int panKey;
	
	public ConfigureBean(){
		// default value
		localAddr = 0;
		panKey = 1;
	}
	
	public int getLocalAddr() {
		return localAddr;
	}
	public void setLocalAddr(int localAddr) {
		this.localAddr = localAddr;
	}
	public int getPanKey() {
		return panKey;
	}
	public void setPanKey(int panKey) {
		this.panKey = panKey;
	}
}
