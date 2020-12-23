package com.ysq.theTourGuide.service.impl;

import com.ysq.theTourGuide.base.service.impl.BaseServiceImpl;
import com.ysq.theTourGuide.entity.Guide;
import com.ysq.theTourGuide.mapper.GuideMapper;
import com.ysq.theTourGuide.service.GuideService;
import org.springframework.stereotype.Service;

@Service
public class GuideServiceImpl extends BaseServiceImpl<GuideMapper, Guide> implements GuideService {
}
