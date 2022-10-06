package com.category.categorymanager.config.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CategoryValidator implements ConstraintValidator<CategoryInput, String> {
    @Override
    public void initialize(CategoryInput constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == "Unknwon") {
            return false;
        }
        return true;
    }
}
