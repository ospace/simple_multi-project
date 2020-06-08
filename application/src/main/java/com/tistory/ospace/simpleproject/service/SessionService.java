package com.tistory.ospace.simpleproject.service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.tistory.ospace.simpleproject.util.SessionUtils;

@Component
public class SessionService extends HandlerInterceptorAdapter implements HttpSessionListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(SessionService.class);
	
//	@Autowired
//	private SessionManager          sessionManager;
//	
//	@Autowired
//    private RedisOperationsSessionRepository sessionRepository;
	
//	@Autowired
//	private LccGWConfiguration      configuration;
	
	private final String BEGIN = "__runtime__";
	
	@PostConstruct
	private void init() {
		//sessionRepository.setDefaultMaxInactiveInterval(maxInactiveInterval);
	}
	
	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object arg) throws Exception {
		String id = req.getRequestedSessionId();
		String url = req.getRequestURI();
		String api = url.substring(url.lastIndexOf('/'));
		Long begin = System.currentTimeMillis();
		
		LOGGER.trace("preHandle : ID{{}} api[{}] url[{}]", id, api, url);
		
		if("OPTIONS".equals(req.getMethod())) return true;
		
//		switch(api) {
//		default:
//			req.getSession(true); //세션키 생성
//			break;
//		}
		
		SessionUtils.set(req.getSession(), BEGIN, begin);
		
		LOGGER.debug("{} begin", url);
		
		return true;
	}
	
//	private void reponseError(HttpServletResponse res) {
//		//LccError error = LccError.SYSTEM_CLOSED;
//		//responseJson(res, Response.error(error.status, error.getMessage()));
//	}
//
//	private void unauthorized(HttpServletResponse res) throws IOException {
//		res.setStatus(HttpStatus.UNAUTHORIZED.value());
//		//responseJson(res, Response.error(LccError.SYSTEM_AUTH_FAIL.status, "invalid sessionId of HTTP Parameter"));
//	}
	
//	private <T> void responseJson(HttpServletResponse res, T val) {
//		res.setContentType("application/json");
//		res.setCharacterEncoding("UTF-8");
//		
//		res.setHeader("Access-Control-Allow-Origin",  "*");
//		res.setHeader("Access-Control-Expose-Headers",  "sessionid");
//		//res.setHeader("Access-Control-Allow-Methods",  "POST,OPTIONS");
//		//res.setHeader("Access-Control-Max-Age",  "3600");
//		//res.setHeader("Access-Control-Allow-Headers",  "Content-Type,sessionid");
//		
//		PrintWriter out;
//		try {
//			out = res.getWriter();
//			out.write(CmmUtils.toJsonString(val));
//			out.flush();
//		} catch (IOException e) {
//			throw new RuntimeException("sessionService response failed", e);
//		}
//	}
	
	@Override
	public void postHandle(HttpServletRequest req, HttpServletResponse res, Object handler, ModelAndView modelAndView) throws Exception {
		String url = req.getRequestURI();
		long end = System.currentTimeMillis();
		Long begin = SessionUtils.get(req.getSession(), BEGIN, Long.class);
		
		LOGGER.debug("{} end: runtime[{} msec]", url, (null == begin?"-":(end-begin)));
	}
	
//	@Override
//	public void afterCompletion(HttpServletRequest req, HttpServletResponse res, Object handler, Exception ex) throws Exception {
//		String id = SessionUtils.getId();
//		String url = req.getRequestURI();
//		String api = url.substring(url.lastIndexOf('/'));
//		
//		LOGGER.debug("afterCompletion : {}", id);
//	}
//
//	@Override
//	public void sessionCreated(HttpSessionEvent se) {
//		String id = se.getSession().getId();
//		LOGGER.debug("sessionCreated : {}", id);
//	}
//
//	@Override
//	public void sessionDestroyed(HttpSessionEvent se) {
//		String id = se.getSession().getId();
//		LOGGER.debug("sessionDestroyed : {}", id);
//		//sessionManager.logOutAllAsync(se.getSession());
//	}
}


