package com.huiyun.ixhuiyunaxtion.master.bean.table;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

/**
 * @Description:
 * @date 2015年2月6日 下午4:05:17
 * @version V1.0
 */
@Table(name = "webcam")
public class Webcam extends EntityBase {

	@Column(column = "did")
	private String did;

	@Column(column = "area")
	private String area;

	public String getDid() {
		return did;
	}

	public Webcam() {
		super();
	}

	public Webcam(String did, String area) {
		super();
		this.did = did;
		this.area = area;
	}

	public void setDid(String did) {
		this.did = did;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	@Override
	public String toString() {
		return "Webcam [did=" + did + ", area=" + area + "]";
	}

}
