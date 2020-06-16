package com.tistory.ospace.simpleproject.repository;

import org.apache.ibatis.annotations.Mapper;

import com.tistory.ospace.base.ICRUDRepository;
import com.tistory.ospace.simpleproject.repository.dto.FileDto;

@Mapper
public interface FileRepository extends ICRUDRepository<FileDto> {
}
