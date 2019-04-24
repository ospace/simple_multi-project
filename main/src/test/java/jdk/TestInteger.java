package jdk;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestInteger {
	private static final Logger logger = LoggerFactory.getLogger(TestInteger.class);
	
	@Test
	public void testParseInteger() {
		
		Integer val = Integer.parseInt("abc");
		
		logger.info("val[{}]", val);
	}
}
