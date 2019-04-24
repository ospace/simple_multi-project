package jdk;

import java.util.Timer;
import java.util.TimerTask;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.junit.Assert;

public class TestTimer {
	private static final Logger logger = LoggerFactory.getLogger(TestTimer.class);
	
	static void sleep(int sec) {
		try {
			Thread.sleep(sec*1000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
	
	static int cnt = 0;
	
	@Test
	public void testTimer() {
		int delay = 1;
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				logger.info("fired");
				++cnt;
			}
		}, delay*1000);
		sleep(delay);
		logger.info("cnt[{}]", cnt);
		Assert.assertTrue(1 == cnt);
	}
	
	@Test
	public void testTimerMultiSchedule() {
		int delay = 1;
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				logger.info("fired");
				++cnt;
			}
		}, delay*1000);
		
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				logger.info("fired");
				++cnt;
			}
		}, delay*1000);
		
		sleep(delay);
		
		logger.info("cnt[{}]", cnt);
		
		Assert.assertTrue(2 == cnt);
	}
	
	@Test
	public void testTimerCancel() {
		int delay = 1;
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				logger.info("fired");
				++cnt;
			}
		}, delay*1000);
		
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				logger.info("fired");
				++cnt;
			}
		}, (delay+1)*1000);
		
		sleep(delay);
		timer.cancel();
		sleep(delay);
		
		logger.info("cnt[{}]", cnt);
		
		Assert.assertTrue(1 == cnt);
	}
}
