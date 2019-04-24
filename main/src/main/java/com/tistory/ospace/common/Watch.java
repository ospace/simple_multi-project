package com.tistory.ospace.common;

import java.util.function.Supplier;

import org.slf4j.Logger;

public class Watch {
	long begin_time = 0;
	long end_time = 0;
	
	public static <P> void eval(Logger logger, String name, Runnable runnable) {
		Watch runtime = Watch.begin();
		runnable.run();
		runtime.stop();
		logger.debug("{} : runtime[{} msec]", name, runtime.get());
	}
	
	public static <R> R eval(Logger logger, String name, Supplier<R> supplier) {
		Watch runtime = Watch.begin();
		R ret = supplier.get();
		runtime.stop();
		logger.debug("{} : runtime[{} msec]", name, runtime.get());
		return ret;
	}
	
	public static Watch begin() {
		Watch ret = new Watch();
		ret.start();
		return ret;
	}
	
	public void start() {
		begin_time = System.currentTimeMillis();
		end_time = 0;
	}
	
	public void stop() {
		end_time = System.currentTimeMillis();
	}
	
	public long lap() {
		return System.currentTimeMillis() - begin_time;
	}
	
	public long get() {
		return end_time - begin_time;
	}
}
