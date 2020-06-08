package com.tistory.ospace.simpleproject.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.tistory.ospace.simpleproject.repository.dto.CodeDto;
import com.tistory.ospace.simpleproject.repository.dto.GroupCodeDto;
import com.tistory.ospace.simpleproject.repository.dto.SearchDto;

@Mapper
public interface CodeRepository {

	int countCodeBy(@Param("search") SearchDto search, @Param("entity") CodeDto entity);
	
	List<CodeDto> findAllCodeBy(@Param("search") SearchDto search, @Param("entity") CodeDto entity);
	
	CodeDto findCodeById(String id);
	
	CodeDto findCodeBy(CodeDto code);
	
	GroupCodeDto findGroupById(String id);
	
	String findLastCodeByGroup(String groupId);
	
	Integer findMaxOrderByGroup(String groupId);
	
	/**
	 * 정렬순으로 첫번째 코드 한개를 획득
	 * @param direction 정렬방향(true: 내림차순, false: 오름차순)
	 */
	CodeDto findFirstOrderBy(@Param("code") CodeDto code, @Param("direction") boolean direction);
	
	void insertCode(CodeDto code);

	void insertGroup(GroupCodeDto group);
	
	void updateCode(CodeDto code);
	
	void updateGroup(GroupCodeDto group);

	void rearrange(@Param("code") CodeDto code, @Param("value") Integer value);
	
	void deleteCodeBy(CodeDto code);
	
	void deletetGroupBy(GroupCodeDto group);
	
}
