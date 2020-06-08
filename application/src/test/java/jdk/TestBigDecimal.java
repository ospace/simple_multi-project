package jdk;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tistory.ospace.core.util.CmmUtils;

public class TestBigDecimal {
	private static final Logger logger = LoggerFactory.getLogger(TestBigDecimal.class);
	
	@Test
	public void testAddDecimal( ) {
		BigDecimal val = new BigDecimal(1);
		BigDecimal val10 = new BigDecimal(10);
		
		BigDecimal res = val.add(val10).add(val10);
		
		logger.info("add 10: {}", val.intValue());
		logger.info("add 10: {}", res.intValue());
		
		BigDecimal other = new BigDecimal(0);
		BigDecimal empty = other.add(null);
		
		logger.info("other[{}]", empty.intValue());
	}
	
	@Test
	public void testSortDecimal() {
		List<BigDecimal> nums = Arrays.asList(new BigDecimal(1), new BigDecimal(3), new BigDecimal(2));
		
		nums.sort((l,r)->l.compareTo(r));

		logger.info("sort : {}", nums);
		
		logger.info("sort : {}", nums.subList(0, 1));
	}
	
	@Test
	public void testNullDecimal() {
		BigDecimal zero = BigDecimal.ZERO;
		
//		BigDecimal empty = null;
		BigDecimal result = zero.add(null);
		Assert.assertTrue(result.equals(BigDecimal.ZERO));
	}
	
	@Test
	public void testFloor() {
		BigDecimal[] val = {
			 BigDecimal.valueOf(0.555)
			,BigDecimal.valueOf(1.555)
			,BigDecimal.valueOf(15.555)
			,BigDecimal.valueOf(155.555)
			,BigDecimal.valueOf(1555.555)
		};

		MathContext mc = new MathContext(2, RoundingMode.FLOOR);
		
		for(BigDecimal v : val) {
			logger.info("floor : {}", v.round(mc).floatValue());
		}
		
		int floor = (int) Math.pow(10, 2);
		for(BigDecimal v : val) {
			logger.info("floor2 : {}", v.intValue() / floor * floor); 
		}
		
		for(BigDecimal v : val) {
			logger.info("floor3 : {}", v.setScale(-2, BigDecimal.ROUND_DOWN).intValue()); 
		}
	}
	
	@Test
	public void testInit() {
		BigDecimal val = new BigDecimal("1.123");
		logger.info("val : {}", val);
		Assert.assertTrue(val.floatValue() == 1.123f);
	}
	
	private static BigDecimal HUNDRED = BigDecimal.valueOf(100.0);
	
	@Test
	public void testToString() {
		BigDecimal val = new BigDecimal("1.123");
		val = val.add(BigDecimal.valueOf(100000), MathContext.DECIMAL64);
		val = val.multiply(BigDecimal.valueOf(30)).divide(HUNDRED);
		BigDecimal val1 = BigDecimal.valueOf(val.setScale(-2, BigDecimal.ROUND_UP).longValue());
		BigDecimal val2 = CmmUtils.ceilHundred(val);
		logger.info("val[{}] val1[{}] val2[{}]", val, val1, val2);
	}
	
	@Test
	public void testAddZero() {
		BigDecimal zero = BigDecimal.ZERO;
		BigDecimal val = BigDecimal.valueOf(7);
		logger.info("added by {}, result {}", val, CmmUtils.add(zero, val));
	}
}
