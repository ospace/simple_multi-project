package com.tistory.ospace.common;

import java.util.function.Supplier;

import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class SessionUtils {
	public static HttpSession getSession() {
		return getSession(false);
	}
	
	public static HttpSession getSession(boolean isCreate) {
		ServletRequestAttributes servletRequestAttribute = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        return (null == servletRequestAttribute) ? null : servletRequestAttribute.getRequest().getSession(isCreate); //true: allow created
	}
	
	public static String getId() {
		return getId(false);
	}
	
	public static String getId(boolean isCreate) {
		HttpSession session = getSession(isCreate);
		return null == session ? null : session.getId();
	}
	
	public static <R> R getJson(String name, Class<R> clazz) {
		return getJson(getSession(false), name, clazz);
	}
	
	public static <R> R getJson(HttpSession session, String name, Class<R> clazz) {
		String res = get(session, name, String.class);
		return CmmUtils.toJsonObject(res, clazz);
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

	/* Sprint Security에서 Principal 가져오는 부분
	 * 추후 Generic형태로 타입을 지정할 수 있도록?
	public static Object getSecurityPrincipal() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
	    Authentication authentication = securityContext.getAuthentication();
	    return (authentication == null) ? null : authentication.getPrincipal();
	}
	*/
}
