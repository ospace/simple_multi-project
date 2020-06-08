package com.tistory.ospace.simpleproject.repository;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.tistory.ospace.simpleproject.repository.dto.UserDto;
import com.tistory.ospace.simpleproject.repository.dto.SearchDto;


@Mapper
public interface UserRepository {
	
	int countBy(@Param("search") SearchDto search);
	
	List<UserDto> findAllBy(@Param("search") SearchDto search);
	
	List<UserDto> findAllIn(@Param("ids") Collection<Integer> ids);
	
	UserDto findById(Integer id);

	UserDto findByLoginId(String loginId);
	
	void insert(UserDto account);
	
	void update(UserDto account);
	
	void deletetById(Integer id);
	
//	void insertAccountHistory(AccountHistory accountHistory);

	
	
}
