package com.huiyun.ixhuiyunaxtion.master.net;

import com.huiyun.ixhuiyunaxtion.master.bean.table.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;


/**
 * 管理token
 * @author lzy_torah
 *
 */
public class TokenManager {
	/** 储存所有的token */
	static Map<String,User> tokenMap=new HashMap<String,User>();
	/**根据当前的用户名生成一个新token
	 * @param user
	 * @return
	 */
	public static String getANewToken(User user){
		if(tokenMap.containsValue(user)){//已经登陆过
			for(Entry<String,User> entry:tokenMap.entrySet()){
				boolean equals = entry.getValue().equals(user);
				if(equals){
					return entry.getKey();
				}
			}
		}
		String token = getUUid();
		tokenMap.put(token, user);
		return token;
	}
	/**判断token
	 * @param token
	 * @return 
	 */
	public static User judgetToken(String token){
		User user = tokenMap.get(token);
		if(user ==null)
			return null;
		return user;
	}
	
	/**
	 * 获取唯一标识uuid
	 */
	public static String getUUid(){
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}
}
