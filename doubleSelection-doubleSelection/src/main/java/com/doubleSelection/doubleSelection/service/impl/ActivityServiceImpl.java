package com.doubleSelection.doubleSelection.service.impl;

import com.doubleSelection.common.core.domain.AjaxResult;
import com.doubleSelection.common.core.page.PageDomain;
import com.doubleSelection.doubleSelection.domain.Activity;
import com.doubleSelection.doubleSelection.domain.DTO.ActivityDTO;
import com.doubleSelection.doubleSelection.domain.PageResult;
import com.doubleSelection.doubleSelection.mapper.ActivityMapper;
import com.doubleSelection.doubleSelection.service.IActivityService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements IActivityService {
    private final ActivityMapper activityMapper;
    @Override
    public PageResult<Activity> list(ActivityDTO activityDTO) {
        PageHelper.startPage(activityDTO.getPage(), activityDTO.getPageSize());
        List<Activity> list=activityMapper.list(activityDTO);
        PageInfo<Activity> page1 = new PageInfo<>(list);
        PageResult<Activity> result = new PageResult<>();
        result.setTotal(page1.getTotal());
        result.setPageNum(page1.getPageNum());
        result.setPageSize(page1.getPageSize());
        result.setPages(page1.getPages());
        result.setRecords(page1.getList());
        return result;

    }

    @Override
    public void insertActivity(Activity activity) {
        List<Activity> activities=activityMapper.selectConflictActivity(activity);
        if(!activities.isEmpty())
        {
            throw new RuntimeException("活动时间冲突");
        }
        List<Activity> list=activityMapper.listAll();
        List<Activity> collect = list.stream().filter(item -> item.getName().equals(activity.getName())).collect(Collectors.toList());
        if(!collect.isEmpty())
        {
            throw new RuntimeException("活动名称重复");
        }
        activityMapper.insert(activity);
    }

    @Override
    public Activity current() {
        LocalDate now = LocalDate.now();
        List<Activity> list=activityMapper.getCurrent(now);
        if(list.isEmpty())
        {
            return null;
        }
        return list.get(0);
    }
}
