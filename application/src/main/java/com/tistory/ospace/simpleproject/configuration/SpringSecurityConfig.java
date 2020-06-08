package com.tistory.ospace.simpleproject.configuration;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;

import com.tistory.ospace.simpleproject.service.UserService;


@Configuration
@EnableWebSecurity
public class SpringSecurityConfig  extends WebSecurityConfigurerAdapter {
	private static final Logger logger = LoggerFactory.getLogger(SpringSecurityConfig.class);
	
	@Autowired
	private UserService userService;
	
	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

	@Override
	public void configure( WebSecurity web ) throws Exception {
		web.ignoring().antMatchers("/h2/**", "/favicon.ico", "/assets/**", "/css/**", "/font/**", "/js/**", "/components/**");
	}
 
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	    ContentNegotiationStrategy contentNegotiationStrategy = http.getSharedObject(ContentNegotiationStrategy.class);
	    if (contentNegotiationStrategy == null) {
            contentNegotiationStrategy = new HeaderContentNegotiationStrategy();
        }
	    
	    MediaTypeRequestMatcher preferredMatcher = new MediaTypeRequestMatcher(
            contentNegotiationStrategy,
            MediaType.APPLICATION_FORM_URLENCODED,
            MediaType.APPLICATION_JSON,
            MediaType.MULTIPART_FORM_DATA
        );
	    preferredMatcher.setUseEquals(true);
	    
		http
		.headers()
		    //X-Frame-Options 셋팅 , 크로스 사이트 스크립트 방지 해재 default 'DENY'
			.addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))	 
			.and()
			.csrf().disable()
		.authorizeRequests()
		 	.antMatchers("/login*").permitAll()
		 	//.antMatchers("/main/**").hasAnyRole("ADMN", "OPER")
		 	.anyRequest().authenticated()
			.and()
		.formLogin()
			.loginPage("/login")
	        .loginProcessingUrl("/loginProc")
	        .defaultSuccessUrl("/main", true)
	        .failureUrl("/login?status=2")
	        .usernameParameter("username")
	        .passwordParameter("password")
	        .and()
		.logout()
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			.logoutSuccessUrl("/main")
			.deleteCookies("JSESSIONID")
			.deleteCookies("REMEMBER_ME_COOKE")
			.invalidateHttpSession( true )
			.and()
		.rememberMe()
			.key("REMEBMER_ME_KEY")
	        .rememberMeServices(tokenBasedRememberMeServices())
			.and()
//		.sessionManagement()			//첫번째 로그인 사용자는 로그아웃, 두번째 사용자 로그인 session-registry-alias : 접속자 정보보기
//			.maximumSessions(1)
//			.expiredUrl("/expireSession")
//			.sessionRegistry(sessionRegistry())
		.exceptionHandling()
		    .defaultAuthenticationEntryPointFor(unauthorizeEntryPoint(), preferredMatcher)
		;
	}
	
	@Bean
	public RememberMeServices tokenBasedRememberMeServices() {
		TokenBasedRememberMeServices tokenBasedRememberMeServices = new TokenBasedRememberMeServices("REMEBMER_ME_KEY", userDetailsService());
		//tokenBasedRememberMeServices.setAlwaysRemember(true);								// 체크박스 클릭안해도 무조건 유지
		tokenBasedRememberMeServices.setTokenValiditySeconds( 60*60*24*30 ); //30일(60초*60분*24시*30일)
		tokenBasedRememberMeServices.setCookieName("REMEMBER_ME_COOKE");
		return tokenBasedRememberMeServices;
	}
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	@Bean
    AuthenticationEntryPoint unauthorizeEntryPoint() {
        return new AuthenticationEntryPoint() {
            @Override
            public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException ex) throws IOException, ServletException {
                logger.error("unauthorized: session[{}] uri[{}] message[{}]", req.getSession().getId(), req.getRequestURI(), ex.getMessage());
                
                String msg = "";
                res.sendError(HttpServletResponse.SC_UNAUTHORIZED, msg);  
            }
        };
    }
}