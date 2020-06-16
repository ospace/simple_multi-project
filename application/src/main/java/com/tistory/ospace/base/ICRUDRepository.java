package com.tistory.ospace.base;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tistory.ospace.base.data.SearchDto;


public interface ICRUDRepository<T> {
	
	Integer countBy(@Param("search") SearchDto search, @Param("entity") T entity);
	
	List<T> findAllBy(@Param("search") SearchDto search, @Param("entity") T entity);
	
	List<T> findAllIn(@Param("ids") Collection<Integer> ids);
	
	T findById(Integer id);
	
	T findBy(T entity);
	
	void insert(T entity);
	
	void update(T entity);
	
	void deleteById(Integer id);
}
