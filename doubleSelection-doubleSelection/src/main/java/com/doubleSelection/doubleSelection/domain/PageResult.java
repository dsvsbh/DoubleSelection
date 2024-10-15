package com.doubleSelection.doubleSelection.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageResult<T> {
    private Long total;
    private Integer pageNum;
    private Integer pageSize;
    private Integer pages;
    private List<T> records;
}
