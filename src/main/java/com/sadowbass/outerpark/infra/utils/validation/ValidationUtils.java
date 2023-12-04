package com.sadowbass.outerpark.infra.utils.validation;

import com.sadowbass.outerpark.infra.utils.validation.exception.ValidationException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class ValidationUtils {

    private static final Validator VALIDATOR;

    static {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            VALIDATOR = factory.getValidator();
        }
    }

    private ValidationUtils() {
        
    }

    public static void validate(Object obj) {
        Set<ConstraintViolation<Object>> validate = VALIDATOR.validate(obj);
        if (!validate.isEmpty()) {
            throw new ValidationException(collectViolation(validate));
        }
    }

    private static List<ViolationResult> collectViolation(Set<ConstraintViolation<Object>> violations) {
        return violations.stream()
                .map(ViolationResult::new)
                .collect(Collectors.toList());
    }
}
