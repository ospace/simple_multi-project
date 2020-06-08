package com.tistory.ospace.simpleproject.util;

import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.tistory.ospace.core.util.DateUtils;

public class DateHelper {
	private static final String EMPTY = "";
	private static DateTimeFormatter fmtDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	private static DateTimeFormatter fmtDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private static DateTimeFormatter fmtDateShort = DateTimeFormatter.ofPattern("MM-dd");
	//private static DateTimeFormatter fmtDateShort2 = DateTimeFormatter.ofPattern("yyyyMM");
	
	private static DateTimeFormatter fmtTime = DateTimeFormatter.ofPattern("HH:mm:ss");
	private static DateTimeFormatter fmtTimeShort = DateTimeFormatter.ofPattern("HH:mm");
	
	public static String toStringDateTime(LocalDateTime date) {
		if(null == date) return EMPTY;
		return DateUtils.toString(date, fmtDateTime);
	}
	
	public static String toStringDate(LocalDateTime date) {
		if(null == date) return EMPTY;
		return DateUtils.toString(date, fmtDate);
	}
	
	public static String toStringTimeShort(LocalTime date) {
		if(null == date) return EMPTY;
		return DateUtils.toString(date, fmtTimeShort);
	}
	
	public static String toStringTime(LocalTime date) {
		if(null == date) return EMPTY;
		return DateUtils.toString(date, fmtTime);
	}
	
	public static String toStringDateShort(LocalDateTime date) {
		if(null == date) return EMPTY;
		return DateUtils.toString(date, fmtDateShort);
	}
	
	public static String toStringDateShort2(LocalDateTime date) {
		if(null == date) return EMPTY;
		return DateUtils.toString(date, fmtDateShort);
	}
}
