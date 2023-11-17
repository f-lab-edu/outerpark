package com.sadowbass.outerpark.infra.utils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public abstract class ValidationUtils {

    private static final Validator VALIDATOR;

    static {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            VALIDATOR = factory.getValidator();
        }
    }

    public static boolean validate(Object obj) {
        Set<ConstraintViolation<Object>> validate = VALIDATOR.validate(obj);
        return validate.size() == 0;
    }
}
