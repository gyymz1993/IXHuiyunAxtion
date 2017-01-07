package com.huiyun.ixhuiyunaxtion.master.bean.table;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

@Table(name = "area")
public class Area extends EntityBase {
	
	@Column(column = "areaName")
	private String areaName;// 区域
	
	public Area() {
	}
	 /**
	 * @param areaName
	 */
	public Area(String areaName) {
		super();
		this.areaName = areaName;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	
	 /* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Area [areaName=" + areaName + "]";
	}
	
	
	
}
