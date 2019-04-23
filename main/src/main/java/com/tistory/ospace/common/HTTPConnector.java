package com.tistory.ospace.common;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//Ref: https://www.mkyong.com/java/how-to-send-http-request-getpost-in-java/
//Ref: http://www.baeldung.com/java-http-request

public class HTTPConnector {
	private static final Logger LOGGER = LoggerFactory.getLogger(HTTPConnector.class);
	
	private String             url;
	private Map<String,String> properties;
	private Map<String,String> params;
	
	public static HTTPConnector of(String url) {
		return new HTTPConnector(url);
		//.setProperty("User-Agent", USER_AGENT)
	}
	
	public HTTPConnector(String url) {
		this.url = url;
	}
	
	public HTTPConnector setProperty(String key, String value) {
		if(null == properties) {
			properties = new HashMap<>();
		}
		properties.put(key, value);
		
		return this;
	}
	
	public HTTPConnector setParam(String key, String value) {
		if(null == params) {
			params = new HashMap<>();
		}
		params.put(key, value);
		
		return this;
	}
	
	public String get() {
		String args = null == params ? null : DataUtils.reduce(params.keySet(), (r,it)->{
			if (0 < r.length()) r.append("&");
			r.append(it).append("=").append(params.get(it));
//				r.append(URLEncoder.encode(it, "UTF-8"))
//				 .append("=")
//				 .append(URLEncoder.encode(params.get(it), "UTF-8"));
		}, new StringBuffer()).toString();
		
		return send("GET", args);
	}
	
	public String post(String body) {
		return send("POST", body);
	}

	public String post(String body, String encoding) {
		return send("POST", body, encoding);
	}
	
	private String send(String method, String body) {
		return send(method, body, null);
	}
	
	private String send(String method, String body, String encoding) {
		HttpURLConnection conn = null;
		try {
			String urlStr = url;
			if("GET".equals(method) && !StringUtils.isEmpty(body)) {
				urlStr = urlStr.concat("?").concat(body);
				body = null;
			}
			
			URL url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			//conn.setRequestProperty("User-Agent", USER_AGENT);
			conn.setRequestMethod(method);
			conn.setDoOutput(true);
			//conn.setConnectTimeout(8000);
			//conn.setReadTimeout(3000);
			
			if(!DataUtils.isEmpty(properties)) {
				for(Entry<String,String> it : properties.entrySet()) {
					conn.setRequestProperty(it.getKey(), it.getValue());	
				}
			}
			
			if(StringUtils.isNotBlank(body)) {
				DataOutputStream out = new DataOutputStream(conn.getOutputStream());
				if(null == encoding || "".equals(encoding)) {
					out.writeBytes(body);
				} else {
					out.write(body.getBytes(encoding));
				}
				out.flush();
				out.close();
			}
			
			LOGGER.trace("http request  : urlStr[{}] body[{}]", urlStr, body);
		
			int resCode = conn.getResponseCode();
			
			LOGGER.trace("http response : resCode[{}]", resCode);
			
			if (200 == resCode) {
				String ret = getResponse(conn.getInputStream());
				LOGGER.trace("http response : data[{}]", ret);
				return ret;
			} else {
				throw new RuntimeException("["+resCode+"] "+getResponse(conn.getErrorStream()));
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if(null!=conn) conn.disconnect();
		}
	}
	
	private static String getResponse(InputStream inStrm) {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(inStrm, "UTF8"));
			StringBuffer ret = new StringBuffer();
			char[] buf = new char[2048];
			int len = 0;
			while(0<=(len=in.read(buf))) {
				ret.append(buf, 0, len);
			}
			in.close();

			return ret.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
