package com.doubleSelection.doubleSelection.controller;

import com.doubleSelection.common.core.domain.AjaxResult;
import com.doubleSelection.doubleSelection.domain.DTO.PageDTO;

import com.doubleSelection.doubleSelection.domain.PageResult;
import com.doubleSelection.doubleSelection.domain.StudentMentorSelection;
import com.doubleSelection.doubleSelection.domain.VO.SelectionListVO;
import com.doubleSelection.doubleSelection.domain.VO.SelectionResultVO;
import com.doubleSelection.doubleSelection.service.ISelectionService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/selection")
@Slf4j
@RequiredArgsConstructor
public class SelectionController {
    private final ISelectionService selectionService;

    /**
     * 学生选择导师
     * @param mentorId
     * @return
     */
@PostMapping("/{mentorId}")
@ApiOperation("学生选择导师")
    public AjaxResult studentSelectMentor(@PathVariable Long mentorId){
    selectionService.studentSelectMentor(mentorId);
    return AjaxResult.success();
}

    /**
     * 学生取消导师选择
     * @param mentorId
     * @return
     */
@DeleteMapping("/{mentorId}")
@ApiOperation("学生取消导师选择")
    public AjaxResult studentCancelSelectMentor(@PathVariable Long mentorId){
    selectionService.studentCancelSelectMentor(mentorId);
    return AjaxResult.success();
}

    /**
     * 分页查询选择当前导师的所有学生
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/{pageNum}/{pageSize}")
@ApiOperation("分页查询选择当前导师的所有学生")
public PageResult<SelectionListVO> list(@PathVariable Integer pageNum, @PathVariable Integer pageSize){
    return selectionService.list(pageNum,pageSize);
}
    /**
     * 导师同意学生申请，双选完成
     * @param studentId
     * @return
     */
@PutMapping("/doubleSelection/{studentId}")
@ApiOperation("导师同意学生申请，双选完成")
    public AjaxResult doubleSelection(@PathVariable Long studentId){
    selectionService.doubleSelection(studentId);
    return AjaxResult.success();
    }

    /**
     * 导师拒绝学生申请
     * @param studentId
     * @return
     */
    @PutMapping("/reject/{studentId}")
    @ApiOperation("导师拒绝学生申请")
    public AjaxResult reject(@PathVariable Long studentId){
    selectionService.reject(studentId);
    return AjaxResult.success();
    }

    /**
     * 获取当前用户的双选结果
     * @param pageDTO
     * @return
     */
    @GetMapping("/result")
    @ApiOperation("查询双选结果")
    public PageResult<SelectionResultVO> result(PageDTO pageDTO)
    {
        return selectionService.result(pageDTO);
    }
}
