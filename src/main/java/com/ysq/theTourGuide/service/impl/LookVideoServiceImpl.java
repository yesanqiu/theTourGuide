package com.ysq.theTourGuide.service.impl;

import com.ysq.theTourGuide.base.service.impl.BaseServiceImpl;
import com.ysq.theTourGuide.entity.LookVideo;
import com.ysq.theTourGuide.mapper.LookVideoMapper;
import com.ysq.theTourGuide.service.LookVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LookVideoServiceImpl extends BaseServiceImpl<LookVideoMapper, LookVideo> implements LookVideoService {

    @Autowired
    LookVideoMapper lookVideoMapper;
    @Override
    public List<LookVideo> findToday(Long touristId) {
        return lookVideoMapper.findToday(touristId);
    }
}
