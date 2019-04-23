package com.tistory.ospace.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.MathContext;
import java.nio.CharBuffer;
import java.text.MessageFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class CmmUtils {
	static final String STR_SEP = "/";
	
	public static final BigDecimal MINUS_ONE = new BigDecimal("-1");
	
	public static String[] split(String str) {
		if(null == str || str.isEmpty()) return null;
		return str.split(STR_SEP);
	}
	
	public static String join(String... strs) {
		return String.join(STR_SEP, strs);
	}
	
	public static String join(List<String> strs) {
		if(null == strs) return null;
		return String.join(STR_SEP, strs);
	}
	
	public static String join(Collection<String> strs) {
		if(null == strs) return null;
		return String.join(STR_SEP, strs);
	}
	
	public static <T> String toString(T obj) {
		return ToStringBuilder.reflectionToString(obj, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
	public static <T> String toJsonString(T obj) {
		return toJsonString(obj, false);
	}
	
	private static final ObjectMapper jsonSimpleObjectMapper = new ObjectMapper()
		.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
		.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
		.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
		.enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT)
		.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
		.registerModule(new JavaTimeModule())
		.setSerializationInclusion(Include.NON_NULL);
	public static <T> String toJsonString(T obj, boolean isPretty) {
		try {
			if (isPretty) {
				return jsonSimpleObjectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
			} else {
				return jsonSimpleObjectMapper.writeValueAsString(obj);
			}
	    } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
	        return e.getMessage();
	    }
	}
	
	/** 개인정보 마스킹 패턴(XML과 JSON 문자열에서 함께 사용) */
	private static String[] regExpPatterns = {
												"(Phone>|Phone\":\")\\d{9,16}", 
												"(mobile\":\")\\d{9,16}", 
												"(FirstName>|firstName\":\")[a-zA-Z ]+", 
												"(DOB>|birthday\":\")[\\d-]+", 
												"(PostalCode>|post\":\")\\d{4,16}", 
												"(EmailAddress>|email\":\")[\\w.%+-]+@[\\w.]+\\.[a-zA-Z]{2,6}", 
												"(AddressLine1>|addressLine1\":\"|address\":\")[\\w ,+\\-]+", 
												"(AddressLine2>|addressLine2\":\")[\\w ,+\\-]+", 
												"(AddressLine3>|addressLine3\":\")[\\w ,+\\-]+", 
												"(DocNumber>|docNumber\":\")[\\w]+"
											};
	//개인정보 마스킹 2-1(로그), 개인정보 마스킹 2-2(VO객체 - BookingVO, BookingContactVO 등 toString 을 @Override 함)
	public static String toMaskPersonalInfo(String str) {
		String temp = str; 
		for(String reg : regExpPatterns) {
			temp = temp.replaceAll(reg, "$1***");
		}
		
		return temp;
	}
	
//	private static final ObjectMapper jsonFieldObjectMapper = new ObjectMapper()
//		.setSerializationInclusion(Include.NON_NULL)
//		.setVisibility(PropertyAccessor.FIELD, Visibility.ANY)
//		.setVisibility(PropertyAccessor.GETTER, Visibility.NONE)
//		.setVisibility(PropertyAccessor.CREATOR, Visibility.NONE)
//		.registerModule(new JavaTimeModule());
//	public static <T> String toFieldJsonString(T obj) {
//		try {
//			return jsonFieldObjectMapper.writeValueAsString(obj);
//		} catch (com.fasterxml.jackson.core.JsonProcessingException e) {
//	        return e.getMessage();
//	    }
//	}
	
	public static JsonNode toJsonObject(String jsonStr) {
		try {
			return jsonSimpleObjectMapper.readTree(jsonStr);
		} catch (IOException e) {
			throw new BaseException("JsonObject", 99, e);
		}
	}
	
	public static <R> R toJsonObject(String jsonStr, Class<R> clazz) {
		if(StringUtils.isEmpty(jsonStr)) return null;
		
		try {
			return jsonSimpleObjectMapper.readValue(jsonStr, clazz);
		} catch (IOException e) {
			throw new BaseException("JsonObject", 99, e);
		}
	}
	
	public static <T> String toString(T obj, ToStringStyle style) {
		return ToStringBuilder.reflectionToString(obj, style);
	}
	
	public static <T> String getListSize(List<T> obj) {
		StringBuilder sb = new StringBuilder();
		getListSize(obj, sb);
		return sb.toString();
	}
	
	public static long diff(Date l, Date r) {
		return Math.abs(l.getTime() - r.getTime())/1000;
	}
	
	public static long diff(LocalDateTime l, LocalDateTime r) {
		return Duration.between(l, r).getSeconds();
	}
	
	public static long diff(Calendar l, Calendar r) {
		return Math.abs(l.getTimeInMillis()-r.getTimeInMillis())/1000;
	}
	
	@SuppressWarnings("unchecked")
	private static <T> void getListSize(List<T> obj, StringBuilder sb) {
		if(null == obj) return;
		sb.append("[size:").append(obj.size());
		if(!obj.isEmpty() && null != obj.get(0)) {
			String[] name = obj.get(0).getClass().getName().split("\\.");
			sb.append(",type:\"").append(name[name.length-1]).append("\"");
		}
		DataUtils.iterate(obj, it->{
			if (!(it instanceof List)) return;
			getListSize((List<Object>) it, sb);
		});
		sb.append("]");
	}
	
	public static Integer max(Integer l, Integer r) {
		if(null == l) return r;
		if(null == r) return l;
		return Math.max(l, r);
	}
	
	public static BigDecimal add(BigDecimal... vals) {
		return add(Arrays.asList(vals));
	}
	
	public static BigDecimal add(List<BigDecimal> vals) {
		if(null == DataUtils.findFirst(vals, it->null!=it)) return null;
		
		BigDecimal result = BigDecimal.ZERO;
		for(BigDecimal val : vals) {
			if(null == val) continue;
			result = result.add(val, MathContext.DECIMAL64);
		}
		return result;
	}
	
	public static BigDecimal floorHundred(BigDecimal val) {
		return null == val ? null : BigDecimal.valueOf(val.setScale(-2, BigDecimal.ROUND_DOWN).longValue());
	}
	
	public static BigDecimal ceilHundred(BigDecimal val) {
		return null == val ? null : BigDecimal.valueOf(val.setScale(-2, BigDecimal.ROUND_UP).longValue());
	}
	
	public static BigDecimal ceilDecimalPoint(BigDecimal val) {
		return null == val ? null : BigDecimal.valueOf(val.setScale(0, BigDecimal.ROUND_UP).longValue());
	}
	
	public static BigDecimal add(BigDecimal l, BigDecimal r) {
		return null == r ? l : (null == l ? r : l.add(r));
	}
	
	public static BigDecimal subtract(BigDecimal l, BigDecimal r) {
		return null == r ? l : (null == l ? BigDecimal.ZERO.subtract(r) : l.subtract(r));
	}
	
	public static BigDecimal multiply(BigDecimal l, BigDecimal r) {
		return null == r ? null : (null == l ? null : l.multiply(r));
	}
	
	
	public static BigDecimal addRate(BigDecimal l, BigDecimal r) {
		return add(l, calcRate(l,r));
	}
	
	private static BigDecimal HUNDRED = BigDecimal.valueOf(100.0);
	public static BigDecimal calcRate(BigDecimal val, BigDecimal rate) {
		return val.multiply(rate).divide(HUNDRED);
	}
	
	public static BigDecimal add(BigDecimal l, BigDecimal r, boolean isRate) {
		return isRate ? addRate(l,r) : add(l,r);
	}
	
	//초단위를 문자열로 변환
	//{0}: day, {1}: hour, {2}: min, {3}: sec
	//ex) "{0}d{1}h{2}m"
	public static String timeFmt(long sec, String fmt) {
		long min = sec/60;
		long hour = min/60;
		long day = hour/24;
		
		return MessageFormat.format(fmt, day, hour%24, min%60, sec%60);
	}
	
	public static Integer min(Integer l, Integer r) {
		if (null == l) return r;
		if (null == r) return l;
		return Math.min(l,r);
	}
	
	public static void writeFile(String filepath, String content) throws IOException {
		File file = new File(filepath);
		
		String path = file.getParentFile().toString();
		File dir = new File(path);
		
		if(!dir.exists() && !dir.mkdirs()) return;
		
		FileOutputStream os = null;
		OutputStreamWriter osw = null;
		try {
			os = new FileOutputStream(file);
			osw = new OutputStreamWriter(os, "UTF8");
			osw.write(content);
			osw.flush();
		} finally {
			if(null != osw) osw.close();
			if(null != os)  os.close();	
		}
	}
	
	public static String readResource(String filepath) throws IOException {
		return readResource(filepath, "UTF8");
	}
	
	public static String readResource(String filepath, String charset) throws IOException {
		InputStream fis = null;
		Reader reader = null;
		BufferedReader br = null;
		try {
			fis = CmmUtils.class.getClassLoader().getResourceAsStream(filepath);
			reader = new InputStreamReader(fis, charset);
			br = new BufferedReader(reader);
			StringBuilder sb = new StringBuilder();
		    CharBuffer cbuf = CharBuffer.allocate(1024);
	    	while(0 < br.read(cbuf)) {		    	
		    	cbuf.flip();
		    	sb.append(cbuf);
		    }
		    return sb.toString();
		} finally {
			if(null != br)     br.close();
			if(null != reader) reader.close();
			if(null != fis)    fis.close();
		}
	}
	
	public static <P> P[] toArray(List<P> data) {
		if(DataUtils.isEmpty(data)) return null;
		
		@SuppressWarnings("unchecked")
		P[] ret = (P[]) Array.newInstance(data.get(0).getClass(), data.size());
		data.toArray(ret);
		return ret;
	}

	public static String createFlow(StackTraceElement[] trace) {
		return String.join(" > ", DataUtils.map(trace,  StackTraceElement::getClassName, StackTraceElement::getMethodName, ".", "sun.reflect."));
	}
	
	public static <P> P[] clone(P[] data) {
	if(DataUtils.isEmpty(data)) return null;
	
		@SuppressWarnings("unchecked")
		P[] ret = (P[]) Array.newInstance(data[0].getClass(), data.length);
		for(int i=0; i>ret.length; ++i) ret[i] = clone(data[i]);
		return ret;
	}
		
	public static <P> List<P> clone(List<P> data) {
		if(DataUtils.isEmpty(data)) return null;
		return DataUtils.map(data, it->clone(it));
	}
	
	public static <P extends BaseEntity> List<P> cloneCmmVOList(List<P> data) {
		if(DataUtils.isEmpty(data)) return null;
		return DataUtils.map(data, it->clone(it));
	}
	
	@SuppressWarnings("unchecked")
	public static <P extends BaseEntity> P clone(P data) {
		if(null == data) return null;
		return (P)data.clone();
	}
	
	public static <P> P clone(P data) {
		return data;
	}
	
	/*
	 * 느린 방식의 복제
	 * Ref: http://javatechniques.com/blog/faster-deep-copies-of-java-objects/
	 */
	@SuppressWarnings("unchecked")
	public static <P extends Serializable> P cloneDeep(P data) throws IOException, ClassNotFoundException {
		FastByteArrayOutputStream outStream = new FastByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(outStream);
		out.writeObject(data);
		out.flush();
		out.close();
		
		ObjectInputStream in = null;
		try {
			in = new ObjectInputStream(outStream.getInputStream());
			return (P) in.readObject();
		} finally {
			if(null != in) in.close();
		}
	}
	
	public static int hashCode(Object[] data) {
		int result = 1;
		for(Object it : data) {
			result = 31 * result + (null==it?0:it.hashCode());
		}
		return result;
	}
	
	public static <P extends Comparable<P>> List<P> toSortedList(Set<P> data) {
		List<P> ret = new ArrayList<>(data);
		ret.sort((l,r)->l.compareTo(r));
		return ret;
	}
	
	public static String getMessage(MessageSource messageSource, String code, Object[] args) {
		return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
	}
	
	public static String getMessage(MessageSource messageSource, String code) {
		return getMessage(messageSource, code, null);
	}
	
	public static String defaultIfEmpty(String value, String defaultVal) {
		return StringUtils.isEmpty(value) ? defaultVal :value;
	}
	
	public static String leftPad(String value, int len, String padVal) {
		int padLen = null == value ? len : len - value.length();
		
		return len > 0 ? repeatString(padVal, padLen).concat(value) : value;
	}
	
	public static String repeatString(String value, int repeat) {
		String ret = "";
		for(int i=0; i<repeat; ++i) {
			ret = ret.concat(value);
		}
		return ret;
	}
	
	public static <T> BigDecimal sumBigDecimal(Collection<T> data, Function<T, BigDecimal> action) {
		return DataUtils.reduce(data, (n,i)->CmmUtils.add(n, action.apply(i)));
	}
	
	public static <T> Integer sumInteger(Collection<T> data, Function<T, Integer> action) {
		return DataUtils.reduce(data, (n,i)->{
			Integer val = action.apply(i);
			return null==n ? val : (null==val ? n : n+val);
		});
	}
	
	public static <T> boolean has(T[] data, T val) {
		if (DataUtils.isEmpty(data)) return false;
		
		for(T it : data) {
			if (it.equals(val)) return true;
		}
		
		return false;
	}
	
	public static <T> boolean has(Collection<T> data, T val) {
		if (DataUtils.isEmpty(data)) return false;
		return data.contains(val);
	}
	
	public static Integer parseInt(String str, Integer defaultVal) {
		if(StringUtils.isEmpty(str)) return defaultVal;
		
		Integer ret = null;
		
		try {
			ret = Integer.parseInt(str);
		} catch(NumberFormatException e) {
			ret = defaultVal;
		}
		
		return ret;
	}
}
