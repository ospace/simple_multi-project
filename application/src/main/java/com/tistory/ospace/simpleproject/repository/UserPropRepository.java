package com.tistory.ospace.simpleproject.repository;

import org.apache.ibatis.annotations.Mapper;

import com.tistory.ospace.base.ICRUDRepository;
import com.tistory.ospace.simpleproject.repository.dto.UserPropDto;

@Mapper
public interface UserPropRepository extends ICRUDRepository<UserPropDto> {

	UserPropDto findByLoginId(String loginId);
}
