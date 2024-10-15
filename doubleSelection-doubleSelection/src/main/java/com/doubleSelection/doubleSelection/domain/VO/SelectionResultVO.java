package com.doubleSelection.doubleSelection.domain.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SelectionResultVO {
    private Integer status;
    private String studentName;
    private String mentorName;
    private String studentEmail;
    private String mentorEmail;
}
