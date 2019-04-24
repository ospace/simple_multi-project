package jdk;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestCompletableFuture {
	private static final Logger logger = LoggerFactory.getLogger(TestGeneric.class);
	
	ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*2);
	
	@Before
	public void setUp() throws Exception {
	}
	

	private CompletableFuture<String> calcAsync1() throws InterruptedException {
		CompletableFuture<String> f = new CompletableFuture<>();
		
		executorService.submit(()->{
		//Executors.newCachedThreadPool().submit(()->{
			Thread.sleep(500);
			f.complete("Hello0");
			//f.cancel(false);
			return null;
		});
		return f;
	}
	
	private Future<String> calcAsync2() {
		return executorService.submit(()->{
			Thread.sleep(1000);
			return "Hello2";
		});
	}
	
	private CompletableFuture<String> calcAsync3(){
		CompletableFuture<String> future = CompletableFuture.supplyAsync(()->{
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return "Hello3";
		}, executorService);
		return future;
	}
	
	private List<CompletableFuture<String>> buildFutures() throws InterruptedException {
		List<CompletableFuture<String>> futures = new ArrayList<>();
		futures.add(calcAsync3());
		futures.add(calcAsync1());
		
		return futures;
	}
	
	@Test
	public void testSimple1() throws Exception {
		Future<String> completableFuture3 = calcAsync3();
		String result3 = completableFuture3.get(); //blocking
		logger.debug("result3["+result3+"]");
		assertEquals("Hello", result3);
		
		Future<String> completableFuture2 = calcAsync2();
		String result2 = completableFuture2.get();
		logger.debug("result2["+result2+"]");
		assertEquals("Hello", result2);
		
		Future<String> completableFuture = calcAsync1();
		String result = completableFuture.get(); //blocking
		logger.debug("result["+result+"]");
		assertEquals("Hello", result);
	}
	
	@Test
	public void testSimple2() throws Exception {
		List<CompletableFuture<String>> futures = buildFutures();
		
		futures.stream().map(CompletableFuture::join);

		//logger.info("done: {}", String.join(",", futures));
		//logger.info("done: {}", futures.stream().collect(Collectors.joining(",")));
		
		for(CompletableFuture<String> it: futures) {
			String result = it.get();
			logger.debug("result{}[{}]", futures.indexOf(it), result);
			assertEquals("Hello", result);
		}
	}
	
	@Test
	public void testSimple3() throws Exception {
		List<CompletableFuture<String>> futures = buildFutures();
		
		CompletableFuture<?> arrFuture[] = futures.toArray(new CompletableFuture[futures.size()]);
		CompletableFuture<Void> all = CompletableFuture.allOf(arrFuture);
		all.thenRun(()->{
			try {
				for(CompletableFuture<String> it: futures) {
					String result = it.get();
					logger.debug("result{}[{}]", futures.indexOf(it), result);
					assertEquals("Hello", result);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			logger.info("done: {}", Thread.currentThread().getId());
		}).exceptionally(e -> {
			logger.error("Error! " + e.getMessage());
            return null;
        }).get();
	}
	
	@Test
	public void testSimple4() throws Exception {
		logger.info("testSimple4");
		List<CompletableFuture<String>> futures = buildFutures();
		
		futures.forEach(it->{
			it.thenApply(res->{logger.debug("thenApply {}", res); return res;});
			  //.thenAccept(res->logger.debug("thenAccept {}", res));
		});
		
		CompletableFuture<?> arrFuture[] = futures.toArray(new CompletableFuture[futures.size()]);

		CompletableFuture<?> all = CompletableFuture.allOf(arrFuture).thenApplyAsync(res->{
			logger.debug("allOf thenApplyAsync called");
			//futures.stream().map(f->f.join()).collect(Collectors.toList());
			return futures;
		}).thenApply(msg->{
			msg.forEach(it->{
				try {
					logger.debug("allOf thenApply {} {}", it, it.get());
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			});
			return futures;
		});
		
		all.get();
		
		List<String> results = new ArrayList<>();
		futures.forEach(it->{try {
			results.add(it.get());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}});
		
		logger.debug("done {}", String.join(",", results));
		
		for(CompletableFuture<String> it: futures) {
			String result = it.get();
			logger.debug("result{}[{}]", futures.indexOf(it), result);
			//assertEquals("Hello", result);
		}
	}
	
}
