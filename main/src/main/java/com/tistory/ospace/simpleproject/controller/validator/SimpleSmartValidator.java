package com.tistory.ospace.simpleproject.controller.validator;

import java.util.Map;

import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.validation.Errors;
import org.springframework.validation.SmartValidator;

public abstract class SimpleSmartValidator implements SmartValidator {
	
//	@Autowired
//	private MessageService messageService;
	
	@Override
	public void validate(Object target, Errors errors) {
		validate(target, errors, (Object)null);
	}
	
	private String getErrorMsg(String errorcode) {
		//return messageService.getValidatorMessage(errorcode);
		return errorcode;
	}
	
	public void errorReject(Errors errors, String errorcode) {
		errors.reject(errorcode, getErrorMsg(errorcode));
	}
	
	private String getErrorMsg(String errorcode, Map<String, String> map) {
		String message = getErrorMsg(errorcode);
		
		ExpressionParser expressionParser = new SpelExpressionParser();
		Expression expression = expressionParser.parseExpression(message, new CustomParserContext());
		
		StandardEvaluationContext context = new StandardEvaluationContext(map);
		context.addPropertyAccessor(new MapAccessor());
		
		return expression.getValue(context, String.class);
	}
	
	public void errorReject(Errors errors, String errorcode, Map<String, String> map) {
		errors.reject(errorcode, getErrorMsg(errorcode, map));
	}
	
	public void errorRejectValue(Errors errors, String fieldName, String errorcode) {
		errors.rejectValue(fieldName, errorcode, getErrorMsg(errorcode));
	}
	
}
