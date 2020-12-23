package com.ysq.theTourGuide.service;

import com.ysq.theTourGuide.base.service.BaseService;
import com.ysq.theTourGuide.entity.LookVideo;

import java.util.List;

public interface LookVideoService extends BaseService<LookVideo> {

    List<LookVideo> findToday(Long touristId);
}
