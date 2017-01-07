package com.huiyun.ixhuiyunaxtion.master.bean.table;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

/**
 * @Description: 红外码 r_name和btn_code合并非重
 * @date 2015年4月22日 下午2:12:59
 * @version V1.0
 */
@Table(name = "redray_name", execAfterTableCreated = "CREATE UNIQUE INDEX index_btn_code ON redray_name(btn_code,r_name)")
public class RedRayFunction extends EntityBase {
	/**
	 * 页面上按键的代号
	 */
	@Column(column = "btn_code")
	private int btn_code;

	/**
	 * 名称 空调等
	 */
	@Column(column = "r_name")
	private String r_name;


	/**
	 * 详情作用
	 */
	@Column(column = "d_function")
	private int d_function;


	public int getBtn_code() {
		return btn_code;
	}


	public void setBtn_code(int btn_code) {
		this.btn_code = btn_code;
	}


	public String getR_name() {
		return r_name;
	}


	public void setR_name(String r_name) {
		this.r_name = r_name;
	}


	public int getD_function() {
		return d_function;
	}


	public void setD_function(int d_function) {
		this.d_function = d_function;
	}


	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + btn_code;
		result = prime * result + ((r_name == null) ? 0 : r_name.hashCode());
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
		RedRayFunction other = (RedRayFunction) obj;
		if (btn_code != other.btn_code)
			return false;
		if (r_name == null) {
			if (other.r_name != null)
				return false;
		} else if (!r_name.equals(other.r_name))
			return false;
		return true;
	}
	
	

}
