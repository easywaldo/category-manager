package com.category.categorymanager.config.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public interface RequestValidator {

    void validate(Validator validator, Errors errors);

}