package com.tistory.ospace.simpleproject.util;

import java.util.Map;

import com.tistory.ospace.common.util.DataUtils;

public enum Gender {
	M("Man", "남성"), W("Woman", "여성");
	
	public final String value;
	public final String name;
	
	private Gender(String value, String name) {
		this.value = value;
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public static Map<String,String> toMap() {
		return DataUtils.map(values(), key->key.name(), val->val.name);
	}
}
