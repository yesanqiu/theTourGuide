package com.ysq.theTourGuide.service.impl;

import com.ysq.theTourGuide.base.service.impl.BaseServiceImpl;
import com.ysq.theTourGuide.entity.Scenic;
import com.ysq.theTourGuide.mapper.ScenicMapper;
import com.ysq.theTourGuide.service.ScenicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScenicServiceImpl extends BaseServiceImpl<ScenicMapper, Scenic> implements ScenicService {

    @Autowired
    ScenicMapper scenicMapper;
    @Override
    public List<Scenic> findScenic(String cityName,String attr) {
        return scenicMapper.findScenic(cityName,attr);
    }

    @Override
    public List<Scenic> findScenicTwo(String cityName, String attr) {
        return scenicMapper.findScenicTwo(cityName,attr);
    }

    @Override
    public List<Scenic> findScenicThree(String attr) {
        return scenicMapper.findScenicThree(attr);
    }
}
