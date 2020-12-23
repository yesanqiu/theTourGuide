package com.ysq.theTourGuide.service.impl;

import com.ysq.theTourGuide.base.service.impl.BaseServiceImpl;
import com.ysq.theTourGuide.entity.Fans;
import com.ysq.theTourGuide.mapper.FansMapper;
import com.ysq.theTourGuide.service.FansService;
import org.springframework.stereotype.Service;

@Service
public class FansServiceImpl extends BaseServiceImpl<FansMapper,Fans> implements FansService {
}
