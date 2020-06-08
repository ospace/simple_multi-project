package com.tistory.ospace.simpleproject.model.validator;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Foo {
	@NotNull(message = "{valid.fooMsg}")
	private Integer id;
	
	@NotEmpty(message = "이름을 입력해주세요")
	private String name;
	
	@NotNull(message = "개수를 입력해주세요")
	@DecimalMin(value = "1.0", message = "1 이상 값을 입력해주세요")
	private BigDecimal count;
	
	@NotNull(message = "전화번호를 입력해주세요")
	@Phone(message = "잘못된 전화번호입니다")
	private String phoneNumber;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BigDecimal getCount() {
		return count;
	}
	public void setCount(BigDecimal count) {
		this.count = count;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}