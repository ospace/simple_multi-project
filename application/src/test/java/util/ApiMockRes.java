package util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.JsonNode;
import com.tistory.ospace.core.util.CmmUtils;

public class ApiMockRes {
	private static final Pattern arrayPtn = Pattern.compile("(\\S+)\\[(\\d+)\\]");
	
	private MvcResult result;
	private JsonNode  currentNode;
	
	public ApiMockRes(MvcResult result) {
		this.result = result;
		try {
			this.currentNode = CmmUtils.toObject(result.getResponse().getContentAsString());
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("LccMockRes parse json failed", e);
		}
	}
	
	public ApiMockRes(MvcResult result, JsonNode currentNode) {
		this.result = result;
		this.currentNode = currentNode;
	}
	
	public String getSessionId() {
		return (String)result.getResponse().getHeaderValue("sessionId");
	}
	
	public ApiMockRes get(String query) {
		return new ApiMockRes(this.result, getJsonNode(query));
	}
	
	public String getText(String query) {
		JsonNode node = getJsonNode(query);
		return null == node ? null : node.textValue();
	}
	
	public Integer getInteger(String query) {
		JsonNode node = getJsonNode(query);
		return null == node ? null : node.intValue();
	}
	
	public List<ApiMockRes> getList(String fieldName) {
		List<ApiMockRes> rt = new ArrayList<>();
		for(JsonNode node : getJsonNode(fieldName)) {
			rt.add(new ApiMockRes(this.result, node));
		}
		return rt;
	}
	
	public String print() {
		return CmmUtils.toString(currentNode);
	}
	
	private JsonNode getJsonNode(String query) {
		String keywords[] = query.split("\\.");
		JsonNode current = currentNode;
		for(String it : keywords) {
			Matcher matcher = arrayPtn.matcher(it);
			if (null == matcher || !matcher.find()) {
				current = current.path(it);
			} else {
				int cnt = matcher.groupCount();
				if(2 != cnt) throw new RuntimeException("invalid arrary query : " + it);
				current = current.path(matcher.group(1)).path(Integer.parseInt(matcher.group(2)));
			}
		}
		
		return current;
	}
}
