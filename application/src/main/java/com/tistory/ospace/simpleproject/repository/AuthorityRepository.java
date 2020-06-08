package com.tistory.ospace.simpleproject.repository;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.tistory.ospace.simpleproject.repository.dto.AuthorityDto;
import com.tistory.ospace.simpleproject.repository.dto.SearchDto;


@Mapper
public interface AuthorityRepository {
	
	int countBy(@Param("search") SearchDto search);
	
	List<AuthorityDto> findAllBy(@Param("search") SearchDto search, @Param("entity") AuthorityDto entity);
	
	List<AuthorityDto> findAllIn(@Param("ids") Collection<Integer> ids);
	
	//AuthorityDto findById(Integer id);

	//AuthorityDto findByLoginId(String loginId);
	
	void insert(AuthorityDto account);
	
	//void update(AuthorityDto account);
	
	void deletetById(Integer userId);
	
//	void insertAccountHistory(AccountHistory accountHistory);

	
	
}
