package com.tistory.ospace.simpleproject.controller;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tistory.ospace.common.BaseException;
import com.tistory.ospace.common.DataUtils;
import com.tistory.ospace.common.SessionUtils;
import com.tistory.ospace.simpleproject.controller.validator.RequestValidator;
import com.tistory.ospace.simpleproject.datacontract.Response;

import io.swagger.annotations.Api;

@Api(value = "OpenApiController", description = "Open API")
@CrossOrigin(origins = "*", exposedHeaders="sessionid")
@RestController
@RequestMapping("/api/")
public class OpenApiController {
	private static final Logger LOGGER = LoggerFactory.getLogger(OpenApiController.class);
	
	@Autowired
	private RequestValidator requestValidator;
	
//	@Autowired
//	private SimpleProjectConfiguration configuration;
	
	//@Autowired
	//private DataContractFactory dataContractFactory;
	
//	@Autowired
//	private MonitoringService monitoringService;
	
	@PostConstruct
	private void init() {
	}
	
	@InitBinder
	protected void initBinder(WebDataBinder binder){
		Class<?> c = binder.getTarget().getClass();
		if(requestValidator.supports(c)) binder.addValidators(requestValidator);
	}

	@ExceptionHandler({BaseException.class})
	public Response<Void> handleLccBaseException(HttpServletRequest request, BaseException ex) {
		//String loggingThreadTime = NavitaireDateTimeconverter.getLoggingTime(request.getSession().getLastAccessedTime());
		String loggingThreadTime = "-";
		String errorId = DataUtils.generateKey(8);
		if(ex.getMessage().contains("Session token authentication failure")) {
			SessionUtils.invalidate();
		}
		
		LOGGER.error("###오류 탐색 source({})### - lastAccessedTime[{}] threadId[{}] SessionId[{}]", errorId, loggingThreadTime, Thread.currentThread().getName(), request.getSession().getId());
		LOGGER.error("[{}] exception[{}]", ex.getSystem(), ex.getMessage(), ex);
		
		return Response.error(ex.getStatus(), ex.getMessage(), errorId);
	}
	
	@ExceptionHandler({Exception.class})
	public Response<Void> handleException(HttpServletRequest request, Exception ex) {
		String loggingThreadTime = "-";
		String errorId = DataUtils.generateKey(8);
		LOGGER.error("###시스템 오류 탐색 source({})### - lastAccessedTime[{}] threadId[{}] SessionId[{}] ", errorId, loggingThreadTime, Thread.currentThread().getName(), request.getSession().getId() );
		LOGGER.error("exception[{}]", ex.getMessage(), ex);
		
		return Response.error(99, ex.getMessage(), errorId);
	}
	
	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public void handleBadRequest(HttpMessageNotReadableException ex) {
		LOGGER.error("BadRequest : {}", ex.getMessage(), ex);
	}

	@GetMapping
	public String ping() {
		LOGGER.info("ping begin");
		return "pong";
	}
//	private static final String sessionIdMsg = "API에서는 sessionid를 입력해서 요청해야한다.";
//	
//	@ApiOperation(value="테스트", notes="테스트용")
//	@ApiImplicitParams({
//		@ApiImplicitParam(name="sessionid", value=sessionIdMsg, dataType="string", paramType="header"),
//		@ApiImplicitParam(name="request", value="요청데이터", dataType="Object", paramType="body")
//	})
//	@RequestMapping(value = "/searchFareSchedule", method = { RequestMethod.POST }, consumes = {"application/json"}, produces = {"application/json"})
//	@ResponseBody
//	public String test(
//			@RequestBody @Valid Request<SearchFareScheduleRQ> request, BindingResult bindingResult) throws Exception {
//		
//		if(bindingResult.hasErrors()) {
//			LOGGER.error("error : bindingResult[{}] count[{}]", bindingResult, bindingResult.getErrorCount());
//			return Response.error(20, generateValidMessage(bindingResult), null, request.getRequest());
//		}
//		
//		LOGGER.info("request - searchFareSchedule : {}", request);
//		long begin_time = System.currentTimeMillis();
//		AvailabilityCondVO req = dataContractFactory.create(request.getRequest());
//		
//		if(StringUtils.isEmpty(req.getCurrency())) {
//			req.setCurrency(CurrencyType.KRW.code);
//		}
//		
//		List<SearchFareSchedulePriceRS> res = new ArrayList<>();
//		DataUtils.iterate(bookingService.getAvailabilityPriceWithTax(req), it->{
//			res.addAll(dataContractFactory.create(it));
//		});
//		
//		//2.최저가 요금으로 정렬
////		res.sort((l,r)->null == l ? -1 : l.sumPrice().compareTo(r.sumPrice()));
//				
//		long end_time = System.currentTimeMillis();
//		LOGGER.info("response - searchFareSchedule : runtime[{} msec]", (end_time-begin_time));
//		if(LOGGER.isErrorEnabled()) {
//			LOGGER.debug("response - searchFareSchedule : {}", CmmUtils.toJsonString(res));
//		}
//		
//		return ok(request.getRequest(), res);
//	}
	
//	@ApiOperation(value="세션유지", notes="세션 유지 API - 현재는 항공사 세션과 무관하게 동작하기에 특별하게 의미가 없음")
//	@RequestMapping(value = "/session", method = { RequestMethod.POST }, produces = {"application/json"})
//	public Response<SessionRS> session(HttpSession session) {
//		return ok(SessionRS.of(session));
//	}
//	
//	@ApiOperation(value="세션종료", notes="세션 종료 API - 세션이 종료되면 항공사 세션도 같이 종료됨")
//	@RequestMapping(value = "/closeSession", method = { RequestMethod.POST }, produces = {"application/json"})
//	public Response<Void> closeSession(HttpSession session) {
//		SessionUtils.invalidate();
//		return ok(null);
//	}
//	
//	@ApiOperation(value="서버정보", notes="서버 정보 조회 API")
//	@RequestMapping(value = "/info", method = { RequestMethod.GET }, produces = {"application/json"})
//	public Response<BuildProperties> info(HttpSession session) {
//		return Response.ok(null, monitoringService.getBuildInfo());
//	}
	
//	private final String AUTH_REMOATEADDRS = "|0:0:0:0:0:0:0:1|127.0.0.1|0.0.0.1|211.202.25.242|1.214.218.218|";
//	private boolean isAuthRemoateAddr(String strRemoteAddr) {
//		boolean isAuth = false;
//		
//		if(-1 < AUTH_REMOATEADDRS.indexOf(strRemoteAddr)) {
//			isAuth = true;
//		}
//		
//		return isAuth;
//	}
	
//	@ApiOperation(value="스케쥴 조회 lock 처리", notes="특정항공사 스케쥴 조회 lock 처리")
//	@RequestMapping(value = "/onLockSearch", method = { RequestMethod.POST }, produces = {"application/json"})
//	public Response<String> onLockSearch(HttpServletRequest request) {
//		String[] suppliers = request.getParameterValues("suppliers");
//		String remoteIp = request.getHeader("X-FORWARDED-FOR");
//		LOGGER.info("    request.getRemoteAddr() : [{}], remoteIp : [{}]", request.getRemoteAddr(), remoteIp);
//		
//		boolean isAuth = false;
//		if(isAuthRemoateAddr(request.getRemoteAddr())) {
//			tmssManager.onLockSearchCacheItem(suppliers);
//			isAuth = true;
//		}
//		
//		return Response.ok(null, "onLockSearch - suppliers : ["+ String.join(", ", suppliers) +"], Auth : ["+ isAuth +"]");
//	}
//	
//	@ApiOperation(value="스케쥴 조회 lock 제거", notes="특정항공사 스케쥴 조회 lock 제거")
//	@RequestMapping(value = "/removeLockSearch", method = { RequestMethod.POST }, produces = {"application/json"})
//	public Response<String> removeLockSearch(HttpServletRequest request) {
//		String[] suppliers = request.getParameterValues("suppliers");
//		
//		boolean isAuth = false;
//		if(isAuthRemoateAddr(request.getRemoteAddr())) {
//			tmssManager.removeLockSearchCacheItem(suppliers);
//			isAuth = true;
//		}
//		
//		return Response.ok(null, "removeLockSearch - suppliers : ["+ String.join(", ", suppliers) +"], Auth : ["+ isAuth +"]");
//	}
//	
//	private <P> Response<P> ok(P response) {
//		return Response.ok(SessionUtils.getId(), response);
//	}
//	
//	private <P,Q> Response<P> ok(Q request, P response) {
//		return Response.ok(SessionUtils.getId(), request, response);
//	}
}
