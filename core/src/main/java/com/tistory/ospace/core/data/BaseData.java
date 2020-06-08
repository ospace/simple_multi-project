package com.tistory.ospace.core.data;

import com.tistory.ospace.core.util.CmmUtils;

public class BaseData implements Cloneable {
	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public String toString() {
		return CmmUtils.toString(this);
	}
}
