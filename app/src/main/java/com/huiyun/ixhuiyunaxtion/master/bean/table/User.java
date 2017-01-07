package com.huiyun.ixhuiyunaxtion.master.bean.table;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

/**   
 * @Title: User.java 
 * @Package com.huiyun.ixconfig.bean 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author Yangshao  
 * @date 2015年1月6日 下午5:17:47 
 * @version V1.0   
 */
@Table(name="user", execAfterTableCreated = "CREATE UNIQUE INDEX index_Name ON user(name)")
public class User extends EntityBase{
	@Column(column="name")
	private String name;
	@Column(column="password")
	private String password;
	@Column(column="type")
	private int type;   //1管理员  2普通用户
	
	public User() {
		super();
	}
	
	 /**
	 * @param name
	 * @param password
	 * @param type
	 */
	public User(String name, String password, int type) {
		super();
		this.name = name;
		this.password = password;
		this.type = type;
	}


	/**
	 * @return 获得 type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type 设置 type
	 */
	public void setType(int type) {
		this.type = type;
	}


	/**
	 * @return 获得 name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name 设置 name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return 获得 password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password 设置 password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	
	
	
	 /* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "User [name=" + name + ", password=" + password + ", type="
				+ type + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	
}
