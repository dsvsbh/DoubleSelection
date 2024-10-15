package com.doubleSelection.doubleSelection.service;

import com.doubleSelection.doubleSelection.domain.DTO.PageDTO;
import com.doubleSelection.doubleSelection.domain.PageResult;
import com.doubleSelection.doubleSelection.domain.StudentMentorSelection;
import com.doubleSelection.doubleSelection.domain.VO.SelectionListVO;
import com.doubleSelection.doubleSelection.domain.VO.SelectionResultVO;

public interface ISelectionService {
    void studentSelectMentor(Long mentorId);

    void studentCancelSelectMentor(Long mentorId);

    void doubleSelection(Long studentId);

    PageResult<SelectionListVO> list(Integer pageNum, Integer pageSize);

    void reject(Long studentId);

    PageResult<SelectionResultVO> result(PageDTO pageDTO);
}
