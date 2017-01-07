package com.huiyun.ixhuiyunaxtion.master.inner;

public interface OnReceiveWTRCmdListener {
	/**当有命令接受到后回调
	 * @param socketId 发送信息来的链接id
	 * @param customCode 自定义码
	 * @param bytes
	 */
	void onReceive(int socketId, int customCode, byte[] bytes);
}
