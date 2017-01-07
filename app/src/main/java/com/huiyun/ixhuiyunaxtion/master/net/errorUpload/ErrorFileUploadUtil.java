package com.huiyun.ixhuiyunaxtion.master.net.errorUpload;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.huiyun.ixhuiyunaxtion.master.StaticValues;
import com.huiyun.ixhuiyunaxtion.master.utils.FileUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

/**   
* @Description: 上传主机的报错文件 TODO
* @date 2015年4月21日 下午5:44:44 
* @version V1.0   
*/
public class ErrorFileUploadUtil {

	/**开始上传报错文件
	*  Function:
	*  @author Yangshao 
	*  2015年4月22日 上午11:38:19
	*/
	public static void startUploadErroFile() {
		List<File> errorFiles = getErrorFiles();
		startUpload(errorFiles);
	}

	/**得到错误记录文件
	*  @return
	*/
	private static List<File> getErrorFiles() {
		String source = FileUtils.getDir("Master");// 获得错误记录存储的文件夹
		List<File> fileList = new ArrayList<File>();
		File dirFile = new File(source);
		String endWith = ".txt";
		traverseDir(dirFile, endWith, fileList);
		return fileList;
	}

	/**深度遍历给定的文件夹，将符合要求的文件存到集合中
	 * 
	*  @param dirFile 要遍历的文件夹
	*  @param endWith 以该字符串结尾的文件才符合要求
	*  @param fileList 结果储存到该集合
	*/
	private static void traverseDir(File dirFile, String endWith,
			List<File> fileList) {
		File files[] = dirFile.listFiles();
		String name;
		for (File f : files) {
			if (f.isFile()) {
				name = f.getName();
				if (name.endsWith(endWith)) {
					fileList.add(f);
					// System.out.println("1个");
				}
			} else if (f.isDirectory()) {
				traverseDir(new File(dirFile, f.getName()), endWith, fileList);
			}
		}
	}

	/**
	 * 开始上传
	 * @param errorFiles 
	 */
	protected static void startUpload(List<File> errorFiles) {
		if (errorFiles != null && errorFiles.size() > 0) {
			for (File f : errorFiles) {
				uploadMethod(StaticValues.uploadHost, f, f.getName());
			}
		}
	}

	private static void uploadMethod(String url, final File file,
			String fileName) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("msg", fileName);
		params.addBodyParameter(file.getAbsolutePath().replace("/", ""), file);
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, url, params,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {
						System.out.println("conn...");
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						if (isUploading) {
							System.out.println("upload: " + current + "/"
									+ total);
						} else {
							System.out.println("reply: " + current + "/"
									+ total);
						}
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						System.out.println("reply: " + responseInfo.result);
						file.delete();
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						System.out.println(error.getExceptionCode() + ":" + msg);
					}
				});
	}
}
