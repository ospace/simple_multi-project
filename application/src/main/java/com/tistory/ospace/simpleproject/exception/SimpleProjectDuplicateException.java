package com.tistory.ospace.simpleproject.exception;

/*
 * 중복에러로 기존에 데이터가 있는 경우 발생 
 */
public class SimpleProjectDuplicateException extends SimpleProjectException {
	private static final long serialVersionUID = 3243208000567822765L;
	public static final int STATUS = 1002;
	
	public SimpleProjectDuplicateException(String msg) {
		super(STATUS, msg);
	}
	
	public SimpleProjectDuplicateException(String msg, Exception e) {
		super(STATUS, msg, e);
	}
}
