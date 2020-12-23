package com.ysq.theTourGuide.service.impl;

import com.ysq.theTourGuide.base.service.impl.BaseServiceImpl;
import com.ysq.theTourGuide.entity.Card;
import com.ysq.theTourGuide.mapper.CardMapper;
import com.ysq.theTourGuide.service.CardService;
import org.springframework.stereotype.Service;

@Service
public class CardServiceImpl extends BaseServiceImpl<CardMapper, Card> implements CardService {
}
