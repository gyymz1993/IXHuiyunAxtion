package com.huiyun.ixhuiyunaxtion.master.inner;

public interface OnReceiveCmdListener {
	/**当有命令接受到后回调
	 * @param socketId 发送信息来的链接id
	 * @param bytes
	 */
	void onReceive(int socketId, byte[] bytes);
}
