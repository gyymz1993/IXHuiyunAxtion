package com.huiyun.ixhuiyunaxtion.master.bean;

/**   
 * @Description: 网关配置信息
 * @author lzn
 * @date 2015-1-16 上午9:12:00 
 * @version V1.0   
 */
public class GatewayConfigure {
	
	public static final int COMM_TYPE_485 = 1;
	public static final int DETAIL_TYPE_PPS_SWITCH = 2;
	
	private int address;
	private int number;
	private int commType;
	private int detailType;
	private int execId;
	private int switchId;
	private String details;
	private String name;
	
	public int getNumber() {
		return number;
	}
	
	 /**
	 *  Function: 对应编号
	 *  @author Yangshao 
	 *  2015-1-16 上午9:27:44
	 *  @param number
	 */
	public void setNumber(int number) {
		this.number = number;
	}
	/**
	 *  Function: 通讯类型
	 *  @author lzn 
	 *  2015-1-16 上午9:26:44
	 *  @param commType
	 */
	public int getCommType() {
		return commType;
	}
	
	 /**
	 *  Function: 通讯类型
	 *  @author lzn 
	 *  2015-1-16 上午9:26:44
	 *  @param commType
	 */
	public void setCommType(int commType) {
		this.commType = commType;
	}
	/**
	 *  Function: 详细类型
	 *  @author lzn 
	 *  2015-1-16 上午9:27:01
	 *  @param detailType
	 */
	public int getDetailType() {
		return detailType;
	}
	
	 /**
	 *  Function: 详细类型
	 *  @author lzn 
	 *  2015-1-16 上午9:27:01
	 *  @param detailType
	 */
	public void setDetailType(int detailType) {
		this.detailType = detailType;
	}
	/**
	 *  Function: 执行器号
	 *  @author lzn 
	 *  2015-1-16 上午9:27:16
	 *  @param execId
	 */
	public int getExecId() {
		return execId;
	}
	
	 /**
	 *  Function: 执行器号
	 *  @author lzn 
	 *  2015-1-16 上午9:27:16
	 *  @param execId
	 */
	public void setExecId(int execId) {
		this.execId = execId;
	}
	/**
	 *  Function: 开关号
	 *  @author lzn 
	 *  2015-1-16 上午9:27:30
	 *  @param switchId
	 */
	public int getSwitchId() {
		return switchId;
	}
	
	 /**
	 *  Function: 开关号
	 *  @author lzn 
	 *  2015-1-16 上午9:27:30
	 *  @param switchId
	 */
	public void setSwitchId(int switchId) {
		this.switchId = switchId;
	}

	/**
	 *  Function: 网关地址
	 *  @author lzn 
	 *  2015-1-16 上午9:46:30
	 *  @param address
	 */
	public int getAddress() {
		return address;
	}
	 /**
	 *  Function: 网关地址
	 *  @author lzn 
	 *  2015-1-16 上午9:46:30
	 *  @param address
	 */
	public void setAddress(int address) {
		this.address = address;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + address;
		result = prime * result + commType;
		result = prime * result + detailType;
		result = prime * result + execId;
		result = prime * result + switchId;
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
		GatewayConfigure other = (GatewayConfigure) obj;
		if (address != other.address)
			return false;
		if (commType != other.commType)
			return false;
		if (detailType != other.detailType)
			return false;
		if (execId != other.execId)
			return false;
		if (switchId != other.switchId)
			return false;
		return true;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
