package com.tistory.ospace.simpleproject.repository;

import org.apache.ibatis.annotations.Mapper;

import com.tistory.ospace.base.ICRUDRepository;
import com.tistory.ospace.simpleproject.repository.dto.UserDto;


@Mapper
public interface UserRepository extends ICRUDRepository<UserDto> {
	
	UserDto findByLoginId(String loginId);
}
