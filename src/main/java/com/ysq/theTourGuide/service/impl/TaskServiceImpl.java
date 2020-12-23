package com.ysq.theTourGuide.service.impl;

import com.ysq.theTourGuide.base.service.impl.BaseServiceImpl;
import com.ysq.theTourGuide.config.TaskEnum;
import com.ysq.theTourGuide.entity.Task;
import com.ysq.theTourGuide.mapper.TaskMapper;
import com.ysq.theTourGuide.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl extends BaseServiceImpl<TaskMapper, Task> implements TaskService {

    @Autowired
    TaskMapper taskMapper;

    @Override
    public int findToday(Long touristId, TaskEnum taskEnum) {
        return taskMapper.findToday(touristId,taskEnum.getTitle());
    }

    @Override
    public int findThisMonth(Long touristId, String title) {
        return taskMapper.findThisMonth(touristId,title);
    }
}
