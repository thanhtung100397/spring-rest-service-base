package com.spring.baseproject.base.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel
public class PageDto<T> {
    @ApiModelProperty(notes = "page index")
    private int pageIndex;
    @ApiModelProperty(notes = "page size", position = 1)
    private int pageSize;
    @ApiModelProperty(notes = "total result number", position = 2)
    private int totalItems;
    @ApiModelProperty(notes = "list items of current page", position = 3)
    private List<T> items;

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

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}
