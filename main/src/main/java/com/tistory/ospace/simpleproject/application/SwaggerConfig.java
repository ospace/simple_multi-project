package com.tistory.ospace.simpleproject.application;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tistory.ospace.common.CmmUtils;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Profile({"dev"})
public class SwaggerConfig {
 
	@Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)
//          .globalOperationParameters(getGlobalOperParams())
          .select()
          .apis(RequestHandlerSelectors.basePackage("com.tistory.ospace.controller"))
          .paths(PathSelectors.any())
          .build()
          .apiInfo(apiInfo())
          .useDefaultResponseMessages(false)
          .globalResponseMessage(RequestMethod.POST, getResMsgList());
    }
	
//	private ArrayList<Parameter> getGlobalOperParams(){
//        ArrayList<Parameter> lists = new ArrayList<Parameter>();
//        lists.add(new ParameterBuilder().name("sessionid").description(
//        		"searchFareSchedule이나 searchOrder를 조회 한 Response headers의 sessionid 입력<br>"
//        		+ "▶ <strong>searchFareSchedule, searchOrder</strong>: sessionid를 입력하지 않아도 되며, sessionid 없이 조회 할 경우 새 sessionid가 생성<br>"
//        		+ "▶ <strong>cancelOrder</strong>: sessionid를 입력하지 않음<br>"
//        		).modelRef(new ModelRef("string")).parameterType("header").build());
//        
//        return lists;
//    }
	
	private ArrayList<ResponseMessage> getResMsgList(){
        ArrayList<ResponseMessage> lists = new ArrayList<ResponseMessage>();
//        LccError[] arryLccError = LccError.values();
//        for (LccError it: arryLccError) {
//        	//현재까지는 1~99까지만 시스템 관련 오류로 처리중임. status 값 확장때 수정 필요함.
//        	if(99 < it.status) continue;//(무순서 고려함)
//        	lists.add(new ResponseMessageBuilder().code(it.status).message(it.msg).build());
//		}
        
//        lists.add(new ResponseMessageBuilder().code(500).message("internal error(임시 에러분류, 현재는 200 반환됨) - [Reference Status Code](#status-code)").build());
        lists.add(new ResponseMessageBuilder().code(500).message("internal error(임시 에러분류, 현재는 200 반환됨) - <a href=\"#status-code\">Reference Status Code</a>").build());
        return lists;
    }
	
	private ApiInfo apiInfo() {
		String description = null;
		try {
			description = CmmUtils.readResource("doc/description.md");
		} catch (IOException e) {
			description = "failed to load a description\r\n";
		}
		return new ApiInfoBuilder()
                .title("LCC Gateway Open API")
                .description(description.concat(generateStatusCode()))
                .build();
	}
	
	private String generateStatusCode() {
		String header = 
				"<div class=\"opblock cursor\"><div class=\"responses-inner\">" +
				"<details><summary style=\"cursor:pointer;\">Status Code<sup><sub>(click to open)</sub></sup></summary>" +
				"<div class=\"response-col_description__inner\" style=\"width:100%\">\r\n\r\n" +
				"| Code | Category | Description |\r\n" +
				"|:-|:-|:-|\r\n";
		
		String tail =
				"</div>" +
				"</details>" +
				"</div></div>";
		
		StringBuilder sb = new StringBuilder();
		sb.append(header);
		
//		for(LccError it : LccError.values()) {
//			if(!it.visible) continue;
//			sb.append("|<div class=\"response-col_status\">").append(it.status).append("</div>");
//			sb.append("|<div class=\"response-col_status\">").append(it.getCategory()).append("</div>");
//			sb.append("|<div class=\"markdown\">").append(it.getMessage()).append("</div>");
//			sb.append("|\r\n");
//		}
		
		sb.append(tail);
		
		return sb.toString();
	}
}
