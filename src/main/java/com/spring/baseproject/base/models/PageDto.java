package com.spring.baseproject.base.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.domain.Page;

import java.util.List;

@ApiModel
public class PageDto<T> {
    @ApiModelProperty(notes = "chỉ mục trang, 0-base")
    private int pageIndex;
    @ApiModelProperty(notes = "kích thước trang", position = 1)
    private int pageSize;
    @ApiModelProperty(notes = "tổng số kết quả", position = 2)
    private long totalItems;
    @ApiModelProperty(notes = "danh sách kết quả", position = 3)
    private List<T> items;

    public PageDto(T data) {
    }

    public PageDto(Page<T> page) {
        setPageIndex(page.getNumber());
        setPageSize(page.getSize());
        setTotalItems(page.getTotalElements());
        setItems(page.getContent());
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}
