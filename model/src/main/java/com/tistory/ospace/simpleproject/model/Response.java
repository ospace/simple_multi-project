package com.tistory.ospace.simpleproject.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Response<RS> {
	private Integer status;
	private String  message;
	private String  errorId;
	private Object  request;
	private RS      response;
	
	public static <RS> Response<RS> ok(RS response) {
		Response<RS> ret = new Response<RS>(response);
		ret.setStatus(0);
		return ret;
	}
	
	public static <RS> Response<RS> ok(Object request, RS response) {
		Response<RS> ret = new Response<RS>(response);
		ret.setRequest(request);
		ret.setStatus(0);
		return ret;
	}
	
	public static <RS> Response<RS> fail(int status) {
		return fail(status, null);
	}
	
	public static <RS> Response<RS> fail(int status, String message) {
		Response<RS> ret = new Response<>(null);
		ret.setStatus(status);
		ret.setMessage(message);
		return ret;
	}
	
	public static <RS> Response<RS> fail(int status, String message, String errorId) {
		Response<RS> ret = new Response<>(null);
		ret.setStatus(status);
		ret.setMessage(message);
		ret.setErrorId(errorId);
		return ret;
	}
	
	public static <RS> Response<RS> fail(int status, String message, String errorId, Object request) {
		Response<RS> ret = fail(status, message, errorId);
		ret.setRequest(request);
		return ret;
	}
	
	public Response() {}
	
	public Object getRequest() {
		return request;
	}

	public void setRequest(Object request) {
		this.request = request;
	}

	public Response(RS response) {
		this.response = response;
	}
	
	public Integer getStatus() {
		return status;
	}
	
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

	public RS getResponse() {
		return response;
	}
	
	public String getErrorId() {
		return errorId;
	}

	public void setErrorId(String errorId) {
		this.errorId = errorId;
	}
	
	public void setResponse(RS response) {
		this.response = response;
	}
	
//	public String toString() {
//		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
//	}
}
