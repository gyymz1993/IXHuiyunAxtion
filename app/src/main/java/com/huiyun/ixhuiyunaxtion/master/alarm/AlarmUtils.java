package com.huiyun.ixhuiyunaxtion.master.alarm;

import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;
import com.huiyun.ixhuiyunaxtion.master.activity.MultiTextBuffer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * @Description: 时间日期工具类
 * @date 2015年4月2日 上午11:41:47
 * @version V1.0
 */
public class AlarmUtils {

	/**
	 * Function:得到当前时间
	 * 
	 * @author Yangshao 2015年4月2日 上 午11:42:02
	 * @return
	 */
	public static long getNowTimeMinuties() {
		return System.currentTimeMillis();
	}

	/**
	 * Function: 传入一个时间和当前时间比较
	 * 
	 * @author Yangshao 2015年4月2日 上午11:41:43
	 * @param setTime
	 * @return
	 */
	public static boolean differSetTimeAndNowTime(long setTime) {
		if (setTime >= getNowTimeMinuties()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Function: 得到一个周期时间
	 * 
	 * @author Yangshao 2015年4月2日 上午11:42:33
	 * @param differDays
	 * @return
	 */
	public static long getDifferMillis(int differDays) {
		return differDays * 24 * 60 * 60 * 1000;
	}

	/**
	 * Function:得到一个日期格式的时间 返回String时间 MM:dd:yyyy HH:mm:ss
	 * 
	 * @author Yangshao 2015年4月2日 上午11:46:24
	 * @param strDate
	 * @return
	 */
	public static String getDateOfString(String strDate) {
		SimpleDateFormat sdf = null;
		long time = 0;
		try {
			time = Long.valueOf(strDate);
			sdf = new SimpleDateFormat("MM:dd:yyyy HH:mm:ss");
			System.out.println(sdf.format(new Date(time)));
		} catch (Exception e) {
			return null;
		}
		return sdf.format(new Date(time));
	}

	/**
	 * 得到一个日期中取出对应时间和当前的日期
	 * 
	 * 得到今天的时间
	 * 
	 * @param timeLng
	 */
	public static String getTodayTime(String timeLng) {
		Long longtime = Long.valueOf(timeLng);
		Date date = new Date(longtime);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		Date nowDate = new Date(getNowTimeMinuties());
		Calendar nowCalendar = Calendar.getInstance();
		nowCalendar.setTime(nowDate);
		nowCalendar.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY));
		nowCalendar.set(Calendar.MINUTE, cal.get(Calendar.MINUTE));
		nowCalendar.set(Calendar.SECOND, cal.get(Calendar.SECOND));
		return String.valueOf(nowCalendar.getTimeInMillis());
	}

	public static String getTomorrowTime(String timeLng) {
		Long longtime = Long.valueOf(timeLng);
		Date date = new Date(longtime);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		Date nowDate = new Date(getNowTimeMinuties());
		Calendar nowCalendar = Calendar.getInstance();
		nowCalendar.setTime(nowDate);
		nowCalendar.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY));
		nowCalendar.set(Calendar.MINUTE, cal.get(Calendar.MINUTE));
		nowCalendar.set(Calendar.SECOND, cal.get(Calendar.SECOND));
		// 把日期往后增加一天.整数往后推,负数往前移动
		nowCalendar.add(Calendar.DATE, 1);//
		return String.valueOf(nowCalendar.getTimeInMillis());
	}

	/**
	 * Function: 得到时间 HH:mm:ss 格式的时间
	 * 
	 * @author Yangshao 2015年4月2日 下午2:29:52
	 * @param strDate
	 * @return
	 */
	public static String getTimeforLong(String strDate) {
		long time = Long.valueOf(strDate);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		// System.out.println(sdf.format(new Date(time)));
		return sdf.format(new Date(time));
	}

	/**
	 * @param strTime
	 *            时间格式 HH:mm:ss 得到日期的通用Long类型
	 * @return 转 Calendar
	 */
	public static long stringToCalendar(String strTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		Date date;
		Calendar calendar = Calendar.getInstance();
		try {
			date = sdf.parse(strTime);
			calendar.setTime(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return calendar.getTimeInMillis();
	}

	/**
	 * Function: 传入得到HH:mm:ss 格式的时间
	 * 
	 * @author Yangshao 2015年4月2日 下午1:34:18
	 * @param
	 * @return
	 */
	public static boolean compareNowTime(String time) {
		// 得到传入时间
		String getTime = AlarmUtils.getTimeforLong(time);
		// 得到当前时间
		String strnowtime = getTimeforLong(String.valueOf(getNowTimeMinuties()));
		MainActivity.showString("当前时间：" + strnowtime,
				MultiTextBuffer.TYPE_OTHER);
		MainActivity.showString("传入时间：" + getTime, MultiTextBuffer.TYPE_OTHER);
		return compareTime(getTime, strnowtime);
	}

	/**
	 * Function: 比较时间大小
	 * 
	 * @author Yangshao 2015年4月2日 上午11:51:22
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static boolean compareTime(String time1, String time2) {
		if (stringToCalendar(time1) > stringToCalendar(time2)) {
			return true;
		}
		return false;
	}

	/**
	 * Function:得到一个年月日的字符串
	 * 
	 * @author YangShao 2015年4月25日 下午3:32:48
	 * @param timer
	 * @return
	 */
	public static String getStringToYear(String timer) {
		String year, month, day, hour, minute;
		year = Integer.valueOf(timer.split(" ")[0].split("-")[0]).toString();
		month = String
				.valueOf(Integer.valueOf(timer.split(" ")[0].split("-")[1]));
		day = Integer.valueOf(timer.split(" ")[0].split("-")[2]).toString();
		hour = Integer.valueOf(timer.split(" ")[1].split(":")[0]).toString();
		minute = Integer.valueOf(timer.split(" ")[1].split(":")[1]).toString();
		return year + month + day + hour + minute;
	}

	/**
	 * Function:得到一个年月日
	 * 
	 * @author YangShao 2015年4月25日 下午3:32:48
	 * @return
	 */
	public static String getStringToDate(String timer) {
		String year, month, day;
		year = Integer.valueOf(timer.split(" ")[0].split("-")[0]).toString();
		month = String
				.valueOf(Integer.valueOf(timer.split(" ")[0].split("-")[1]));
		day = Integer.valueOf(timer.split(" ")[0].split("-")[2]).toString();
		return year + month + day;
	}

	/**
	 * Function:得到一个时间
	 * 
	 * @author YangShao 2015年4月25日 下午3:32:48
	 * @return
	 */
	public static String getStringToTime(String date) {
		String timer = AlarmUtils.getDateOfString(date);
		String hour, minute, second;
		hour = Integer.valueOf(timer.split(" ")[1].split(":")[0]).toString();
		minute = Integer.valueOf(timer.split(" ")[1].split(":")[1]).toString();
		second = Integer.valueOf(timer.split(" ")[1].split(":")[2]).toString();
		return hour + minute + second;
	}

}
