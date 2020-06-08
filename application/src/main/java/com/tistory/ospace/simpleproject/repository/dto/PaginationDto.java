package com.tistory.ospace.simpleproject.repository.dto;

import org.apache.ibatis.type.Alias;

import com.tistory.ospace.core.data.BaseDto;

@Alias("Pagination")
public class PaginationDto extends BaseDto {
    
	private int     pageNo = 1;	//현재 페이지
	private int     size = 10;	//한번에 표시되는 페이지 크기
	private int     limit = 10;  //한 페이지에 표시되는 데이터 개수
	private int     total;		//전체 목록수
	
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int page) {
		this.pageNo = page;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}

	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	/**
	 * 총페이지 수
	 * @return
	 */
	public int getTotalPage() {
		return (int)Math.ceil( (double)getTotal() / (double)getLimit());
	}
	/**
	 * 시작 페이지
	 * @return
	 */ 
	public int getStartPage() {
		return getIndexPage() * getSize() + 1;
	}
	/**
	 * 끝 페이지
	 * @return
	 */
	public int getEndPage() {
		return Math.min((getIndexPage()+1) * getSize(), getTotalPage());
	}
	
	protected int getIndexPage() {
		return (getPageNo()-1)/getSize();
	}
	
	public int getOffset() {
		return (getPageNo()-1)*getLimit();
	}
}
