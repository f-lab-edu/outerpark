package com.sadowbass.outerpark.presentation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sadowbass.outerpark.infra.utils.Pagination;
import lombok.Getter;

import java.util.Collection;

@Getter
public class PageResult<T> {

    private int pageNum;
    private int pageSize;
    private int totalCount;
    private Collection<T> content;
    private int totalPage;
    private boolean isLast;

    public PageResult(Pagination pagination, int totalCount, Collection<T> content) {
        this.pageNum = pagination.getPageNum() + 1;
        this.pageSize = pagination.getPageSize();
        this.totalCount = totalCount;
        this.content = content;

        this.totalPage = (int) Math.ceil((double) this.totalCount / this.pageSize);
        this.isLast = pageNum >= totalPage;
    }

    @JsonProperty("isLast")
    public boolean isLast() {
        return isLast;
    }
}
