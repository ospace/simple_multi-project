package jdk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestObject {
	private static final Logger logger = LoggerFactory.getLogger(TestObject.class);
	
	public void initList(List<String> data) {
		data.add("a");
		data.add(null);
		data.add("b");	
	}
	
	
	@Test
	public void testListHashKey() {
		List<String> l1 = new ArrayList<>();
		initList(l1);
		
		List<String> l2 = new ArrayList<>();
		initList(l2);
		
		logger.info("l1 hashCode : {}", Objects.hash(l1));
		logger.info("l2 hashCode : {}", Objects.hash(l2));
		
		logger.info("l1 hashCode : {}", Objects.hash("a", l1));
		logger.info("l1 hashCode : {}", Objects.hash(null, l1));
		
		String[] a1 = {"a", null, "b"};
		String[] a2 = {"a", null, "b"};
		
		logger.info("a1 hashCode : {}", Objects.hash(Arrays.asList(a1)));
		logger.info("a2 hashCode : {}", Objects.hash(Arrays.asList(a2)));
	}
	

	@Test
	public void testEquals() {
		String v1 = "foo";
		Integer v2 = 123;
		
		Assert.assertTrue(v1.equals("foo"));
		Assert.assertFalse(v1.equals("FOO"));
		Assert.assertTrue(v2.equals(123));
		Assert.assertFalse(v2.equals(1234));
		Assert.assertFalse(v2.equals(12));
	}
}
