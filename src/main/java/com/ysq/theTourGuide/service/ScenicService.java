package com.ysq.theTourGuide.service;

import com.ysq.theTourGuide.base.service.BaseService;
import com.ysq.theTourGuide.entity.Scenic;

import java.util.List;

public interface ScenicService extends BaseService<Scenic> {

    List<Scenic> findScenic(String cityName,String attr);

    List<Scenic> findScenicTwo(String cityName,String attr);

    List<Scenic> findScenicThree(String attr);
}
