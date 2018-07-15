package com.i5i58.test;

import java.text.ParseException;
import java.util.Date;

import com.i5i58.util.DateUtils;

public class Test {

	public static void main(String[] args) {
		try {
			System.out.println(DateUtils.getNowTimeString());
			long time = DateUtils.getNowTime();
			System.out.println(time);
			System.out.println(DateUtils.timeToDate(time));
			long date = DateUtils.getNowDate();
			System.out.println(date);
			System.out.println(DateUtils.timeToDate(date));
			Date d = new Date();
			System.out.println(DateUtils.getDate(d));
			System.out.println(DateUtils.getTime(d));
			System.out.println(DateUtils.timeToDate(date));
			System.out.println(DateUtils.AddMonth(DateUtils.getTime(d), 2));
			System.out.println(DateUtils.timeToDate(DateUtils.AddMonth(DateUtils.getTime(d), 2)));
			System.out.println(DateUtils.timeToDate(DateUtils.addDay(DateUtils.getTime(d), -2)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
