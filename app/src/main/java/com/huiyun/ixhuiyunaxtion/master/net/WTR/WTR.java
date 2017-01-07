package com.huiyun.ixhuiyunaxtion.master.net.WTR;

/**   
 * @Description: 射频中转器
 * @date 2015-3-24 下午2:02:22 
 * @version V1.0   
 */
public interface WTR {
	public final int P_FUNC_CODE = 0;
	
	public final int FUNC_CODE_DEVINFO    = 1;
	public final int FUNC_CODE_DEVINFO_RE = 2;
	public final int FUNC_CODE_BLINK      = 3;
	public final int FUNC_CODE_BLINK_RE   = 4;
	public final int FUNC_CODE_WRITE      = 7;
	public final int FUNC_CODE_WRITE_RE   = 8;
	public final int FUNC_CODE_READ       = 9;
	public final int FUNC_CODE_READ_RE    = 10;
	public final int FUNC_CODE_BEAT       = 11;
	public final int FUNC_CODE_BEAT_RE    = 12;
	
	public final int CUSTOM_CODE_SET  = 5;
	public final int CUSTOM_CODE_DATA = 8;
	
	public final int ADDR_WTR = 1;
}
