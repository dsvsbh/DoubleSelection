package com.doubleSelection.doubleSelection.domain.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SelectionListVO {
    private Long studentId;
    private Integer status;//0 申请中, 1 导师确认, 2 拒绝
    private String name;
    private String email;
    private String major;
    private String interests;
}
