package jdk;


import java.text.MessageFormat;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestString {
	private static final Logger logger = LoggerFactory.getLogger(TestString.class);
	
	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void testStringSplit() throws Exception {
		String s = "a,b,c,e,f";
		
		String[] vals = s.split(",");
		
		logger.info("Split {} to {}", vals, String.join(",",  vals));
		
		logger.info("result -1 : {}", String.join("_", s.split(",", -1)));
		logger.info("result 0 : {}", String.join("_", s.split(",", 0)));
		logger.info("result 1 : {}", String.join("_", s.split(",", 1)));
		logger.info("result 2 : {}", String.join("_", s.split(",", 2)));
	}
	
	@Test
	public void testMessageFormat() {
		String s1 = MessageFormat.format("{0} {1}", "A", "B", "C");
		String s2 = MessageFormat.format("{1} {2}", "A", "B", "C");
		
		logger.info("s1 : {}", s1);
		logger.info("s2 : {}", s2);
		
	}
	
	@Test
	public void testStringAndChar() {
		String s = "ABC" + (char)((int)'C'+1);
		
		logger.info("s : {}", s);
	}
	
	@Test
	public void testStringRegSplit() {
		String s = "a,b\\,c,d\\,e";
		
		String[] vals = s.split("(?<!\\\\),");
		
		//for(String it: vals) it.replace("\\,", "");
		for(int i=0; i<vals.length; ++i) {
			vals[i] = vals[i].replaceAll("\\\\,", ",");
		}
		
		logger.info("original : {}", s);
		logger.info("Split {} to {}", vals, String.join("|",  vals));

	}
}
