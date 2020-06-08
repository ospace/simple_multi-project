package com.tistory.ospace.core.util;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateUtils {
	public static final ZoneId LOCAL = ZoneId.systemDefault();
	public static final ZoneId UTC = ZoneId.of("UTC");
	
	public static final DateTimeFormatterBuilder defaultDateTimeBuilder = new DateTimeFormatterBuilder()
			.appendOptional(DateTimeFormatter.ISO_DATE_TIME);
	
	public static final DateTimeFormatterBuilder defaultDateBuilder = new DateTimeFormatterBuilder()
			.appendOptional(DateTimeFormatter.ISO_DATE)
			.appendOptional(DateTimeFormatter.BASIC_ISO_DATE);
	
	/**
	 * String convert to ZonedDateTime
	 * @param dateStr
	 * @param standardZone
	 * @param changeZone
	 * @return
	 */
	public static ZonedDateTime toZonedDateTime(String dateStr, DateTimeFormatterBuilder builder, ZoneId standardZone, ZoneId changeZone) {
		ZonedDateTime date = toZonedDateTime(dateStr, builder, standardZone).withZoneSameInstant(changeZone);
		return date;
	}
	
	public static ZonedDateTime toZonedDateTime(String dateStr, DateTimeFormatterBuilder builder) {
		if (StringUtils.isEmpty(dateStr)) return null;
		ZonedDateTime date = toZonedDateTime(dateStr, builder, LOCAL);
		return date;
	}
	
    public static ZonedDateTime toZonedDateTime(String date, DateTimeFormatterBuilder builder, ZoneId zone) {
        if (StringUtils.isEmpty(date) || null == builder || null == zone) return null;
        return toZonedDateTime(date, builder.toFormatter().withZone(zone));
    }
	
    public static ZonedDateTime toZonedDateTime(String date, String pattern, ZoneId zone) {
        if(StringUtils.isEmpty(date) || StringUtils.isEmpty(pattern)) return null;
        return ZonedDateTime.parse(date, DateTimeFormatter.ofPattern(pattern).withZone(zone));
    }
    
	public static ZonedDateTime toZonedDateTime(String date, String pattern) {
        if(StringUtils.isEmpty(date) || StringUtils.isEmpty(pattern)) return null;
        return ZonedDateTime.parse(date, DateTimeFormatter.ofPattern(pattern));
    }
	
	public static ZonedDateTime toZonedDateTime(String date, DateTimeFormatter formatter) {
	    if(StringUtils.isEmpty(date) || null == formatter) return null;
	    return ZonedDateTime.parse(date, formatter);
	}
	
	/**
	 * Builder를 넘겨주지 않는 경우 default로 선언 해놓은 DateTimeFormatterBuilder 사용
	 * @param dateStr
	 * @return
	 */
//	public static ZonedDateTime toDateTime(String dateStr) {
//		if (StringUtils.isEmpty(dateStr)) return null;
//		ZonedDateTime date = toDateTIme(dateStr, LOCAL);
//		return date;
//	}
	
	/**
	 * Calendar convert to ZonedDateTime
	 * @param calendar
	 * @return
	 */
	public static ZonedDateTime toZonedDateTime(Calendar calendar, ZoneId standardZone, ZoneId changeZone) {
	    if(null == calendar || null == standardZone || null == changeZone) return null;
		ZonedDateTime date = ZonedDateTime.ofInstant(calendar.toInstant(), standardZone).withZoneSameInstant(changeZone);
		return date;
	}
	
	public static ZonedDateTime toZonedDateTime(Calendar calendar, ZoneId standardZone) {
	    if(null == calendar || null == standardZone) return null;
		ZonedDateTime date = ZonedDateTime.ofInstant(calendar.toInstant(), standardZone);
		return date;
	}
	
	public static ZonedDateTime toZonedDateTime(Calendar calendar) {
	    if(null == calendar) return null;
		ZonedDateTime date = ZonedDateTime.ofInstant(calendar.toInstant(), LOCAL);
		return date;
	}
	
	/**
	 * Millisecond convert to ZonedDateTime
	 * @param tms
	 * @return
	 */
	public static ZonedDateTime toZonedDateTime(long tms) {
		return Instant.ofEpochMilli(tms).atZone(LOCAL);
	}
	
    /**
	 * ZonedDateTime convert to String with pattern
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String toString(ZonedDateTime date, String pattern) {
		if(null == date || null == pattern) return null;
		return toString(date, DateTimeFormatter.ofPattern(pattern));
	}
	
	public static String toString(ZonedDateTime date, DateTimeFormatter formatter) {
	    if(null == date || null == formatter) return null;
	    return date.format(formatter);
	}
	
	/**
	 * LocalTime convert to String
	 * @param time
	 * @param pattern
	 * @return
	 */
	public static String toString(LocalTime time, String pattern) {
        if(null == time || null == pattern) return null;
        return toString(time, DateTimeFormatter.ofPattern(pattern));
    }
    
    public static String toString(LocalTime time, DateTimeFormatter formatter) {
        if(null == time || null == formatter) return null;
        return time.format(formatter);
    }
    
    public static LocalTime toLocalTime(String time, String pattern) {
        if(StringUtils.isEmpty(time) || StringUtils.isEmpty(pattern)) return null;
        return LocalTime.parse(time, DateTimeFormatter.ofPattern(pattern));
    }
    
    public static LocalTime toLocalTime(String time, DateTimeFormatter formatter) {
        if(StringUtils.isEmpty(time) || null == formatter) return null;
        return LocalTime.parse(time, formatter);
    }
    
    /**
     * LocalDateTime convert to String
     * @param date
     * @param pattern
     * @return
     */
    public static String toString(LocalDateTime date, String pattern) {
        if(null == date || null == pattern) return null;
        return toString(date, DateTimeFormatter.ofPattern(pattern));
    }

    public static String toString(LocalDateTime date, DateTimeFormatter formatter) {
        if(null == date || null == formatter) return null;
        return date.format(formatter);
    }
	
    /**
     * String convert to LocalDateTime with pattern
     * @param date
     * @param pattern
     * @return
     */
    public static LocalDateTime toLocalDateTime(String date, String pattern) {
        if(StringUtils.isEmpty(date) || StringUtils.isEmpty(pattern)) return null;
        return toLocalDateTime(date, DateTimeFormatter.ofPattern(pattern));
    }
    
    public static LocalDateTime toLocalDateTime(String date, DateTimeFormatter formatter) {
        if(StringUtils.isEmpty(date) || null == formatter) return null;
        return LocalDateTime.parse(date, formatter);
    }
        
    public static Date toDate(LocalDateTime dateTime) {
        if(null == dateTime) return null;
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
    
    public static LocalDateTime toLocalDateTime(Date date) {
        if(null == date) return null;
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }
    
    /*
     * Date는 가급적 지양
     */
    public static String toString(Date date, String formatter) {
        if(null == date) return null;
        SimpleDateFormat transFormat = new SimpleDateFormat(formatter);
        return transFormat.format(date);
    }
    
	/**
	 * 요일명 가져오기
	 * locale parameter 넘겨줄 시 해당 locale의 요일명으로 반환
	 * @param date
	 * @param locale
	 * @return
	 */
	public static String getDayOfWeek(ZonedDateTime date, Locale locale) {
		return date.getDayOfWeek().getDisplayName(TextStyle.FULL, locale);
	}
	public static String getDayOfWeek(ZonedDateTime date) {
		return date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
	}
	
	/**
	 * get today
	 * @return
	 */
	public static ZonedDateTime now() {
		return ZonedDateTime.now(LOCAL);
	}
	
	public static String now(String pattern) {
		return toString(now(), pattern);
	}
	
	public static String now(DateTimeFormatter formatter) {
        return toString(now(), formatter);
    }
	
	//current 날짜 기준으로 생일을 적용하여 나이를 계산
	public static int getAge(ZonedDateTime birthday, ZonedDateTime current) {
		return (int) ChronoUnit.YEARS.between(birthday, current);
	}
	
	/**
	 * get difference of days
	 * @param start
	 * @param end
	 * @return
	 */
	public static long getDiffOfDays(ZonedDateTime start, ZonedDateTime end) {
		return ChronoUnit.DAYS.between(start.toInstant(), end.toInstant());
	}
	
	/**
	 * get difference of hours
	 * @param start
	 * @param end
	 * @return
	 */
	public static long getDiffOfHours(ZonedDateTime start, ZonedDateTime end) {
		return ChronoUnit.HOURS.between(start.toInstant(), end.toInstant());
	}
	
	/**
	 * get difference of minutes
	 * @param start
	 * @param end
	 * @return
	 */
	public static long getDiffOfMinutes(ZonedDateTime start, ZonedDateTime end) {
		return ChronoUnit.MINUTES.between(start.toInstant(), end.toInstant());
	}
	
	/**
	 * get difference of seconds
	 * @param start
	 * @param end
	 * @return
	 */
	public static long getDiffOfSeconds(ZonedDateTime start, ZonedDateTime end) {
		return ChronoUnit.SECONDS.between(start.toInstant(), end.toInstant());
	}
	
	/**
	 * 입력받은 시간(분) 정보로 ZoneId 반환
	 * @param minutes
	 * @return
	 */
	public static ZoneId getZoneIdOfMinutes(long minutes) {
		int hours = (int)(minutes / 60);
		return ZoneOffset.ofHours(hours);
	}
	
	public static double offset(Date l, Date r) {
		return Math.abs(l.getTime() - r.getTime())/1000;
	}
	
	public static double offset(LocalDateTime l, LocalDateTime r) {
		return Duration.between(l, r).getSeconds();
	}
	
	public static double offset(Calendar l, Calendar r) {
		return Math.abs(l.getTimeInMillis()-r.getTimeInMillis())/1000;
	}

	/***
     * 초단위를 문자열로 변환. 
     * @param sec : 초
     * @param fmt : 포멧팅 문자열({0}: day, {1}: hour, {2}: min, {3}: sec)
     * @return String
     */
	public static String timeFmt(long sec, String fmt) {
		long min = sec/60;
		long hour = min/60;
		long day = hour/24;
		
		return MessageFormat.format(fmt, day, hour%24, min%60, sec%60);
	}
	
	public static boolean isTimeout(ZonedDateTime timeout) {
		return null == timeout ? true : ZonedDateTime.now().compareTo(timeout) >= 0;
	}
	
	public static boolean isTimeout(LocalDateTime timeout) {
		return null == timeout ? true : LocalDateTime.now().compareTo(timeout) >= 0;
	}
	
	/**
     * 현재 년
     * @return int
     */
    public static int getYear()  {
        return now().getYear();
     }
    
    /***
     * 현재 월
     * @return int
     */
    public static int getMonth() {
        return now().getMonth().getValue();
    }
    
    /***
     * 현재 일
     * @return int
     */
    public static int getDay() {
        return now().getDayOfMonth();
    }
    
    /***
     * 현재 요일
     * @return String
     */
    public static String getWeek() {
        String [] week = {"","일","월","화","수","목","금","토"};
        return getWeek(week); 
    }
    
    public static String getWeek(String [] week) {
        return week[now().getDayOfWeek().getValue()]; 
    }
    
    /***
     * 현재달 주 (1주, 2주 ...)
     * @return int
     */
    public static int getCurrentWeek(){
        Calendar cal = Calendar.getInstance();
        int result = cal.get(Calendar.WEEK_OF_MONTH) ;
        return result;
    }
    
    /***
     * 해당년월의 마지막 날짜
     * @param nYear : 년도
     * @param nMonth : 월
     * @return int
     */
    public static int getLastDay(int nYear, int nMonth){
        GregorianCalendar cld = new GregorianCalendar (nYear, nMonth - 1, 1);
        int result = cld.getActualMaximum(Calendar.DAY_OF_MONTH);
        return result;
    }
    
    /***
     * 해당년월의 첫번째 날짜의 요일(1:SUNDAY, 2:MONDAY...)
     * @param nYear
     * @param nMonth
     * @return int
     */
    public static int getFirstWeekday(int nYear, int nMonth){
        GregorianCalendar cld = new GregorianCalendar (nYear, nMonth - 1, 1);
        int result = cld.get(Calendar.DAY_OF_WEEK);
        return result;
    }
    
    /***
     * 해당년월의 주의 개수
     * @param nFristWeekday : 그 달의 첫째날의 요일
     * @param nToDay : 그 달의 날짜 수
     * @return int
     */
    public static int getWeekCount(int nFristWeekday, int nToDay){
        int nCountDay = nFristWeekday + nToDay - 1;
        int result = (nCountDay / 7);
        if ((nCountDay % 7) > 0) {
            result++;
        }
        return result;
    }
}
