package com.sadowbass.outerpark.infra.utils.validation;

import lombok.Getter;

import javax.validation.ConstraintViolation;

@Getter
public class ViolationResult {

    private String property;
    private String message;
    private Object invalidValue;

    public ViolationResult(ConstraintViolation<Object> constraintViolation) {
        this.property = constraintViolation.getPropertyPath().toString();
        this.message = constraintViolation.getMessage();
        this.invalidValue = constraintViolation.getInvalidValue();
    }
}
