package com.tistory.ospace.simpleproject.exception;

/*
 * 무결성 오류가 발생: 삭제, 수정시 다른 곳에서 참조 중인 경우 발생
 */
public class SimpleProjectDataIntegrityException extends SimpleProjectException {
	private static final long serialVersionUID = 3177686619890627943L;
	public static final int STATUS = 1003;
	
	public SimpleProjectDataIntegrityException(String msg) {
		super(STATUS, msg);
	}
	
	public SimpleProjectDataIntegrityException(String msg, Exception e) {
		super(STATUS, msg, e);
	}
}
