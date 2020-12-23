package com.ysq.theTourGuide.service.impl;

import com.ysq.theTourGuide.base.service.impl.BaseServiceImpl;
import com.ysq.theTourGuide.entity.Route;
import com.ysq.theTourGuide.mapper.RouteMapper;
import com.ysq.theTourGuide.service.RouteService;
import org.springframework.stereotype.Service;

@Service
public class RouteServiceImpl extends BaseServiceImpl<RouteMapper, Route> implements RouteService {
}
