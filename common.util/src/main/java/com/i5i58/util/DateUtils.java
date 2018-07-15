package com.i5i58.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

	public static final long hourMilliSecond = 60 * 60 * 1000;
	public static final long dayMilliSecond = 24 * 60 * 60 * 1000;
	public static final long monthMilliSecond = dayMilliSecond * 30;

	public static long getNowDate() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		Date newDate = sdf.parse(sdf.format(date));
		return newDate.getTime();
	}

	/**
	 * 获取本周一零点时间
	 * 
	 * @return long time
	 * @throws ParseException
	 */
	public static long getWeekStartTime() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		Date newDate = sdf.parse(sdf.format(cal.getTime()));
		return newDate.getTime();
	}

	public static long getNowDate(Locale locale) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", locale);
		Date date = new Date();
		Date newDate = sdf.parse(sdf.format(date));
		return newDate.getTime();
	}

	public static long getDate(Date date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date newDate = sdf.parse(sdf.format(date));
		return newDate.getTime();
	}

	public static Date timeToDate(long time) throws ParseException {
		return new Date(time);
	}

	public static long getNowTime() {
		return new Date().getTime();
	}

	public static long getTime(Date date) throws ParseException {
		return date.getTime();
	}

	public static long AddMonth(long time, int month) throws ParseException {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);
		c.add(Calendar.MONTH, month);
		return c.getTimeInMillis();
	}

	public static long addDay(long time, int day) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);
		c.add(Calendar.DAY_OF_YEAR, day);
		return c.getTimeInMillis();
	}

	public static Date addDay(Date date, int day) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_YEAR, day);
		return c.getTime();
	}

	public static long addDayToTime(Date date, int day) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_YEAR, day);
		return c.getTimeInMillis();
	}

	/**
	 * 增加时间
	 * 
	 * @author frank
	 * @param date
	 * @param time
	 *            秒
	 * @return
	 */
	public static Date addTime(Date date, int time) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.SECOND, time);
		return c.getTime();
	}

	/**
	 * 增加时间，返回long
	 * 
	 * @param date
	 * @param time
	 *            秒
	 * @return
	 */
	public static long addTimeToTime(Date date, int time) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.SECOND, time);
		return c.getTimeInMillis();
	}

	public static String getNowTimeString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(new Date());
	}

	public static String getNowTimeString(String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date());
	}

	public static String getTimeString(long time, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date(time));
	}

	public static String getTimeString(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(new Date(time));
	}

	public static String getDateString(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(new Date(time));
	}

	public static String getTimeString(Date time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(time);
	}

	/**
	 * @author songfl 是否是周末
	 */
	public static boolean isWeekend(long time) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek == 0 || dayOfWeek == 6) {
			return true;
		}
		return false;
	}

	/**
	 * 判断给定时间距离下个月的天数
	 * 
	 * @author songfl
	 * @param time
	 */
	public static int getDayToNextMonth(long time) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);
		int maxDayOfMonth = c.getMaximum(Calendar.DAY_OF_MONTH);
		int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);

		return maxDayOfMonth - dayOfMonth + 1;
	}

	/**
	 * @author songfl 计算天数差
	 * @throws ParseException
	 */
	public static long getDayInterval(long time1, long time2) throws ParseException {
		Date date1 = DateUtils.timeToDate(time1);
		long datetime1 = DateUtils.getDate(date1);

		Date date2 = DateUtils.timeToDate(time2);
		long datetime2 = DateUtils.getDate(date2);

		long intervalDays = (datetime2 - datetime1) / (1000 * 3600 * 24);

		return intervalDays;
	}

	/**
	 * @author huangl 计算月份差
	 * @throws ParseException
	 */
	public static int getMonthInterval(long time1, long time2) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time1);
		int year1 = c.get(Calendar.YEAR);
		int month1 = c.get(Calendar.MONTH);

		c.setTimeInMillis(time2);
		int year2 = c.get(Calendar.YEAR);
		int month2 = c.get(Calendar.MONTH);

		if (year1 == year2) {
			return month2 - month1;
		}
		return 12 * (year2 - year1) + month2 - month1;

	}

	/**
	 * 判断是否是同一个星期
	 * 
	 * @author songfl
	 */
	public static boolean isSameWeek(long time1, long time2) throws ParseException {
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setFirstDayOfWeek(Calendar.MONDAY);// 西方周日为一周的第一天，这里将周一设为一周第一天
		cal2.setFirstDayOfWeek(Calendar.MONDAY);
		cal1.setTimeInMillis(time1);
		cal2.setTimeInMillis(time2);
		if (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
				&& cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))// subYear==0,说明是同一年
		{
			return true;
		}
		return false;
	}

	/**
	 * 判断时间是否过期
	 * 
	 * @author songfl
	 * @throws ParseException
	 */
	public static boolean isTimeExpire(long deadline) throws ParseException {
		Date date = DateUtils.timeToDate(deadline);
		long datetime = DateUtils.getDate(date);

		return datetime < DateUtils.getNowDate();
	}

	public static long getLastMonthBeginning() {
		Calendar c = Calendar.getInstance();
		int year1 = c.get(Calendar.YEAR);
		int month1 = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int min = c.get(Calendar.MINUTE);
		int sec = c.get(Calendar.SECOND);
		int mill = c.get(Calendar.MILLISECOND);

		c.add(Calendar.DAY_OF_MONTH, -day + 1);
		c.add(Calendar.HOUR_OF_DAY, -hour);
		c.add(Calendar.MINUTE, -min);
		c.add(Calendar.SECOND, -sec);
		c.add(Calendar.MILLISECOND, -mill - 1);

		long time1 = c.getTimeInMillis();
		c.add(Calendar.MONTH, -1);
		c.add(Calendar.MILLISECOND, 1);
		long time2 = c.getTimeInMillis();
		return time2;
	}

	public static long getLastMonthEnd() {
		Calendar c = Calendar.getInstance();
		int year1 = c.get(Calendar.YEAR);
		int month1 = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int min = c.get(Calendar.MINUTE);
		int sec = c.get(Calendar.SECOND);
		int mill = c.get(Calendar.MILLISECOND);

		c.add(Calendar.DAY_OF_MONTH, -day + 1);
		c.add(Calendar.HOUR_OF_DAY, -hour);
		c.add(Calendar.MINUTE, -min);
		c.add(Calendar.SECOND, -sec);
		c.add(Calendar.MILLISECOND, -mill - 1);

		long time1 = c.getTimeInMillis();
		return time1;
	}
}
