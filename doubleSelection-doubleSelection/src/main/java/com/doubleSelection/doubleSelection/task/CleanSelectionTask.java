package com.doubleSelection.doubleSelection.task;

import com.doubleSelection.doubleSelection.domain.Activity;
import com.doubleSelection.doubleSelection.service.IActivityService;
import com.doubleSelection.doubleSelection.service.ISelectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Objects;

@RequiredArgsConstructor
public class CleanSelectionTask {
    private final IActivityService activityService;
    private final ISelectionService selectionService;
    /**
     * 每天0点检索活动是否结束，活动结束的话删除未成功的选择记录，清除垃圾
     */
    @Scheduled(cron = "0 0 0 1/1 * ? *")
    public void deleteFailSelection(){
        Activity current = activityService.current();
        if(Objects.nonNull(current))
        {
            return ;
        }
        selectionService.deleteFailSelection();
    }

}
