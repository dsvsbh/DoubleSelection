package com.doubleSelection.doubleSelection.controller;

import com.doubleSelection.common.core.controller.BaseController;
import com.doubleSelection.common.core.domain.AjaxResult;
import com.doubleSelection.doubleSelection.domain.Activity;
import com.doubleSelection.doubleSelection.domain.DTO.ActivityDTO;
import com.doubleSelection.doubleSelection.domain.PageResult;
import com.doubleSelection.doubleSelection.service.IActivityService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/activity")
@RequiredArgsConstructor
public class ActivityController {
    private final IActivityService activityService;
    /**
     * 分页查询活动列表
     * @return
     */
    @GetMapping
    @ApiOperation(value = "分页查询活动列表")
    public PageResult<Activity> list(ActivityDTO activityDTO) {
        return activityService.list(activityDTO);
    }

    /**
     * 添加活动
     * @param activity
     * @return
     */
    @PostMapping
    @ApiOperation(value = "添加活动")
    private AjaxResult add(@RequestBody Activity activity) {
        activityService.insertActivity(activity);
        return AjaxResult.success();
    }

    /**
     * 查看当前活动
     * @return
     */
    @ApiOperation("查询当前活动")
    @GetMapping("/current")
    public AjaxResult current() {
        return AjaxResult.success(activityService.current());
    }
}
