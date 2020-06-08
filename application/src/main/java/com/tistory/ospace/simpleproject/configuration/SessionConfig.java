package com.tistory.ospace.simpleproject.configuration;

import org.springframework.context.annotation.Bean;
//import org.springframework.data.redis.connection.RedisPassword;
//import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
//import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
//import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
//import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionIdResolver;

//https://docs.spring.io/spring-session/docs/current-SNAPSHOT/reference/html/httpsession.html
//@Configuration
//@EnableRedisHttpSession
public class SessionConfig { //extends AbstractHttpSessionApplicationInitializer {
	
//	@Value("${spring.redis.host}")
//	private String redisHost;
//
//	@Value("${spring.redis.password}")
//	private String redisPassword;
//
//	@Value("${spring.redis.port}")
//	private int redisPort;
	
//	@Bean
//	JedisConnectionFactory jedisConnectionFactory() {
//		RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
//		redisConfig.setHostName(redisHost);
//		redisConfig.setPort(redisPort);
//		//redisConfig.setPassword(redisPassword);
//		redisConfig.setPassword(RedisPassword.of(redisPassword));
//		
//		return new JedisConnectionFactory(redisConfig);
//	}
//	
//	@Bean
//	RedisTemplate<String, Object> redisTemplate() {
//		final RedisTemplate<String, Object> template = new RedisTemplate<>();
//		template.setConnectionFactory(jedisConnectionFactory());
//		return template;
//	}
//	

//	@Bean HeaderHttpSessionStrategy sessionStrategy() {
//		HeaderHttpSessionStrategy strategy = new HeaderHttpSessionStrategy();
//		strategy.setHeaderName("sessionid");
//		return strategy;
//	}
	
	@Bean
	public HttpSessionIdResolver httpSessionIdResolver() {
		//return new HeaderHttpSessionIdResolver("sessionid"); 
		return HeaderHttpSessionIdResolver.xAuthToken();
	}
}
