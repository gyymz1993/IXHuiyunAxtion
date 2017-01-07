package com.huiyun.ixhuiyunaxtion.master;

/**   
 * @Description: 储存一些全局的变量
 */
public class StaticValues {
	// 主机ID
	public static String MASTER_ID = "";
	public static String SERVER_TOKEN = "";
	// 检查版本地址
	public static String versionFile; //= "http://192.168.1.5:8080/JavaServers/index.txt";
	// 下载新APK地址
	public static String downLoadFile; //= "http://192.168.1.5:8080/JavaServers/AxtionMaster2.apk";
	
	/**
	 * 远程服务器地址
	 */
	public static final String SERVER_IP_ADDRESS = "120.24.239.90";
	/**
	 * 上传错误文件的地址
	 */
	public static String uploadHost = "http://192.168.1.5:8080/huiyun/uploadServlet";
}
