package com.tistory.ospace.simpleproject.controller;

import java.security.Principal;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tistory.ospace.core.BaseException;
import com.tistory.ospace.core.util.CmmUtils;
import com.tistory.ospace.core.util.StringUtils;
import com.tistory.ospace.simpleproject.controller.validator.RequestValidator;
import com.tistory.ospace.simpleproject.model.Response;
import com.tistory.ospace.simpleproject.service.MonitoringService;
import com.tistory.ospace.simpleproject.util.SessionUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value = "OpenApiController", description = "Open API")
@CrossOrigin(origins = "*", exposedHeaders="sessionid")
@RestController
@RequestMapping("/api/")
public class OpenApiController {
	private static final Logger LOGGER = LoggerFactory.getLogger(OpenApiController.class);
	
	@Autowired
	private RequestValidator requestValidator;
	
	@Autowired
	private MonitoringService monitoringService;
	
	@PostConstruct
	private void init() {
	}
	
	@InitBinder
	protected void initBinder(WebDataBinder binder){
		Class<?> c = binder.getTarget().getClass();
		if(requestValidator.supports(c)) binder.addValidators(requestValidator);
	}

//	@ExceptionHandler({BaseException.class})
//	public Response<Void> handleLccBaseException(HttpServletRequest request, BaseException ex) {
//		String loggingThreadTime = "-";
//		String errorId = StringUtils.generateKey(8);
//		if(ex.getMessage().contains("Session token authentication failure")) {
//			SessionUtils.invalidate();
//		}
//		
//		LOGGER.error("###오류 탐색 source({})### - lastAccessedTime[{}] threadId[{}] SessionId[{}]", errorId, loggingThreadTime, Thread.currentThread().getName(), request.getSession().getId());
//		LOGGER.error("[{}] exception[{}]", ex.getSystem(), ex.getMessage(), ex);
//		
//		return Response.fail(ex.getStatus(), ex.getMessage(), errorId);
//	}
//	
//	@ExceptionHandler({Exception.class})
//	public Response<Void> handleException(HttpServletRequest request, Exception ex) {
//		String loggingThreadTime = "-";
//		String errorId = StringUtils.generateKey(8);
//		LOGGER.error("###시스템 오류 탐색 source({})### - lastAccessedTime[{}] threadId[{}] SessionId[{}] ", errorId, loggingThreadTime, Thread.currentThread().getName(), request.getSession().getId() );
//		LOGGER.error("exception[{}]", ex.getMessage(), ex);
//		
//		return Response.fail(99, ex.getMessage(), errorId);
//	}
//	
//	@ExceptionHandler
//	@ResponseStatus(HttpStatus.BAD_REQUEST)
//	public void handleBadRequest(HttpMessageNotReadableException ex) {
//		LOGGER.error("BadRequest : {}", ex.getMessage(), ex);
//	}

	@GetMapping
	public String ping() {
		LOGGER.info("ping begin");
		return "pong";
	}
	

    @ApiOperation(
        value = "자신정보",
        notes = "자신의 정보를 조회한다."
    )
    @ApiImplicitParams({
        //@ApiImplicitParam(name = "userId", value = "사용자의 user Id", required = true, dataType = "string", defaultValue = "49")
    })
    @GetMapping("/me")
    //@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    //@PreAuthorize("#username == authentication.principal.username")
    //@PostAuthorize("returnObject.username == authentication.principal.nickName")
    public Principal me(final Principal principal) {
        LOGGER.info("authentication[{}]", CmmUtils.toString(SessionUtils.getAuthentication()));
        
        return principal;
    }
    
	@ApiOperation(value="서버정보", notes="서버 정보 조회 API")
	@RequestMapping(value = "/info", method = { RequestMethod.GET }, produces = {"application/json"})
	public BuildProperties info(HttpSession session) {
		return monitoringService.getBuildInfo();
	}
}
