package com.tistory.ospace.simpleproject.repository.dto;

import org.apache.ibatis.type.Alias;

import com.tistory.ospace.simpleproject.util.Gender;

@Alias("UserProp")
public class UserPropDto extends UserDto {
    private static final long serialVersionUID = 7731387950915163222L;
    
    private String birthday;
	private String email;
	private Gender gender;

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}
}