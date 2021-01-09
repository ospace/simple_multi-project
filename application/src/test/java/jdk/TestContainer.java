package jdk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.map.MultiValueMap;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.tistory.ospace.common.util.CmmUtils;


public class TestContainer {
	private static final Logger logger = LoggerFactory.getLogger(TestContainer.class);
	
	@Test
	public void testIterateMap() {
		Map<String, String> foo = new HashMap<>();
		foo.put("mon", "월");
		foo.put("sun", "일");
		
/*		foo.forEach((k,v)->{
			if("sun".equals(k)) {
				foo.put("sat", "토");
			}
			logger.info("Map: {}[{}]", k, v);
		});*/
		
		String day = null;
		
		foo.put("fri", day = "금");
		logger.info("day: {}", day);
	}
	
	@Test
	public void testMultiMap() {
		org.apache.commons.collections.MultiMap m = new MultiValueMap();
		m.put("1", "one");
		m.put("1", "One");
		m.put("1", "oNe");
		m.put("1", "onE");
		m.put(1, "ONE");
		
		Object v = m.get("1");
		logger.info("type[{}] v[{}]", v.getClass().getName(), v);
		
		Multimap<String, String> m2 = ArrayListMultimap.create();
		m2.put("1", "one");
		m2.put("1", "One");
		m2.put("1", "oNe");
		m2.put("1", "onE");
		Collection<String> v2 = m2.get("1");
		logger.info("type[{}] v2[{}]", v2.getClass().getName(), v2);
	}
	
	static class Foo {
		public final int id;
		public final String name;
		public final List<Integer> count;
		public Foo (int id, String name, List<Integer> count) {
			this.id = id; this.name = name; this.count = count;
		}
		public String toString() { return CmmUtils.toJsonString(this); }
	}
	
	static List<Integer> generateInt(int size) {
		Set<Integer> result = new HashSet<>();
		for(int i=0; i<size; ++i) result.add((int)(Math.random()*10+0));
		return new ArrayList<>(result);
	}
	
	static List<Foo> makeFoos(int size) {
		List<Foo> ret = new ArrayList<>();
		for(int i=0; i<size; ++i) {
			
			//ret.add(new Foo(i, 0==i%2?"even":"odd", i%10));
			ret.add(new Foo(i, randWeek(), generateInt(i%10)));
		}
		return ret;
	}
	
	static final String[] week = {"-", "sunday","monday","tuesday","wednesday","thursday","friday","saturday"};
	
	static String randWeek() {
		int n = (int) (Math.random()*(week.length-1)+0);
		return week[n];
	}

	//Ref: https://github.com/boonproject/boon/wiki/Boon-Data-Repo-Indexed-Collections-and-ETL-for-Java-Beans,-JSON,-Maps
	/*<!-- https://mvnrepository.com/artifact/io.fastjson/boon -->
		<dependency>
		    <groupId>io.fastjson</groupId>
		    <artifactId>boon</artifactId>
		    <version>0.34</version>
		</dependency>*/
/*	@Test
	public void testBoonRepo() {
		long begin_time = System.currentTimeMillis();
		Repo<Integer, Foo> repo = Repos.builder()
				.primaryKey("id")
				.searchIndex("name")
				.searchIndex("count")
				.build(int.class, Foo.class);
		
		repo.init(makeFoos(10000));
		//repo.addAll(makeFoos(10000));
		
		List<Foo> res = null;
		//Query: http://www.querydsl.com/
		res = repo.query(org.boon.criteria.ObjectFilter.in("name", "-", "monday"), eq("count", 8));
		long end_time = System.currentTimeMillis();
		logger.info("runtime: {} msec, {} ea", (end_time-begin_time), res.size());
		
		//logger.info("1st res : {}", res);
	}*/
	
	@Test
	public void testHashSet() {
		HashSet<String> data = new HashSet<>();
		
		data.add("AA");
		data.add("BB");
		
		boolean res = data.containsAll(Arrays.asList("AA","CC"));
		
		logger.info("containsAll : {}", res);
	}
	
	@Test
	public void testList() {
		List<Integer> nums = new LinkedList<>();
		
		nums.add(3);
		nums.add(1);
		nums.add(2);
		
		nums.sort((l,r)->l.compareTo(r));
		
		logger.info("sorted : {}", nums);
	
		Set<Integer> res = new HashSet<>(nums);
		
		logger.info("HashSet : {}", res);
		
		
		//String[] strs2 = null;
		//List<String> strs3 = Arrays.asList(strs2); 
		//logger.info("null array : {}", String.join(",", strs2));
		
	}
	
	@Test
	public void testListWithSize() {
		List<Integer> nums = new ArrayList<>(3);
		
		nums.set(0, 3);
		nums.set(1, 2);
		
		logger.info("nums : size[{}]", nums.size());
	}
}
