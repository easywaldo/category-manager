package com.category.categorymanager.config.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

@Component
@RequiredArgsConstructor
public class CustomValidator implements Validator {

    private final SpringValidatorAdapter validatorAdapter;

    @Override
    public boolean supports(Class<?> clazz) {
        return RequestValidator.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RequestValidator requestValidator = (RequestValidator) target;
        requestValidator.validate(validatorAdapter, errors);
    }
}
