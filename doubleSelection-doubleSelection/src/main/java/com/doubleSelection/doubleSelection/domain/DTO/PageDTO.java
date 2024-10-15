package com.doubleSelection.doubleSelection.domain.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageDTO {
    private Integer pageNum;
    private Integer pageSize;
}
