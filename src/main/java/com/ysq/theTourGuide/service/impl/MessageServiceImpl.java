package com.ysq.theTourGuide.service.impl;

import com.ysq.theTourGuide.base.service.impl.BaseServiceImpl;
import com.ysq.theTourGuide.entity.Message;
import com.ysq.theTourGuide.mapper.MessageMapper;
import com.ysq.theTourGuide.service.MessageService;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl extends BaseServiceImpl<MessageMapper, Message> implements MessageService {
}
