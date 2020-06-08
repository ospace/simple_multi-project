package jdk;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tistory.ospace.core.data.Tuple;
import com.tistory.ospace.core.util.CmmUtils;
import com.tistory.ospace.core.util.DateUtils;


public class TestZonedDateTime {
	private static final Logger logger = LoggerFactory.getLogger(TestZonedDateTime.class);
	
	public static final String SOAP_DATETIME_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
	public static final String SOAP_DATETIME_FORMAT_PATTERN2 = "MM/dd/yyyy HH:mm:ss";
	public static final String SOAP_DATETIME_FORMAT_PATTERN_FRONT = "yyyyMMddHHmm";
	
	public static final String SOAP_DATE_FORMAT_PATTERN = "yyyyMMdd";
	public static final String SOAP_DATE_FORMAT_PATTERN2 = "yyyy-MM-dd";
	
	public static final DateTimeFormatter SOAP_DATETIME_FORMATTER = DateTimeFormatter.ofPattern(SOAP_DATETIME_FORMAT_PATTERN);
	public static final DateTimeFormatter SOAP_DATETIME_FORMATTER2 = DateTimeFormatter.ofPattern(SOAP_DATETIME_FORMAT_PATTERN2);
	public static final DateTimeFormatter SOAP_DATETIME_FORMATTER3 = DateTimeFormatter.ofPattern(SOAP_DATETIME_FORMAT_PATTERN_FRONT);
	
	public static final DateTimeFormatter SOAP_DATE_FORMATTER = DateTimeFormatter.ofPattern(SOAP_DATE_FORMAT_PATTERN);
	public static final DateTimeFormatter SOAP_DATE_FORMATTER2 = DateTimeFormatter.ofPattern(SOAP_DATE_FORMAT_PATTERN2);
	
	@Test
	public void testDatetime() throws Exception {
		ZoneId seoul = ZoneId.of("Asia/Seoul");
		ZonedDateTime theTime = ZonedDateTime.of(2015, 2, 3, 1, 15, 43, 0, seoul);
		ZonedDateTime theTime1 = ZonedDateTime.of(2016, 10, 3, 1, 15, 43, 0, seoul);
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm");
		
		LocalDateTime local = LocalDateTime.of(2018, 12, 25, 18, 30);
		
		logger.info(">>>>>> java localDateTime: {}", local.format(formatter));
		logger.info(">>>>>> java localDateTime parse: {}", LocalDateTime.parse("2018-08-13 18:30", formatter1));
		logger.info(">>>>>> java localDateTime parse2: {}", LocalDateTime.parse("08/13/2018 - 18:30", formatter2));
		logger.info("---------------------------------------------------------");
		
		logger.info(">>>>>> java theTime: {}", theTime.withZoneSameInstant(ZoneId.of("Asia/Tokyo")).format(formatter1));
		logger.info(">>>>>> java localDateTime plus 2days: {}", local.plusDays(2).format(formatter));
		logger.info(">>>>>> java localDateTime set parallax(no format): {}", local.plusHours(9));
		logger.info("---------------------------------------------------------");
		
		Calendar now = Calendar.getInstance();
		TimeZone tz = now.getTimeZone();
		LocalDateTime ldt = LocalDateTime.ofInstant(now.toInstant(), tz.toZoneId());

		logger.info(">>>>>> before to convert: {}", now);
		logger.info(">>>>>> time zone: {}", tz);
		logger.info(">>>>>> convert LocalDateTime: {}", ldt);
		logger.info("---------------------------------------------------------");
		
		Calendar cal = Calendar.getInstance();
//		cal.setTime(Date.from(local.toInstant(ZoneOffset.UTC)));
		cal.setTime(Date.from(theTime.toInstant()));
		logger.info(">>>>> localDateTime convert to Calendar!!! : {}", cal.getTime());
		logger.info("---------------------------------------------------------");
		
		DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
		builder.appendOptional(formatter1).appendOptional(formatter2);
		
		DateTimeFormatter f = builder.toFormatter();
		
		logger.info(">>>>> formatter : {}", LocalDateTime.parse("2018-08-13 18:30", f));
		logger.info("---------------------------------------------------------");
		
		logger.info(">>>>> difference : {}", Period.between(theTime.toLocalDate(), theTime1.toLocalDate()));
		logger.info(">>>>> chrono unit difference : {}", ChronoUnit.MONTHS.between(theTime.toLocalDate(), theTime1.toLocalDate()));
		logger.info(">>>>> week : {}", local.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH));
		logger.info("---------------------------------------------------------");
		
//		ZonedDateTime t = getToday("MM/dd/yyyy - HH:mm");
		ZonedDateTime t = getToday();
		logger.info(">>>>>> zone date time test : {}", t);
		logger.info("---------------------------------------------------------");
		
		LocalDate local2 = LocalDate.parse("20180327", DateTimeFormatter.ofPattern("yyyyMMdd"));
		LocalDateTime local3 = local2.atStartOfDay();
		logger.info(">>>>>> pasing : {}", local2);
		logger.info(">>>>>> convert to localDateTime : {}", local3);
		logger.info("---------------------------------------------------------");
	}
	
	@Test
	public void testFormatter() {
		ZonedDateTime date = getDateTime("2018-08-13T18:30:00");
		logger.info(">>>> getDateTime!! : {}", date);
		Assert.assertTrue(null != date);
	}
	
	@Test
	public void testDateParse() {
		ZonedDateTime date = ZonedDateTime.of(LocalDate.parse("2018-08-13", SOAP_DATE_FORMATTER2).atStartOfDay(), ZoneId.systemDefault());
		
		logger.info(">>>> getDateTime!! : {}", date);
		Assert.assertTrue(null != date);
	}
	
	@Test
	public void testDateGenericFormat() {
		DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder()
				.appendOptional(SOAP_DATETIME_FORMATTER)
				.appendOptional(SOAP_DATETIME_FORMATTER2)
				.appendOptional(SOAP_DATETIME_FORMATTER3)
				.appendOptional(SOAP_DATE_FORMATTER)
				.appendOptional(SOAP_DATE_FORMATTER2);
		
		ZonedDateTime date = null;
		ZoneId zone = ZoneId.of("GMT");
		String dateStr = "20180327";
		
		if(dateStr.length() > 10) {
			date = ZonedDateTime.parse(dateStr, builder.toFormatter().withZone(zone));
		} else {
			LocalDate localDate = LocalDate.parse(dateStr, builder.toFormatter());
			date = ZonedDateTime.of(localDate, LocalTime.MIDNIGHT, zone);
		}
		
		logger.info(">>>> date : {}", date);
		Assert.assertNotNull(date);
	}
	
	@Test
	public void testDateTimeFormatter() {
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
//		ZonedDateTime date = ZonedDateTime.parse("2018-08-24T16:00:00.306", DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneId.systemDefault()));
//		ZonedDateTime date = ZonedDateTime.parse("08/24/2018 09:20", DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm").withZone(ZoneId.systemDefault()));
//		LocalDate date = LocalDate.parse("2018-08-24", DateTimeFormatter.ISO_DATE.withZone(ZoneId.systemDefault()));
//		LocalDate date = LocalDate.parse("20180824", DateTimeFormatter.BASIC_ISO_DATE.withZone(ZoneId.systemDefault()));
		
		String date = ZonedDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneId.systemDefault()));
		DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder().append(DateTimeFormatter.ISO_DATE_TIME);
				
		logger.info(">>>> date : {}", builder);
		Assert.assertNotNull(date);
	}
	
	@Test
	public void testDateStrConvert() {
//		ZonedDateTime date = ZonedDateTime.parse("1966-03-01T00:00:00.193", DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneId.systemDefault()));
		ZonedDateTime date = ZonedDateTime.parse("1966-03-01T00:00:00.193+09:00", DateTimeFormatter.ISO_DATE_TIME);
		logger.info(">>>> date of pattern : {}", date.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")));
		logger.info(">>>> date            : {}", date.getZone());
		Assert.assertNotNull(date);
	}
	
	@Test
	public void testChangeZone() {
		ZoneId zone = ZoneId.systemDefault();
		ZonedDateTime date = ZonedDateTime.now(zone);
		
		logger.info(">>>> date : {}", date.withZoneSameInstant(ZoneId.of("GMT")));
		Assert.assertNotNull(date);
	}
	
	@Test
	public void testSetMillisecond() {
		long milli = System.currentTimeMillis();
		logger.info(">>>> milli : {}", milli);
		ZonedDateTime date = Instant.ofEpochMilli(milli).atZone(ZoneId.systemDefault());
		logger.info(">>>> date : {}", date);
	}
	
	@Test
	public void testParallax() {
		ZonedDateTime date1 = ZonedDateTime.parse("2018-08-16T00:00:00", DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneId.of("Asia/Seoul")));
		ZonedDateTime date2 = ZonedDateTime.parse("2018-08-17T00:00:00", DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneId.of("America/Araguaina")));
		// same date2
		ZonedDateTime date3 = date2.withZoneSameInstant(ZoneId.of("UTC+03:00"));
		
		// zoneId가 다를 경우 zonedDateTime에서 알아서 UTC+00:00 기준으로 변환 후 차이값 계산
		logger.info(">>>> date 1 : {}", date1);
		logger.info(">>>> date 2 : {}", date2);
		logger.info(">>>> date 3 : {}", date3);
		logger.info("");
		logger.info(">>>> date 1 UTC+00:00 Time : {}", date1.withZoneSameInstant(ZoneId.of("GMT")));
		logger.info(">>>> date 2 UTC+00:00 Time : {}", date2.withZoneSameInstant(ZoneId.of("GMT")));
		logger.info(">>>> date 3 UTC+00:00 Time : {}", date3.withZoneSameInstant(ZoneId.of("GMT")));
		logger.info("");
		
		long chronoDays = ChronoUnit.DAYS.between(date1, date2);
		long chronoHours = ChronoUnit.HOURS.between(date1, date2);
		long chronoMinutes = ChronoUnit.MINUTES.between(date1, date2);
		
		logger.info(">>>> days : {}", chronoDays);
		logger.info(">>>> hours : {}", chronoHours);
		logger.info(">>>> minutes : {}", chronoMinutes);
	}
	
	@Test
	public void testAge() {
		
		ZonedDateTime now = ZonedDateTime.now();
		logger.info("now       : {}", now);

		//ZonedDateTime orgBirthday = now.minusYears(10);
		ZonedDateTime orgBirthday = DateUtils.toZonedDateTime("2008-09-05", "yyyy-MM-dd");
		ZonedDateTime birthday = orgBirthday;
		int age = DateUtils.getAge(birthday, now);
		
		logger.info("birthday : {}", birthday);
		logger.info("age      : {}", age);
		
		birthday = orgBirthday.minusDays(1);
		age = DateUtils.getAge(birthday, now);
		
		logger.info("birthday : {}", birthday);
		logger.info("age      : {}", age);
		
		birthday = orgBirthday.plusDays(1);
		age = DateUtils.getAge(birthday, now);
		
		logger.info("birthday : {}", birthday);
		logger.info("age      : {}", age);
		
		age = DateUtils.getAge(now, birthday);
		logger.info("age      : {}", age);
	}
	
	@Test
	public void testTimeZone() {
		LocalTime time = LocalTime.ofSecondOfDay(480 * 60);
		logger.info(">>>>>>> time : {}", time.toString());
		
		ZonedDateTime date = ZonedDateTime.now(ZoneOffset.ofHours(time.getHour()));
//		ZonedDateTime date = ZonedDateTime.now(ZoneId.of("UTC"));
//		logger.info(">>>>>>> date zone : {}", date.witChZoneSameInstant(ZoneOffset.ofHours(time.getHour())));
		logger.info(">>>>>>> date zone : {}", date);
	}
	
	@Test
	public void testDiffHours() {
		ZonedDateTime date1 = ZonedDateTime.of(2018, 10, 2, 16, 30, 0, 0, ZoneId.systemDefault());
		ZonedDateTime date2 = ZonedDateTime.now();
		
		long diff = ChronoUnit.HOURS.between(date2, date1);
		
		logger.info(">>> diffrence hours : {}", diff);
	}
	
	@Test
	public void testHonoluluTime() {
//		출발시간: 2018-11-09 18:50
//		도착시간: 2018-11-09 07:00
		ZonedDateTime date1 = ZonedDateTime.of(2018, 11, 9, 18, 50, 0, 0, ZoneId.of("+09:00"));
		ZonedDateTime date2 = ZonedDateTime.of(2018, 11, 9, 7, 0, 0, 0, DateUtils.getZoneIdOfMinutes(-600));
		logger.info(">>> zoneId : {}", DateUtils.getZoneIdOfMinutes(-600));
		logger.info(">>> date1 : {}, date2 : {}", date1, date2);
		logger.info(">>> diffent seconds : {}", ChronoUnit.SECONDS.between(date1, date2));
	}
	
	@Test
	public void testToString() {
		Tuple<String, ZonedDateTime> now = Tuple.of("NOW", ZonedDateTime.now());
		logger.info(">>> now : {}", CmmUtils.toString(now));
	}
	
	public static ZonedDateTime getToday() {
		return ZonedDateTime.now(ZoneId.systemDefault());
	}
	
	public static ZonedDateTime getDateTime(String dateStr) {
		DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder()
				.appendOptional(SOAP_DATETIME_FORMATTER)
				.appendOptional(SOAP_DATETIME_FORMATTER2)
				.appendOptional(SOAP_DATETIME_FORMATTER3)
				.appendOptional(SOAP_DATE_FORMATTER)
				.appendOptional(SOAP_DATE_FORMATTER2);
		
		ZoneId zone = ZoneId.of("GMT");
		ZonedDateTime date = ZonedDateTime.parse(dateStr, builder.toFormatter().withZone(zone));
//		ZonedDateTime date = ZonedDateTime.of(local, zone).withZoneSameInstant(ZoneId.of("Asia/Seoul"));
		return date.withZoneSameInstant(ZoneId.systemDefault());
	}
	
	 @Test
	 public void testDayOfWeek() {
		 ZonedDateTime now = getToday();
		 
		 logger.info("day {}", now.getDayOfWeek().getValue());
		 
		 for(int i=0; i<7; ++i) {
			 ZonedDateTime  val = now.plusDays(i);
			 logger.info("[{}] day {}, {}", i, val.getDayOfWeek().getValue(), val.getDayOfWeek());
		 }
		 
		 LocalDate nowDate = now.toLocalDate();
		 LocalTime nowTime = now.toLocalTime();
		 
		 logger.info("nowDate[{}] nowTime[{}]", nowDate, nowTime);
		 
		 LocalDate localDate = LocalDate.parse("2019-01-01");
		 logger.info("compare: " + nowDate + " vs. " + localDate + " is " + nowDate.compareTo(localDate));
		 
		 
	 }
}
