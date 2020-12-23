package com.ysq.theTourGuide.service;

import com.ysq.theTourGuide.base.service.BaseService;
import com.ysq.theTourGuide.entity.Video;

import java.util.List;

public interface VideoService extends BaseService<Video> {

    List<Video> findAllVideoByDistance()throws Exception;

    List<Video> findAllVideoByLevel() throws Exception;

    List<Video> findAllVideoByGoodNums() throws Exception;

    Integer getCounts() throws Exception;

    List<Video> findVideoByLocation(String attr);
}
