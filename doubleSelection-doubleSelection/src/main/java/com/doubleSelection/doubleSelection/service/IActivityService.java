package com.doubleSelection.doubleSelection.service;

import com.doubleSelection.common.core.domain.AjaxResult;
import com.doubleSelection.doubleSelection.domain.Activity;
import com.doubleSelection.doubleSelection.domain.DTO.ActivityDTO;
import com.doubleSelection.doubleSelection.domain.PageResult;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


public interface IActivityService {
    PageResult<Activity> list(ActivityDTO activityDTO);


    void insertActivity(Activity activity);

    Activity current();
}
