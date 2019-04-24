package jdk;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tistory.ospace.common.CmmUtils;


@JsonIgnoreProperties({ "val"})
@JsonPropertyOrder({"num", "str"})
@JsonInclude(JsonInclude.Include.NON_NULL)
class Bar {
	private String str;
	private int    num;
	private String val;
	private List<Integer> vals;
	// pair로 싸지 말고 상위에 풀어사용
	//@JsonUnwrapped
	//private Pair<Integer, String> pair;
	
	public Bar() {
	}
	public String getStr() {
		return str;
	}
	public void setStr(String str) {
		this.str = str;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getVal() {
		return val;
	}
	public void setVal(String val) {
		this.val = val;
	}
	public void setVals(List<Integer> vals) {
		this.vals = vals;
	}
	public List<Integer> getVals() {
		return this.vals;
	}
/*	public Pair<Integer, String> getPair() {
		return pair;
	}
	public void setPair(Pair<Integer, String> pair) {
		this.pair = pair;
	}*/
	@Override
	public String toString() {
		return CmmUtils.toString(this);
	}
}

public class TestJson {
	private static final Logger logger = LoggerFactory.getLogger(TestJson.class);
	
	@Test
	public void testJsonToObject () throws JsonParseException, JsonMappingException, IOException {
		String jsonStr = "{\"str\":\"foo\", \"num\":10, \"vals\":\"\"}";
		ObjectMapper objectMapper = new ObjectMapper();
		//objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		//objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);
		objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
		
		Bar bar = objectMapper.readValue(jsonStr, Bar.class);
		logger.info("bar : {}", bar);
		
		String jsonStr1 = "{\"str\":\"foo\"}";
		Bar bar1 = objectMapper.readValue(jsonStr1, Bar.class);
		logger.info("bar : {}", bar1);
	}
	
	@Test
	public void testObjectToJson() {
		Bar bar = new Bar();
		bar.setNum(10);
		//bar.setStr("bar");
		//bar.setVal("bar");
		//bar.setPair(Pair.of(1, "one"));
		
		ObjectMapper objectMapper = new ObjectMapper().setSerializationInclusion(Include.NON_NULL);
		
		try {
			String res = objectMapper.writeValueAsString(bar);
			logger.info("result : {}", res);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	static class Foo {
		private Integer id;
		private String  name;
		private Bar     bar;
		
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public boolean hasName() {
			return null != name;
		}
		public Bar getBar() {
			return bar;
		}
		public void setBar(Bar bar) {
			this.bar = bar;
		}
		public String toString() {
			return "id["+id+"] name["+name+"]";
		}
	}
	
	@Test
	public void testMapMapper() {
		Map<String,String> data = new HashMap<>();
		
		data.put("id", "1");
		data.put("name", "foo");
		
		ObjectMapper mapper = new ObjectMapper();
		
		Foo foo = mapper.convertValue(data, Foo.class);
		
		logger.info("foo : {}", foo);
	}	
}
