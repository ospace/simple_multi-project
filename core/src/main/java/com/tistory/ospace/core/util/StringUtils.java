package com.tistory.ospace.core.util;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
	private static final String STR_SEP = "/";
	private static final Random RANDOM = new Random();
	public static final String DEFAULT_KEY_SOURCE =
	        "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"; 
	
	public static boolean isEmpty(String val) {
		return null == val || val.isEmpty();
	}
	
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}
	
	public static String isEmpty(String val, String init) {
		return isEmpty(val) ? init : val;
	}
	
	public static <R> R isEmpty(String val, R init, Function<String, R> action) {
		return isEmpty(val) ? init : action.apply(val);
	}
	
	public static String isNull(String val, String init) {
		return null == val ? init : val;
	}
	
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
	
	/**
	 * 특수문자 제거
	 * @param str
	 * @return
	 */
	public static String specialCharacterRemove(String str) {
		String match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z0-9\\s-,]";
		str = str.replaceAll(match, "");
		return str;
	}
	
	/**
	 * 정규식 체크
	 * @param pattern
	 * @param str
	 * @return
	 */
	public static boolean isRegex(String pattern, String str) {
		if(str == null) return false;
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(str);
		return m.find();
		
	}
	
	/**
	 * 숫자 체크
	 * @param pattern
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str) {
		if(str == null) return false;
		return isRegex("^[0-9]+$", str);
	}
    
	public static String substring(String str, int start, int end) {
		if(isEmpty(str)) return str;
		return str.substring(start, end);
	}

	public static String trim(String str) {
		return null == str ? null : str.trim();
	}
	
	 /**
     * 랜덤키 생성 keySource 이용함 
     * @param size
     * @param keySource
     * @return
     */
    public static String generateKey(int size, String keySource) {
        char [] ret = new char[size];
        for(int i=0; i<size; ++i) {
            ret[i] = keySource.charAt(RANDOM.nextInt(size));
        }
        return new String(ret);
    }
    
    /**
     * 랜덤키 생성 DEFAULT_KEY_SOURCE 이용함 
     * @param size
     * @return
     */
    public static String generateKey(int size) {
        return generateKey(size, DEFAULT_KEY_SOURCE);
    }
    
    public static String newId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}