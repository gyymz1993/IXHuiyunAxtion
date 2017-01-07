package com.huiyun.ixhuiyunaxtion.master.inner;

/**
 * 有结果后回调
 * @author torahs
 *
 * @param <T>
 */
public interface OnResultListener<T> {
	void onResult(boolean isSucceed, T obj);
}