package com.yjf.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	private static SimpleDateFormat shortDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat fullDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 日前转换
	 * @param date
	 * @return yyyyMMdd
	 */
	public static String shortFormatDate(Date date){
		return shortDateFormat.format(date);
	}
	
	/**
	 * 日前转换
	 * @param date
	 * @return yyyy-MM-dd
	 */
	public static String formatDate(Date date){
		return simpleDateFormat.format(date);
	}
	
	/**
	 * 日前转换
	 * @param date
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String formatTime(Date date){
		return fullDateFormat.format(date);
	}
	
	/**
	 * 获取日期的时间差
	 * @param oldDate
	 * @param newDate
	 * @return
	 */
	public static String getTimeDiff(Date oldDate, Date newDate){
		long diff = newDate.getTime() - oldDate.getTime();
		long day = (diff/(60*60*1000*24));
		long hour=(diff/(60*60*1000) - day*24);
		long min=((diff/(60*1000))- day*24*60 - hour*60);
		long s=(diff/1000 - day*24*60*60 - hour*60*60 - min*60);
		if(day > 0) {
			return day + "天" + hour + ":" + min + ":" + s;
		}
		return hour + ":" + min + ":" + s;
	}

	/**
	 * 获取上一天
	 * @return
	 */
	public static Date getPreviousDay(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, -1);
		Date previousDay = calendar.getTime();
		return previousDay;
	}
}
