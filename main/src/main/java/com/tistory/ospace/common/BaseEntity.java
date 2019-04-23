package com.tistory.ospace.common;

//Cloneable
public class BaseEntity implements Cloneable {
	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			throw new BaseException(e);
		}
	}
	
	@Override
	public String toString() {
		return CmmUtils.toJsonString(this);
	}
}
