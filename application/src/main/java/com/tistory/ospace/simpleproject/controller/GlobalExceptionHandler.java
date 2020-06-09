package com.tistory.ospace.simpleproject.controller;

import java.io.FileNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.tistory.ospace.core.util.CmmUtils;
import com.tistory.ospace.simpleproject.exception.SimpleProjectDataIntegrityException;
import com.tistory.ospace.simpleproject.exception.SimpleProjectDuplicateException;
import com.tistory.ospace.simpleproject.exception.SimpleProjectException;
import com.tistory.ospace.simpleproject.util.ErrorRS;

// 특정 패키지에 제한된 예외 처리
@ControllerAdvice("com.tistory.ospace.simpleproject")
public class GlobalExceptionHandler {
	private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler({FileNotFoundException.class})
	public String handleFileNotFound(FileNotFoundException e, HttpServletRequest req, Model model) {
		logger.error("{}: uri[{}] filename[{}]", e.getClass().getSimpleName(), req.getRequestURI(), e.getMessage());
		
		model.addAttribute("path", req.getRequestURI());
		model.addAttribute("message", e.getMessage());
		
		return "forward:/error/404";
    }

	static private final Pattern constraintPattern = Pattern.compile("constraint fails \\(([\\w`\\.]+), .* FOREIGN KEY \\(([\\w`]+)\\)", Pattern.MULTILINE);
	@ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = SimpleProjectException.class)
    public ErrorRS handleSimpleProjectException(HttpServletRequest req, SimpleProjectException ex) {
	    String message = null;
	    if(ex instanceof SimpleProjectDuplicateException) {
	        message = ex.getMessage();
	        logger.error("중복 오류: {}", message, ex);    
	    } else if (ex instanceof SimpleProjectDataIntegrityException){
	        String source = "다른 곳";
	        
	        if(null == ex.getCause()) {
	            message = ex.getMessage();
	        } else {
	            message = ex.getCause().getMessage();
    	        Matcher matcher = constraintPattern.matcher(message);
    	        if(matcher.find()) {
    	            String group = matcher.group(1);
    	            String splited[] = group.split("\\.");
    	            //source = ModelUtils.toTableName(splited[splited.length-1]);
    	            source = splited[splited.length-1];
    	        }
	        }
    	        
	        logger.error("데이터무결성 오류: error[{}]", message, ex);
	        message = source+"에서 사용중입니다";
	    }
        
        return ErrorRS.of(ex.status, req.getRequestURI(), message);
    }
	
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class) // BindException.class 예외로는 안됨
    public ErrorRS handleValidationExceptions(HttpServletRequest req, HttpMessageNotReadableException ex) {
	    int status = 4;
        String msg = ex.getCause().getMessage();//CmmUtils.toString(ex.getBindingResult());
        logger.error("요청 유효성 오류({}): path[{}] message[{}]", status, req.getRequestURI(), msg);
        return ErrorRS.of(status, req.getRequestURI(), msg);
    }
    
	@ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorRS handleConstraintViolationExceptions(HttpServletRequest req, ConstraintViolationException ex) {
        int status = 3;
        String msg = ex.getMessage();
        logger.error("요청 제약사항 오류({}): path[{}] message[{}]", status, req.getRequestURI(), msg);
        return ErrorRS.of(status, req.getRequestURI(), msg);
    }
    
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ErrorRS handleMethodArgumentNotValidException(HttpServletRequest req, MethodArgumentNotValidException ex) {
        int status = 2;
        String msg = CmmUtils.toString(ex.getBindingResult());
        logger.error("인자 유효성 오류({}): {}", status, msg, ex);
        return ErrorRS.of(status, req.getRequestURI(), msg);
    }
    
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    public ErrorRS handleBaseException(HttpServletRequest req, Exception ex) {
        String msg = ex.getMessage();
        logger.error("서버내부 오류: {}[{}]", ex.getClass().getSimpleName(), msg, ex);
        return ErrorRS.of(1, null==req?"-":req.getRequestURI(), msg);
    }
}