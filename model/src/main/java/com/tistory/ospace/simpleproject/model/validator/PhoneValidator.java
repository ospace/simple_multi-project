package com.tistory.ospace.simpleproject.model.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<Phone, String>{

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		
		if(null == value || value.isEmpty()) return false;
		
		if(11 < value.length()) return false;
		
		return true;
	}

}
