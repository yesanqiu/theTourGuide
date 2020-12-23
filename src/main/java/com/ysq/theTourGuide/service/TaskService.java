package com.ysq.theTourGuide.service;

import com.ysq.theTourGuide.base.service.BaseService;
import com.ysq.theTourGuide.config.TaskEnum;
import com.ysq.theTourGuide.entity.Task;

public interface TaskService extends BaseService<Task> {

    int findToday(Long touristId, TaskEnum taskEnum);

    int findThisMonth(Long touristId,String title);
}
