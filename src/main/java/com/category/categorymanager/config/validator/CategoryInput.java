package com.category.categorymanager.config.validator;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CategoryValidator.class)
public @interface CategoryInput {
    String message() default "형식이 일치하지 않습니다";
    Class[] groups() default {};
    Class[] payload() default {};
}
