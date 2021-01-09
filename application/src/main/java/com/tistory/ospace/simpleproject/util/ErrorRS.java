package com.tistory.ospace.simpleproject.util;

import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.tistory.ospace.common.util.DateUtils;

@JsonRootName("error")
@JsonInclude(Include.NON_NULL)
public class ErrorRS {
	private Integer status;
	private String path;
	private String error;
	private String message;
	private String timestamp;
	
	public static ErrorRS of(Integer status) {
	    return of(status, null, null, null);
	}
	
	public static ErrorRS of(Integer status, String message) {
        return of(status, null, null, message);
    }
	
	public static ErrorRS of(Integer status, String path, String message) {
        return of(status, null, path, message);
    }
	
	public static ErrorRS of(Integer status, String error, String path, String message) {
		ErrorRS ret = new ErrorRS();
		
		ret.setStatus(status);
		ret.setPath(path);
		ret.setError(error);
		ret.setMessage(message);
		ret.setTimestamp(DateUtils.now(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
		
		return ret;
	}
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}

	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
}
