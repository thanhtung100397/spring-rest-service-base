package com.spring.baseproject.utils.jpa;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class SortAndPageFactory {
    public static Pageable createPageable(List<String> sortBy, List<String> sortType,
                                          int pageIndex, int pageSize, Integer maxPageSize) {
        if (pageIndex < 0) {
            pageIndex = 0;
        }
        if (maxPageSize == null || maxPageSize < 1) {
            if (pageSize < 1) {
                pageSize = 1;
            }
        } else {
            if (pageSize < 1) {
                pageSize = 1;
            } else if (pageSize > maxPageSize) {
                pageSize = maxPageSize;
            }
        }
        Sort sort = createSort(sortBy, sortType);
        if (sort == null) {
            return PageRequest.of(pageIndex, pageSize);
        } else {
            return PageRequest.of(pageIndex, pageSize, sort);
        }
    }

    public static Sort createSort(List<String> sortBys,
                                  List<String> sortTypes) {
        List<Sort.Order> orders = new ArrayList<>();
        if (sortBys != null && sortTypes != null) {
            for (int i = 0; i < sortBys.size(); i++) {
                String sortBy = sortBys.get(i);
                Sort.Direction direction;
                try {
                    String sortType = sortTypes.get(i);
                    direction = Sort.Direction.valueOf(sortType.toUpperCase());
                    orders.add(new Sort.Order(direction, sortBy));
                } catch (Exception ignore) {
                }
            }
        }
        if (orders.isEmpty()) {
            return null;
        } else {
            return Sort.by(orders);
        }
    }

    public static Sort createSort(String sortBy,
                                  String sortType) {
        Sort.Direction direction;
        try {
            direction = Sort.Direction.valueOf(sortType.toUpperCase());
        } catch (Exception ignore) {
            direction = Sort.Direction.ASC;
        }
        return Sort.by(direction, sortBy);
    }
}
