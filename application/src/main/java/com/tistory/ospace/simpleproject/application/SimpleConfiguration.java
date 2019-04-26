package com.tistory.ospace.simpleproject.application;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * 설정 프로퍼티 파일들을 읽어옴
 * 사용상 주의사항:
 *  - 처음 초기화시 인스탄스를 획득해서 가져오지 말것
 *  - 사용이 필요한 경우 인스탄스를 획득해서 값을 획득
 *  - 컴포넌트에서 사용하는 경우 inject 해서 사용
 */
@Primary
@Component
public class SimpleConfiguration {
	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleConfiguration.class);
	
	private static SimpleConfiguration instance = null;
	
	@Value("${spring.profiles.active:dev}")
	private String profile;
	
	@Value("${spring.application.name:ospace}")
	private String name;
	
	private String activePropertiesFile = "properties/application-%s.properties";
	private String defaultFile = "properties/default.properties";
	
	Properties defaultProp = new Properties();
	Properties gwProp = new Properties();
			
	private final ObjectMapper mapper = new ObjectMapper()
			.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
			.enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT)
			.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
			.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
			.setSerializationInclusion(Include.NON_NULL);
	
	@PostConstruct
	private void init() {
		LOGGER.info("===== active profile : {} =====", profile);
		
	/*	InputStream inStream = null;
		try {
			String propertiesFile = String.format(activePropertiesFile, profile);
		
			inStream = getClass().getClassLoader().getResourceAsStream(propertiesFile);
			gwProp.load(inStream);
			
			LOGGER.info("properties[{}] loaded", propertiesFile);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (null != inStream) {
				try {
					inStream.close();
				} catch (IOException e) {}
			}
		}
		
		try {
			inStream = getClass().getClassLoader().getResourceAsStream(defaultFile);
			defaultProp.load(inStream);
		} catch (IOException e) {
			LOGGER.warn("default config properties not exist : {}", defaultFile);
		} finally {
			if (null != inStream) {
				try {
					inStream.close();
				} catch (IOException e) {}
			}
		}*/
		
		SimpleConfiguration.instance = this;
	}
	
	/*
	 * 가급적 사용을 지향하고 빈 인젝션하여 사용을 권장함. 아래는 불가피하게 사용하는 일반 클래스에서 사용하기 위한 목적.
	 * 필요시 일시적으로 지역 참조하여 값을 읽고 사용을 권장.
	 * ex) String foo = LccGWConfiguration.instance().getPropertyString("foo");
	 */
	public static SimpleConfiguration instance() {
		return SimpleConfiguration.instance;
	}
	
	public String getProfile() {
		return profile;
	}
	
	public String getName() {
		return name;
	}
	
    /**
     * Returns a value for the configuration key.
     * @param key key.
     * @return value stored under given key.
     */
	public String getString(String key) {
		String val = gwProp.getProperty(key);
		
		if(null == val) {
			val = defaultProp.getProperty(key);
		}
		
		return val;
	}
	
	public String getString(String key, String defaultValue) {
		String value = getString(key);
		return null == value ? defaultValue : value;
	}
	
	public String[] getStringArray(String key) {
		String val = getString(key);
		return StringUtils.isEmpty(val) ? new String[] {} : split(val);
	}
	
	public Integer getInteger(String key) {
		String value = getString(key);
		try {
			return null == value ? null : Integer.parseInt(value);
		} catch(NumberFormatException e) {
			return null;
		}
	}

	public Integer getInteger(String key, Integer defaultValue) {
		Integer res = getInteger(key);
		return null == res ? defaultValue : res;
	}

	public Boolean getBoolean(String key) {
		String value = getString(key);
		return null == value ? null : "true".equalsIgnoreCase(value); 
	}

	public Boolean getBoolean(String key, Boolean defaultValue) {
		Boolean res = getBoolean(key);
		return null == res ? defaultValue : res;
	}
	
	/*
	 * 특정 키가 포함된 키값들을 가져옴. 여러개 배열형태 값은 콤마구분자로 합쳐서 반환됨.
	 * 특정 키가 포함되는 키들은 prefix를 제외한 문자열을 키로 사용함.
	 * 예) 설정된 프로퍼티가 다음과 같다고 하면
	 *   datasource.url=jdbc:mysql
	 *   datasource.username=user
	 * prefix를 "datasource"로 지정해서 호출할 경우 아래의 키가 찾게 된다.
	 *   datasource.url
	 *   datasource.username
	 * 여기서 앞의 공통된 datasource를 제외한 아래 문자열을 키로 해서 값을 반환된다.
	 *  url
	 *  username  
	 * 설정값이 콤마(,)로 구분된 경우 String[] 형태로 저장되며, 빈값으로 된 경우 null로 저장된다.
	 */
	public Map<String,Object> getMap(String prefix) {
		Map<String,Object> ret = new HashMap<>();
		
		Properties[] props = {defaultProp, gwProp};
		for(Properties prop : props) {
			for(Entry<Object, Object> it : prop.entrySet()) {
				String key = (String)it.getKey();
				if (!key.startsWith(prefix)) continue;
				String val = (String)it.getValue();
				String[] vals = split(val);
				if(null != vals &&  1 < vals.length) {
					ret.put(key.substring(prefix.length()+1), vals);
				} else {
					ret.put(key.substring(prefix.length()+1), StringUtils.isEmpty(val)?null:val);
				}
			}
		}
		
		return ret;
	}
	
	/*
	 * 설정에 포함된 키값 맵데이터를 클래스로 변환해서 반환
	 */
	public <R> R getObject(String prefix, Class<R> clazz) {
		Map<String, Object> res = getMap(prefix);
		return mapper.convertValue(res, clazz);
	}
	
	private String[] split(String str) {
		if(StringUtils.isEmpty(str)) return null;
		String[] ret = str.split("(?<!\\\\),", -1);
		
		for(int i=0; i<ret.length; ++i) {
			ret[i] = ret[i].replaceAll("\\\\,", ",");
		}
		
		return ret;
	}
}
