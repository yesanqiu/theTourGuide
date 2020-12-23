package com.ysq.theTourGuide.service.impl;

import com.ysq.theTourGuide.base.service.impl.BaseServiceImpl;
import com.ysq.theTourGuide.entity.Video;
import com.ysq.theTourGuide.mapper.VideoMapper;
import com.ysq.theTourGuide.service.GuideService;
import com.ysq.theTourGuide.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VideoServiceImpl extends BaseServiceImpl<VideoMapper, Video> implements VideoService {

    @Autowired
    GuideService guideService;

    @Autowired
    VideoMapper videoMapper;


    @Override
    public List<Video> findAllVideoByDistance() throws Exception{
        List<Video> videoList = new ArrayList<>();
        videoList = this.findAll();
        return null;
    }

    @Override
    public List<Video> findAllVideoByLevel() throws Exception {
        List<Video> videoList = new ArrayList<>();
        videoList = this.findAll();
        for(Video v: videoList){
            guideService.get(v.getGuideId());
        }
        return videoList;
    }

    @Override
    public List<Video> findAllVideoByGoodNums() throws Exception {
        List<Video> videoList = new ArrayList<>();
        videoList = this.findAll();
//        List<Task> taskList = new ArrayList<>();
//        taskList.add(new Task(result.getInt(1),result.getString(2),result.getString(3)));
        return videoList;
    }



    @Override
    public Integer getCounts() throws Exception {
        return videoMapper.getCounts();
    }

    @Override
    public List<Video> findVideoByLocation(String attr){
        return videoMapper.findVideoByLocation(attr);
    }
}
