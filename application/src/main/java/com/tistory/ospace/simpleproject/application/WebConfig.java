package com.tistory.ospace.simpleproject.application;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.LocaleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.tistory.ospace.simpleproject.application.customMapper.CustomObjectMapper;
import com.tistory.ospace.simpleproject.application.customMapper.JsonDateSerializer;
import com.tistory.ospace.simpleproject.service.SessionService;

@Configuration
//@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

	@Autowired
	private Environment env;
	
	@Autowired
	private SessionService sessionService;
	
	//@Value("${LCC-GW.locale}")
	private String locale = "en";

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		configurer.useRegisteredExtensionsOnly(false)
		.favorPathExtension(true)
		.favorParameter(false)
		.ignoreAcceptHeader(false)
		.defaultContentType(MediaType.APPLICATION_JSON)
		.mediaType("html", MediaType.TEXT_HTML)
		.mediaType("json", MediaType.APPLICATION_JSON)
		.mediaType("xml", MediaType.APPLICATION_XML);
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
	    converters.add(mappingJacksonHttpMessageConverter());
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(sessionService);
	}
	
	@Bean
	public MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter() {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setPrettyPrint(Boolean.valueOf(env.getProperty("json.pretty.print")));
		converter.setObjectMapper(new CustomObjectMapper());

		ObjectMapper objectMapper = converter.getObjectMapper();
		objectMapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false);
		objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
		
		SimpleModule module = new SimpleModule();
		module.addSerializer(Date.class, new JsonDateSerializer());
	    objectMapper.registerModule(module);
	    
		converter.setObjectMapper(objectMapper);
		  
		return converter;
	}

	@Bean
	public RequestContextListener requestContextListener(){
	    return new RequestContextListener();
	}
	
	@Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/doc/**").addResourceLocations("classpath:/swagger-ui/");
    }
	
	@Bean
	public LocaleResolver localeResolver() {
		CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
		cookieLocaleResolver.setDefaultLocale(LocaleUtils.toLocale(locale));
		
		return cookieLocaleResolver;
	}
	
//	@Bean
//	public MessageSource messageSource() {
//		ReloadableResourceBundleMessageSource bean = new ReloadableResourceBundleMessageSource();
//	    bean.setBasename("classpath:properties/messages");
//	    bean.setDefaultEncoding("UTF-8");
//	    return bean;
//	}
	
//	@Override
//	public Validator getValidator() {
//		LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
//	    bean.setValidationMessageSource(messageSource());
//	    return bean;
//	}
}