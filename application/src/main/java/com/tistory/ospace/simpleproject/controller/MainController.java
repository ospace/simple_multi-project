package com.tistory.ospace.simpleproject.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tistory.ospace.core.util.CmmUtils;
import com.tistory.ospace.simpleproject.util.SessionUtils;

@Controller
public class MainController {
	private static final Logger logger = LoggerFactory.getLogger(MainController.class);

	/**
     * 기본페이지가 메인 화면
     * 
     * @return 
     */
	@RequestMapping("/")
	public String index() {
		return "redirect:/main";
	}

	/**
	 * 로그인 폼
	 * 
	 * @return 
	 */
	 @RequestMapping(value = "/login")
	 public String login(HttpServletRequest req) {
		 logger.info("login: remote[{}] port[{}] session[{}]", req.getRemoteAddr(), req.getRemotePort(), req.getRequestedSessionId());
		 return "none:login";
	 }
	
	 /**
      * 메인화면
      * 
      * @return 
      */
	 @RequestMapping(value = "/main")
	 public String main(Model model) {
		 logger.info("main begin");
		 long runtime = System.currentTimeMillis();
		 
		 logger.debug("principal[{}]", CmmUtils.toString(SessionUtils.getSecurityPrincipal()));
		 
		 logger.info("main end: runtime[{} msec]", System.currentTimeMillis()-runtime);
		 return "normal:main";
	 }
}
