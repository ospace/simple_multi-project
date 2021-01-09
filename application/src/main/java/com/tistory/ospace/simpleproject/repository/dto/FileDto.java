package com.tistory.ospace.simpleproject.repository.dto;

import org.apache.ibatis.type.Alias;

import com.tistory.ospace.common.core.BaseDto;

@Alias("File")
public class FileDto extends BaseDto {
	private Integer id;
	private String  originalFilename;
	private String  filename;
	private String  extension;
	private String  type;
	private Long    size;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getOriginalFilename() {
		return originalFilename;
	}
	public void setOriginalFilename(String originalFilename) {
		this.originalFilename = originalFilename;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Long getSize() {
		return size;
	}
	public void setSize(Long size) {
		this.size = size;
	}
}
