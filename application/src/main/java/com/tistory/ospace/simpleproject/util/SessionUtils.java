package com.tistory.ospace.simpleproject.util;

import java.util.function.Supplier;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.tistory.ospace.common.util.CmmUtils;
import com.tistory.ospace.simpleproject.repository.dto.UserDto;

public class SessionUtils {
	public static HttpSession getSession() {
		return getSession(false);
	}
	
	public static HttpSession getSession(boolean isCreate) {
		ServletRequestAttributes servletRequestAttribute = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        return servletRequestAttribute.getRequest().getSession(isCreate); //true: allow created
	}
	
	public static String getId() {
		return getId(false);
	}
	
	public static String getId(boolean isCreate) {
		HttpSession session = getSession(isCreate);
		return null == session ? null : session.getId();
	}
	
	public static UserDto getCurrentUser() {
	    Object principal = getSecurityPrincipal();
	    if(null == principal || "anonymouseUser".equals(principal)) return null;
	    
	    return (UserDto) principal;
	}
	
	public static Integer getCurrentUserId() {
	    UserDto user = getCurrentUser();
	    return null == user ? null : user.getId();
	}
	
	public static Object get(String name) {
		HttpSession session = getSession(false);
		return null == session ? null : session.getAttribute(name);
	}
	
	public static void set(String name, Object value) {
		HttpSession session = getSession(false);
		if (null != session) session.setAttribute(name, value);
	}
	
	public static <R> R getJson(String name, Class<R> clazz) {
		return getJson(getSession(false), name, clazz);
	}
	
	public static <R> R getJson(HttpSession session, String name, Class<R> clazz) {
		String res = get(session, name, String.class);
		return CmmUtils.toObject(res, clazz);
	}
	
	public static <R> R get(HttpSession session, String name, Class<R> clazz) {
		if(null == session) return null;
		Object res = session.getAttribute(name);
		return null == res ? null : clazz.cast(res);
	}
	
	public static void setJson(String name, Object value) {
		setJson(getSession(false), name, value);
	}
	
	public static void setJson(HttpSession session, String name, Object value) {
		set(session, name, CmmUtils.toJsonString(value));
	}
	
	public static void set(HttpSession session, String name, Object value) {
		if (null != session) session.setAttribute(name, value);
	}
	
	public static void invalidate() {
		HttpSession session = getSession(false);
		if (null != session) session.invalidate();
	}
	
	public static <R, T> R caching(String key, Class<R> resClazz, Supplier<R> action) {
		R ret = SessionUtils.getJson(key, resClazz);
		if(null == ret) {
			ret = action.get();
			SessionUtils.setJson(key, ret);
		}
		
		return ret;
	}

	// Spring security 
	public static Authentication getAuthentication() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
	    return null != securityContext ? securityContext.getAuthentication() : null;
	}
	
	public static Object getSecurityPrincipal() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
	    Authentication authentication = securityContext.getAuthentication();
	    return null != authentication ? authentication.getPrincipal() : null;
	}
	
//--------------------- OAuth2 support -------------------------------------------------------------	
//	@SuppressWarnings("unchecked")
//	public static List<String> getAuthorities() {
//		return (List<String>)getAuthenticationExtraInfo("authorities", List.class);
//	}
//	
//	public static String getUserId() {
//		return getAuthenticationExtraInfo("user_id", String.class);
//	}
//	
//	public static <R> R getAuthenticationExtraInfo(String key, Class<R> clazz) {
//		return getAuthenticationExtraInfo(getAuthentication(), key, clazz);
//	}
//	
//	public static <R> R getAuthenticationExtraInfo(Authentication auth, String key, Class<R> clazz) {
//		Map<String, ?> extraInfo = getAuthenticationExtraInfo(auth);
//		return null != extraInfo ? clazz.cast(extraInfo.get(key)) : null;
//	}
//	
//	
//	public static Map<String, ?> getAuthenticationExtraInfo() {
//		return getAuthenticationExtraInfo(getAuthentication());
//	}
//	
//	@SuppressWarnings("unchecked")
//	public static Map<String, ?> getAuthenticationExtraInfo(Authentication auth) {
//		if(!isOAuth2(auth)) return null;
//		
//		OAuth2AuthenticationDetails oauthDetails = (OAuth2AuthenticationDetails) auth.getDetails();
//	    return null == oauthDetails ? null : (Map<String, Object>) oauthDetails.getDecodedDetails();
//	}
//	
//	public static boolean isOAuth2(Authentication auth) {
//		return null == auth ? false : (auth.getDetails() instanceof OAuth2AuthenticationDetails ? true : false);
//	}
	
	/***
	 * 쿠키값 가져오기 <p>
	 * @param request <br>
	 * @param sName : 쿠키명 <br>
	 * @return String
	 * @throws Exception
	 */
	public static String getCookie(HttpServletRequest request, String sName) throws Exception {
		Cookie [] cookies = request.getCookies();
		if (cookies==null) return "";
		String value = "";
		for(int i=0;i<cookies.length;i++) {
			if(sName.equals(cookies[i].getName())) {
				value = cookies[i].getValue();				
				break;
			}
		}
		return value;
	}
	
	/***
	 * 쿠키값 생성 <p>
	 * @param response <br>
	 * @param sName : 쿠키 명 <br>
	 * @param cValue : 쿠키 값 <br>
	 * @param nTime : 쿠키 우효시간 ( -1 브라우저 생성시만 존재)<br>
	 * @param sDomain : 유효 도메인 ex) www.merong.net , .merong.net
	 */
	public static void setCookie(HttpServletResponse response, String sName, String cValue, int nTime, String sDomain) {
		
		Cookie cookie = new Cookie(sName, cValue.trim());
		cookie.setPath("/");
		if(nTime > 0) nTime = 	3600 * nTime;
		cookie.setMaxAge(nTime);		
		
		if(sDomain != null && !sDomain.equals("")) cookie.setDomain(sDomain);
		
		response.addCookie(cookie);		
	}
	
	/*** 세션 ID 가져오기<p>
	 * @param request <br>
	 * @param name
	 * @return
	 */
	public static String getSessionID(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
	    return session.getId();
	} 
	
	public static int getSessionTime(HttpServletRequest request) {
		HttpSession session   = request.getSession(true);
	    return session.getMaxInactiveInterval();
	} 
	
	/***
	 * 세션 가져오기<p>
	 * @param request <br>
	 * @param name
	 * @return
	 */
	
/*	public static LoginDto getSession(HttpServletRequest request, String sName) {
		HttpSession session = request.getSession(true);
		return (LoginDto)session.getAttribute(sName);
	} */ 
	
	 /***
     * 세션 생성<p>
     * @param request <br>
     * @param sName <br>
     * @param sValue
     */
/*    public static void setSession(HttpServletRequest request, String sName, LoginDto sValue) {
      HttpSession session   = request.getSession(true);
      session.setAttribute(sName, sValue);
    } */
    
    /***
     * 세션 종료<p>
     * @param request <br>
     * @param nMaxTime : 분단위
     */
    public static void setSessionOut(HttpServletRequest request) {
		HttpSession session   = request.getSession(true);
	    session.invalidate();
	} 
    
    /***
     * 세션 시간 설정<p>
     * @param request <br>
     * @param nMaxTime : 분단위
     */
    public static void setSessionTime(HttpServletRequest request, int nMinute) {
		HttpSession session   = request.getSession(true);
		int  nMaxTime = 60 * nMinute; 
	    session.setMaxInactiveInterval(nMaxTime);
	} 
	
    
    /**
     * Remote IP
     * @param request
     * @return
     */
    public static String getRemoteIp(HttpServletRequest request) {
    	    	
    	String ip = null;
		String [] headers = {"X-FORWARDED-FOR","Proxy-Client-IP","WL-Proxy-Client-IP","HTTP_CLIENT_IP","HTTP_X_FORWARDED_FOR"};
		
		for( String header : headers ) {
			ip = request.getHeader(header);
			if(ip != null) return ip; 
		}

		if (ip == null)  ip = request.getRemoteAddr();
		
		return ip;

	}
}
