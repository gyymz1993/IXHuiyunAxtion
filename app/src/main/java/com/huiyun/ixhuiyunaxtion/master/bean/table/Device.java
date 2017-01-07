package com.huiyun.ixhuiyunaxtion.master.bean.table;

import com.huiyun.ixhuiyunaxtion.master.utils.BytesStringUtils;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Transient;

@Table(name = "predevice", execAfterTableCreated = "CREATE UNIQUE INDEX index_chipId ON predevice(chipId,number)")
public class Device extends EntityBase {
	public static final int TYPE_IN             = 1;
	public static final int TYPE_OUT            = 2;
	public static final int TYPE_MASTER         = 3;
	public static final int TYPE_PHONE          = 4;
	public static final int TYPE_RAYSENDER      = 5;
	public static final int TYPE_BODY_INDUCTION = 6;
	public static final int TYPE_OUT_WITH_IN    = 7;
	public static final int TYPE_GATEWAY        = 12;
	public static final int TYPE_WINDOW         = 14;
	public static final int TYPE_COLOR_LAMP     = 22;
	public static final int TYPE_RED_RAY        = 24;
	
	@Column(column = "area")
	private String area;// 区域
	@Column(column = "name")
	private String name;// 名字
	@Column(column = "number")
	private int number = 0;// 编号
	@Column(column = "details")
	private String details;// 设备信息，返回的字符串
	@Column(column = "chipId")
	private String chipId;// 芯片id,储存show模式。手机芯片id固定为 1000
	@Column(column = "address")
	private int address;// 设备地址
	@Column(column = "type")
	private int type;// 输入设备1，输出设备个位数为2,主机3,手机4，网关输出12. 红外设备5
	@Column(column = "phoneCode")
	private int phoneCode;// 对应的手机按键码，只有输出设备有，输入设备为0

	// 不储存此字段
	@Transient
	private int count; // 表示一个chipId的number数
	@Transient
	private boolean checked;// 在组合页面使用，储存是否被选中的状态

	public int getCount() {
		return count;
	}

	@Transient
	private int state;

	/**
	 * Function: 获取device的状态(1表示开，0表示关)
	 * 
	 * @author lzn 2015-1-15 下午1:12:07
	 * @return
	 */
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	/**
	 * Function: 确认当前device的状态是否明确
	 * 
	 * @author lzn 2015-1-15 下午1:12:50
	 * @return
	 */
	public boolean isReable() {
		return reable;
	}

	public void setReable(boolean reable) {
		this.reable = reable;
	}

	@Transient
	private boolean reable;

	/**
	 * 对外 BYTE Function:
	 * 
	 * @author Yangshao 2015年1月13日 下午3:37:17
	 * @return
	 */
	public byte[] getChipIds() {
		return BytesStringUtils.toArrayByteShow(chipId);
	}

	public void setChipIds(byte[] chipIds) {
		this.chipId = BytesStringUtils.toStringShow(chipIds);
	}

	// ------------------------------------

	public void setCount(int count) {
		this.count = count;
	}

	public int getPhoneCode() {
		return phoneCode;
	}

	public void setPhoneCode(int phoneCode) {
		this.phoneCode = phoneCode;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public Device() {
		super();
	}

	public Device(String details, String chipId, int address) {
		super();
		this.details = details;
		this.chipId = chipId;
		this.address = address;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getChipId() {
		return chipId;
	}

	public void setChipId(String chipId) {
		this.chipId = chipId;
	}

	public int getAddress() {
		return address;
	}

	public void setAddress(int address) {
		this.address = address;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((chipId == null) ? 0 : chipId.hashCode());
		result = prime * result + number;
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
		Device other = (Device) obj;
		if (chipId == null) {
			if (other.chipId != null)
				return false;
		} else if (!chipId.equals(other.chipId))
			return false;
		if (number != other.number)
			return false;
		return true;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Device [area=" + area + ", name=" + name + ", number=" + number
				+ ", details=" + details + ", chipId=" + chipId + ", address="
				+ address + ", type=" + type + ", phoneCode=" + phoneCode + "]";
	}

}
