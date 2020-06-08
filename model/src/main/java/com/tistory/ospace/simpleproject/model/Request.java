package com.tistory.ospace.simpleproject.model;

import javax.validation.Valid;

public class Request<RQ> {
	private String  signature;
	private String  timestamp;
	private String  version;
	private Integer timeout;
	@Valid
	private RQ      request;
	
	public String getSignature() {
		return signature;
	}
	
	public void setSignature(String signature) {
		this.signature = signature;
	}
	
	public String getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	public String getVersion() {
		return version;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}

	public Integer getTimeout() {
		return timeout;
	}
	
	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}
	
	public RQ getRequest() {
		return request;
	}
	
	public void setRequest(RQ request) {
		this.request = request;
	}
	
//	public String toString() {
//		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
//	}
}
