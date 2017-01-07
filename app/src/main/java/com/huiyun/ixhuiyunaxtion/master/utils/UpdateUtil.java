package com.huiyun.ixhuiyunaxtion.master.utils;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;

import com.huiyun.ixhuiyunaxtion.master.StaticValues;
import com.huiyun.ixhuiyunaxtion.master.bean.table.Version;
import com.huiyun.ixhuiyunaxtion.master.inner.OnResultListener;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

public class UpdateUtil {

	// 下载保存路径 主机保存位置
	public static String mSavePath = "/sdcard/ix.apk";

	public static OnResultListener<Version> onResultListener;

	public static OnResultListener<Version> getOnResultListener() {
		return onResultListener;
	}

	public static void setOnResultListener(
			OnResultListener<Version> monResultListener) {
		onResultListener = monResultListener;
	}

	public static void getVersion() {
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, StaticValues.versionFile,
				new RequestCallBack<String>() {
					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						System.out.println(current + "/" + total);
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						System.out.println(responseInfo.result);
						try {
							JSONObject jsonObject = new JSONObject(
									responseInfo.result);
							int version = jsonObject.getInt("version");
							String desc = jsonObject.getString("desc");
							Version versions = new Version(version, desc);
							if (onResultListener != null) {
								onResultListener.onResult(true, versions);
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onStart() {
					}

					@Override
					public void onFailure(HttpException error, String msg) {
					}
				});
	}

	public static HttpHandler<?> handler = null;

	public static void downLoadNewApk(final Context context) {
		/**
		 * 点击取消停止下载
		 */
		HttpUtils http = new HttpUtils();
		@SuppressWarnings("unused")
		HttpHandler<File> handler = http.download(StaticValues.downLoadFile,
				mSavePath, true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
				true, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
				new RequestCallBack<File>() {

					@Override
					public void onStart() {
						System.out.println("conn...");
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						System.out.println("onLoading" + current + "/" + total);
						// mProgress.setProgress(x);
					}

					@Override
					public void onSuccess(ResponseInfo<File> responseInfo) {
						System.out.println("downloaded:"
								+ responseInfo.result.getPath());
						UIUtils.runInMainThread(new Runnable() {
							@Override
							public void run() {
								/**
								 * 安装
								 */
								InstallApkUtils.installAndStartApk(context,
										mSavePath);
							}
						});
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						// apkfile.delete();
						System.out.println("onFailure" + msg);
					}
				});
	}

	/**
	 * 获取软件版本号
	 * 
	 * @param context
	 * @return
	 */
	public static int getVersionCode(Context context) {
		int versionCode = 0;
		try {
			// 获取软件版本号，对应AndroidManifest.xml下android:versionCode
			versionCode = context.getPackageManager().getPackageInfo(
					"com.huiyun.master", 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}

	/**
	 * 安装APK文件
	 */
	// public static String mSavePath =
	// Environment.getExternalStorageDirectory().toString()
	// + "/ix.apk";

	public static File apkfile = new File(mSavePath);

	public static void installApk(Context context) {
		if (!isExis()) {
			return;
		}
		System.out.println("安装");
		// 通过Intent安装APK文件
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		context.startActivity(i);
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	public static boolean isExis() {
		File apkfile = new File(mSavePath);
		if (apkfile.exists()) {
			return true;
		}
		return false;
	}

}
