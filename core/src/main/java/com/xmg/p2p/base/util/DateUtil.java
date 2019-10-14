package com.xmg.p2p.base.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	public static Date getBeginDate(Date beginDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(beginDate);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE,0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}
	
	public static Date getEndDate(Date endDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(endDate);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE,59);
		calendar.set(Calendar.SECOND, 59);
		return calendar.getTime();
	}
	
	public static long timeInterval(Date firstTime,Date secondTime) {
		return Math.abs(firstTime.getTime()-secondTime.getTime());
	}
}
