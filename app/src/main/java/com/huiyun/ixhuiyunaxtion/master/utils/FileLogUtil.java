package com.huiyun.ixhuiyunaxtion.master.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

public class FileLogUtil {

	/**
	*  Function:记录错误信息到文件
	*  @param ex 错误
	*/
	public static void errorFileLog(Throwable ex) {
		PrintWriter err = null;
		try {
			err = new PrintWriter(new FileWriter(getALogFile("err_")));
			ex.printStackTrace(err);
			err.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.close(err);
		}
	}

	/**
	*  Function:得到一个可写的文件
	* @param string 开头的几个字母
	*  @return
	*/
	private static File getALogFile(String string) {
		String writableFile = FileUtils.getWritableFile("Master", string
				+ getCurrentTimeString(false) + ".txt");
		return new File(writableFile);
	}

	/**
	*  Function:得到格式化的当前时间
	*  @param withSecond 是否带秒
	*  @return 格式化的当前时间
	*/
	public static String getCurrentTimeString(boolean withSecond) {
		Calendar c = Calendar.getInstance();
		int month = c.get(Calendar.MONTH);
		int date = c.get(Calendar.DAY_OF_MONTH);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		int second = c.get(Calendar.SECOND);
		if (withSecond)
			return month + "_" + date + "__" + hour + "_" + minute + "_"
					+ second + "__";
		else
			return month + "_" + date + "__" + hour + "_" + minute;
	}

	
	 /**
	 *  Function:把信息打印到指定文件中，前面会带有时间
	 *  @param log 要打印的信息
	 *  @param filePath 文件路径
	 */
	public static void printFileLog(String log, String filePath){
		log = getCurrentTimeString(true) + ":" + log + "\n";
		FileUtils.writeFile(log, filePath, true);
	}
}
