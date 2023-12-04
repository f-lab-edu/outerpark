package com.sadowbass.outerpark.application;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BaseEntity {

    private final LocalDateTime createdDate;
    private Long createdBy;
    private final LocalDateTime modifiedDate;
    private Long modifiedBy;

    public BaseEntity() {
        LocalDateTime now = LocalDateTime.now();

        this.createdDate = now;
        this.modifiedDate = now;
    }

    public void addCreator(Long createdBy) {
        this.createdBy = createdBy;
        this.modifiedBy = createdBy;
    }
}
