package com.tistory.ospace.simpleproject.application;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.Marshaller;

import org.apache.commons.lang.LocaleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
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
@EnableWebMvc
@ComponentScan(basePackages = { "kr.co.polarium.lccgw" }, includeFilters = { @Filter(Controller.class), @Filter(ControllerAdvice.class) })
public class WebConfig implements WebMvcConfigurer {

	@Autowired
	private Environment env;
	
	@Autowired
	private SessionService sessionService;
	
	@Value("${LCC-GW.locale}")
	private String locale;

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
	    converters.add(marshallingHttpMessageConverter());
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
	public MarshallingHttpMessageConverter marshallingHttpMessageConverter() {
		MarshallingHttpMessageConverter converter = new MarshallingHttpMessageConverter();
		converter.setMarshaller(jaxb2Marshaller());
		converter.setUnmarshaller(jaxb2Marshaller());
		return converter;
	}

	@Bean
	public Jaxb2Marshaller jaxb2Marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setPackagesToScan(new String[] { "com.navitaire.model.booking", "org.springframework.hateoas" });

		Map<String, Object> marshallerProperties = new HashMap<String, Object>();
		marshallerProperties.put(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.valueOf(env.getProperty("xml.pretty.print")));
		marshaller.setMarshallerProperties(marshallerProperties);
		return marshaller;
	}

	@Bean
	public RequestContextListener requestContextListener(){
	    return new RequestContextListener();
	}
	
	@Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("**").addResourceLocations("classpath:/swagger-ui/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
	
	@Bean
	public LocaleResolver localeResolver() {
		CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
		cookieLocaleResolver.setDefaultLocale(LocaleUtils.toLocale(locale));
		
		return cookieLocaleResolver;
	}
	
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource bean = new ReloadableResourceBundleMessageSource();
	    bean.setBasename("classpath:properties/messages");
	    bean.setDefaultEncoding("UTF-8");
	    return bean;
	}
	
	@Override
	public Validator getValidator() {
		LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
	    bean.setValidationMessageSource(messageSource());
	    return bean;
	}
}