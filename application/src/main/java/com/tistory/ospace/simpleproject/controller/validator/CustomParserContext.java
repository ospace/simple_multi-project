package com.tistory.ospace.simpleproject.controller.validator;

import org.springframework.expression.ParserContext;

public class CustomParserContext implements ParserContext {
	
	@Override
    public boolean isTemplate() {
      return true;
    }

    @Override
    public String getExpressionPrefix() {
      return "${";
    }

    @Override
    public String getExpressionSuffix() {
      return "}";
    }

}
