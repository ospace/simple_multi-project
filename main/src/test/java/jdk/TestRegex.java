package jdk;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestRegex {
	private static final Logger logger = LoggerFactory.getLogger(TestRegex.class);
	
	@Test
	public void testParseJourneyKey() {
		final String regex = "(\\w{2})~(\\d*)~.*~.*?~(\\w{3})~([\\w\\/ :]{16}?)~(\\w{3})~([\\w\\/ :]{16}?)~";
		final String string = "UO~3627~ ~~ICN~05/17/2018 22:35~HKG~05/18/2018 01:10~";
		final String subst = "\\1, \\2, \\3, \\4, \\5, \\6";

		final Pattern pattern = Pattern.compile(regex);
		final Matcher matcher = pattern.matcher(string);

		if(matcher.find()) {
			for(int i=0; i<=matcher.groupCount(); ++i) {
				logger.info("[{}] {}", i, matcher.group(i));
			}
		}
		// The substituted value will be contained in the result variable
		final String result = matcher.replaceAll(subst);

		logger.info("Substitution result: {}", result);
	}
	
	@Test
	public void testParseKey() {
		String msg = "1234{56}7890{123}456789{0123}45678901234{56}7890";
		
		Map<String,String> mapping = new HashMap<>();
		mapping.put("56", "abc");
		mapping.put("123", "de");
		
		Pattern pattern = Pattern.compile("(\\{\\w+?\\})");
		Matcher matcher = pattern.matcher(msg);
		
		StringBuffer sb = new StringBuffer();
		
		while(matcher.find()) {
			for(int i=1; i<=matcher.groupCount(); ++i) {
				String key = matcher.group(i);
				logger.info("[{}] {}", i, key);
				String val = mapping.get(key.substring(1, key.length()-1));
				if(null == val) continue;
				matcher.appendReplacement(sb, val);
			}
		}
		
		matcher.appendTail(sb);
		
		logger.info("result : {}", sb.toString());
	}
	
	@Test
	public void testTagNil() {
		Pattern pattern = Pattern.compile("^(?:.*:)?nil$");
		
		logger.info("case1 : {}", pattern.matcher("a:nil").find());
		logger.info("case2 : {}", pattern.matcher("nnil").find());
		logger.info("case3 : {}", pattern.matcher(":nil").find());
		logger.info("case4 : {}", pattern.matcher("nil").find());
		logger.info("case5 : {}", pattern.matcher("nil ").find());
		logger.info("case6 : {}", pattern.matcher(" nil").find());
	}
}
