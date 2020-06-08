package com.tistory.ospace.simpleproject.repository.dto;

import org.apache.ibatis.type.Alias;

import com.tistory.ospace.core.data.BaseDto;

@Alias("Code")
public class CodeDto extends BaseDto {
	private String        code;
	private String        groupCode;
	private String        name;
	private Integer       order;

	public static CodeDto ofGroup(String groupCode, Integer order) {
		CodeDto ret = new CodeDto();
		ret.setGroupCode(groupCode);
		ret.setOrder(order);
		return ret;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
}
