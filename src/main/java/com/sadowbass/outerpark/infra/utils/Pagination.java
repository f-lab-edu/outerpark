package com.sadowbass.outerpark.infra.utils;

import lombok.Getter;

@Getter
public class Pagination {

    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int DEFAULT_PAGE_NUM = 0;
    private Integer pageSize;
    private Integer pageNum;

    private int limit;
    private int offset;

    public Pagination(Integer pageSize, Integer pageNum) {
        if (pageSize == null) {
            pageSize = DEFAULT_PAGE_SIZE;
        }

        if (pageNum == null) {
            pageNum = DEFAULT_PAGE_NUM;
        }

        this.pageSize = pageSize < 1 ? DEFAULT_PAGE_SIZE : pageSize;
        this.pageNum = pageNum < 1 ? DEFAULT_PAGE_NUM : pageNum - 1;

        this.limit = this.pageSize;
        this.offset = this.pageSize * this.pageNum;
    }

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }
}
