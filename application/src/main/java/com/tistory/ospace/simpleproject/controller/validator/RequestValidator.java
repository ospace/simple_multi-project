package com.tistory.ospace.simpleproject.controller.validator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.tistory.ospace.common.util.DataUtils;
import com.tistory.ospace.simpleproject.model.Request;

@Component
public class RequestValidator extends SimpleSmartValidator {

    @Autowired(required = false)
    private List<SimpleSmartValidator> validators;

    @Override
    public boolean supports(Class<?> clazz) {
        return Request.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors, Object... validationHints) {
        if (errors.hasErrors()) return;

        if (!(target instanceof Request)) return;
        Request<?> rq = (Request<?>) target;

        if (null == rq.getRequest()) return;
        Class<?> rqClass = rq.getRequest().getClass();

        DataUtils.forEach(validators, it -> {
            if (it.supports(rqClass)) {
                it.validate(rq.getRequest(), errors, target);
            }
        });
    }
}
