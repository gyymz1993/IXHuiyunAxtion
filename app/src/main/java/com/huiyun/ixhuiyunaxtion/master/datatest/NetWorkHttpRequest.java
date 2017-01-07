package com.huiyun.ixhuiyunaxtion.master.datatest;

import com.huiyun.ixhuiyunaxtion.master.inner.OnResultListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**   
* @Description: 网络请求
* @date 2015年5月26日 下午5:15:19 
* @version V1.0   
*/
public class NetWorkHttpRequest {

	private  static NetWorkHttpRequest httpRequest;
	//当前网络的网关ip
	public static String currentNetWorIP;
	private NetWorkHttpRequest(){
		
	}
	public static NetWorkHttpRequest getHttpRequest(){
			if(httpRequest==null){
				synchronized (NetWorkHttpRequest.class) {
					if(httpRequest==null){
						httpRequest=new NetWorkHttpRequest(); 
					}
				}
			}
			return httpRequest;
	}
	static OnResultListener<Boolean> listener;
	public void setListener(OnResultListener<Boolean> listener) {
		NetWorkHttpRequest.listener = listener;
	}
	
	
	public  boolean isNetWorkPing() {
		boolean netSataus = false;
		if(currentNetWorIP!=null){
			netSataus = httpRequest.ping(currentNetWorIP);
		}else{
			System.out.println("请先获取主机ip");
		}
		if (listener != null) {
			listener.onResult(true, netSataus);
		}
		return netSataus;
	}

	private final boolean ping(String netWorkIp) {
		try {
			Process p = Runtime.getRuntime().exec(
					"ping -c 3 -w 100 " + netWorkIp);// ping3次
			// 读取ping的内容，可不加。
			InputStream input = p.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(input));
			StringBuffer stringBuffer = new StringBuffer();
			String content = "";
			while ((content = in.readLine()) != null) {
				stringBuffer.append(content);
			}
			// PING的状态
			int status = p.waitFor();
			if (status == 0) {
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return false;
	}

	public String getGateWay(int paramInt) { 
		return (paramInt & 0xFF) + "." + (0xFF & paramInt >> 8) + "." + (0xFF & paramInt >> 16) + "."
				+ (0xFF & paramInt >> 24);
	}
	
	
	
	 /**
	 *  Function:判断网络是否可用
	 * 
	 *  @author YangShao 2015年5月26日 下午4:41:53
	 *  @return
	 */
	public boolean isNetWorkAvailable(){
		boolean netSataus = false;  
		NetWorkHttpRequest httpRequest = new NetWorkHttpRequest();
		netSataus = httpRequest.getURLResponse("http://www.baidu.com") !=""?true:false;
		if(listener!=null){
			listener.onResult(true, netSataus);
		}
		return netSataus; 
	}

	
	/**
	 * 获取指定URL的响应字符串
	 * @param urlString
	 * @return
	 */
	private String getURLResponse(String urlString){
		HttpURLConnection conn = null; //连接对象
		InputStream is = null;
		String resultData = "";
		try {
			URL url = new URL(urlString); //URL对象
			conn = (HttpURLConnection)url.openConnection(); //使用URL打开一个链接
			conn.setDoInput(true); //允许输入流，即允许下载
			conn.setDoOutput(true); //允许输出流，即允许上传
			conn.setUseCaches(false); //不使用缓冲
			conn.setRequestMethod("GET"); //使用get请求
			is = conn.getInputStream();   //获取输入流，此时才真正建立链接
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader bufferReader = new BufferedReader(isr);
			String inputLine  = "";
			while((inputLine = bufferReader.readLine()) != null){
				resultData += inputLine + "\n";
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(conn != null){
				conn.disconnect();
			}
		}
		return resultData;
	}

}
