package com.tistory.ospace.simpleproject.configuration;

import java.io.File;
import java.net.URL;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;

import com.tistory.ospace.simpleproject.service.SessionService;

@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {
//    private static final Logger logger = LoggerFactory.getLogger(UserVueController.class);
    
	@Autowired
	private SessionService sessionService;
	
	@Autowired
	private MessageSource messageSource;
	
	/*
	 * tiles의 적용으로 변경된 기본 document의 위치를 resources/templates으로 변경
	 */
	@Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> webServerFactoryCustomizer() {
        URL templateUrl = Class.class.getResource("/templates");
        return factory -> {
            factory.setDocumentRoot(new File(templateUrl.getFile()));
        };
    }
	
	/*
	 * Tiles에 커스컴 FreeMarkerTilesView로 적용
	 */
    @Bean
    public UrlBasedViewResolver tilesViewResolver() { 
        UrlBasedViewResolver resolver = new UrlBasedViewResolver();
        
        resolver.setViewClass(FreeMarkerTilesView.class);
        resolver.setOrder(1); // 먼저 ViewResolver 타야함.

        return resolver;
    }
    
    /*
     * FreeMarker용 Tiles 초기화 빈 등록
     */
    @Bean
    public TilesConfigurer tilesConfigurer() {
        TilesConfigurer tilesConfigurer = new TilesConfigurer();
        
        tilesConfigurer.setCheckRefresh(true);
        tilesConfigurer.setTilesInitializer(new FreeMarkerTilesInitializer());
        
        return tilesConfigurer;
    }
    
	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		configurer
		    .useRegisteredExtensionsOnly(false)      	// 등록된 확장자만 사용 여부
    		.favorPathExtension(true)                	//확장자로 contentType 적용 여부
    		.favorParameter(true)          				//파라미터로 contentType 적용여부
    		.ignoreAcceptHeader(false)  				// accept 헤더 무시 여부
    		.defaultContentType(MediaType.TEXT_HTML) 	//기본 contentType
    		.mediaType("html", MediaType.TEXT_HTML)
    		.mediaType("json", MediaType.APPLICATION_JSON_UTF8)
    		.mediaType("xml", MediaType.APPLICATION_XML);
	}
	
	/*
	 * Http 메시지 변환용?
	 */
//	@Override
//	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//	    converters.add(mappingJacksonHttpMessageConverter());
//	}
//
//	@Bean
//	public MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter() {
//		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
//		//converter.setPrettyPrint(Boolean.valueOf(env.getProperty("json.pretty.print")));
//		converter.setObjectMapper(new CustomObjectMapper());
//
//		ObjectMapper objectMapper = converter.getObjectMapper();
//		objectMapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false);
//		objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
//		objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
//		
//		SimpleModule module = new SimpleModule();
//		module.addSerializer(Date.class, new JsonDateSerializer());
//	    objectMapper.registerModule(module);
//	    
//		converter.setObjectMapper(objectMapper);
//		  
//		return converter;
//	}

//	@Bean
//	public RequestContextListener requestContextListener(){
//	    return new RequestContextListener();
//	}
	
    @Bean
    @Override
    public Validator getValidator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource);
        return bean;
    }

    /*
     * 확장자 기반 contentType을 사용하는 경우 확장자 제거용 
     */
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseSuffixPatternMatch(true);
    }
        
	@Bean
	public LocaleResolver localeResolver() {
	    SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.KOREA);
        return slr;
	}
	
   @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
        registry.addInterceptor(sessionService);
    }
}