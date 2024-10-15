package com.doubleSelection.doubleSelection.mapper;

import com.doubleSelection.doubleSelection.domain.Activity;
import com.doubleSelection.doubleSelection.domain.DTO.ActivityDTO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ActivityMapper {
    List<Activity> list(ActivityDTO activityDTO);

    List<Activity> selectConflictActivity(Activity activity);

    void insert(Activity activity);

    List<Activity> listAll();

    List<Activity> getCurrent(LocalDate now);
}
