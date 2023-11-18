package com.sadowbass.outerpark.infra.utils.validation.exception;

import com.sadowbass.outerpark.infra.utils.validation.ViolationResult;

import java.util.Collections;
import java.util.List;

public class ValidationException extends RuntimeException {

    public static final String EXCEPTION_MESSAGE = "유효성검증 실패";
    private final List<ViolationResult> violationResult;

    public ValidationException(List<ViolationResult> violationResults) {
        super(EXCEPTION_MESSAGE);
        this.violationResult = violationResults;
    }

    public List<ViolationResult> getViolationResult() {
        return Collections.unmodifiableList(violationResult);
    }
}
