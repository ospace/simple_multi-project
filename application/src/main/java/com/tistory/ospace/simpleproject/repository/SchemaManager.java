package com.tistory.ospace.simpleproject.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class SchemaManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(SchemaManager.class);
	
	private Reflections                          reflections = new Reflections(SchemaManager.class.getPackage().getName());
	private Map<String, Object>       connectorMap = new HashMap<>();

	@PostConstruct
	private void init() {
		registrySchemaClass(Object.class, connectorMap);
	}
	
	@PreDestroy
	private void shutdown() {
		LOGGER.info("shutdown SchemaManager");
		
		//connectorMap.forEach((k,v)->v.shutdown());
		connectorMap.clear();
	}
	
	private static final Pattern schemaPtn = Pattern.compile("schema(\\w+)$");
	private <T>void registrySchemaClass(Class<T> clazz, Map<String, T> map) {
		
		LOGGER.info("schema registering : {}", clazz.getSimpleName());
		for(Class<?> it : reflections.getSubTypesOf(clazz)) {
			String pkg =it.getPackage().getName();
			//schema 글자를 제외한 나머지 글자를 스키마코드로 사용
			Matcher matcher = schemaPtn.matcher(pkg);
			if(null == matcher || !matcher.find()) continue;
			
			String schema = matcher.group(1);
			
			try {
				map.put(schema, clazz.cast(it.newInstance())); 
				LOGGER.info("  + loaded : shema[{}] class[{}]", schema, it.getName());
			} catch (InstantiationException | IllegalAccessException e) {
				LOGGER.warn("  + failed : schema[{}] {}[{}({})] ", schema, clazz.getSimpleName(), it.getName(), e.getClass().getSimpleName(), e.getMessage(), e);
			}
		}
	}
	
}
