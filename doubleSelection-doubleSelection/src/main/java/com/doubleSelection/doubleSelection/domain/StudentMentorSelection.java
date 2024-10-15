package com.doubleSelection.doubleSelection.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Data
@Slf4j
@NoArgsConstructor
public class StudentMentorSelection {
    private Long selectionId;
    private Long studentId;
    private Long mentorId;
    private Integer status;//0 申请中, 1 导师确认, 2 拒绝
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
