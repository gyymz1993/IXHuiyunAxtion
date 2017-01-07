package com.huiyun.ixhuiyunaxtion.master.dao.database;

/**
 * 输入 & 输出关联表
 * 用于记录输入设备与输出（包括输出设备、场景）的关联
 *
 */
public class RelationBean {
	private int inputType;
	private int inputAddr;
	private int inputNum;
	private int outputType;
	private int outputAddr;
	private int outputNum;
	private String sceneName;
	
	
	/**
	 * inputType值，表明该输出对应一个输入设备
	 */
	public static final int IN_TYPE_NORMAL = 0;
	
	/**
	 * outputType值，表明该输出对应一个输出设备
	 */
	public static final int OUT_TYPE_NORMAL = 0;
	
	/**
	 * outputType值，表明该输出对应一个场景
	 */
	public static final int OUT_TYPE_SCENE  = 1;
	
	public RelationBean(){
		inputType  = 0;
		inputAddr  = 0;
		inputNum   = 0;
		outputType = 0;
		outputAddr = 0;
		outputNum  = 0;
		sceneName  = "";
	}
	
	public int getInputAddr() {
		return inputAddr;
	}
	public void setInputAddr(int inputAddr) {
		this.inputAddr = inputAddr;
	}
	
	public int getInputObj() {
		return inputNum;
	}
	public void setInputObj(int inputObj) {
		this.inputNum = inputObj;
	}
	
	public int getOutputAddr() {
		return outputAddr;
	}
	public void setOutputAddr(int outputAddr) {
		this.outputAddr = outputAddr;
	}
	
	public int getOutputObj() {
		return outputNum;
	}
	public void setOutputObj(int outputObj) {
		this.outputNum = outputObj;
	}
	
	public int getInputType() {
		return inputType;
	}
	public void setInputType(int inputType) {
		this.inputType = inputType;
	}
	
	public int getOutputType() {
		return outputType;
	}
	public void setOutputType(int outputType) {
		this.outputType = outputType;
	}
	
	public String getSceneName() {
		return sceneName;
	}
	public void setSceneName(String sceneName) {
		this.sceneName = sceneName;
	}

	@Override
	public String toString() {
		return "RelationBean [inputType=" + inputType + ", inputAddr="
				+ inputAddr + ", inputObj=" + inputNum + ", outputType="
				+ outputType + ", outputAddr=" + outputAddr + ", outputObj="
				+ outputNum + ", sceneName=" + sceneName + "]";
	}
	
}
