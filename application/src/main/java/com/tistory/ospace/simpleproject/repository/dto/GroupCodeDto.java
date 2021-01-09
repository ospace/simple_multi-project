package com.tistory.ospace.simpleproject.repository.dto;

import java.time.LocalDateTime;

import org.apache.ibatis.type.Alias;

import com.tistory.ospace.common.core.BaseObject;

@Alias("GroupCode")
public class GroupCodeDto extends BaseObject {
	private String        code;
	private String        name;
	private LocalDateTime createDate;
	private LocalDateTime modifyDate;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public LocalDateTime getCreateDate() {
		return createDate;
	}
	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}
	public LocalDateTime getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(LocalDateTime modifyDate) {
		this.modifyDate = modifyDate;
	}
}
