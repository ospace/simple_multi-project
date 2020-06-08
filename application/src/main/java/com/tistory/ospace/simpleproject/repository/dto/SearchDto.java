package com.tistory.ospace.simpleproject.repository.dto;

import org.apache.ibatis.type.Alias;

import com.tistory.ospace.core.data.BaseData;

@Alias("Search")
public class SearchDto extends BaseData {
	private Integer limit;
	private Integer offset;
	private String type;
	private String keyword;

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

}
