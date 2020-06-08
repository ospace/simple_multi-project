package com.tistory.ospace.simpleproject.repository;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.tistory.ospace.simpleproject.repository.dto.SearchDto;
import com.tistory.ospace.simpleproject.repository.dto.UserPropDto;

@Mapper
public interface UserPropRepository {

	int countBy(@Param("search") SearchDto search);

	List<UserPropDto> findAllBy(@Param("search") SearchDto search);

	List<UserPropDto> findAllIn(@Param("ids") Collection<Integer> ids);

	UserPropDto findById(Integer id);
	
	UserPropDto findByLoginId(String loginId);

	void insert(UserPropDto user);

	void update(UserPropDto user);

	void deletetById(Integer id);

	

}
