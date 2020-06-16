package com.tistory.ospace.simpleproject.repository;

import org.apache.ibatis.annotations.Mapper;

import com.tistory.ospace.base.ICRUDRepository;
import com.tistory.ospace.simpleproject.repository.dto.AuthorityDto;


@Mapper
public interface AuthorityRepository extends ICRUDRepository<AuthorityDto> {
}
