package com.ysq.theTourGuide.service.impl;

import com.ysq.theTourGuide.base.service.impl.BaseServiceImpl;
import com.ysq.theTourGuide.entity.TheOrder;
import com.ysq.theTourGuide.mapper.OrderMapper;
import com.ysq.theTourGuide.service.TheOrderService;
import org.springframework.stereotype.Service;

@Service
public class TheOrderServiceImpl extends BaseServiceImpl<OrderMapper, TheOrder> implements TheOrderService {
}
