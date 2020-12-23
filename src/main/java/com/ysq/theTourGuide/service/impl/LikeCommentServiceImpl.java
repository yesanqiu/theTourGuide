package com.ysq.theTourGuide.service.impl;

import com.ysq.theTourGuide.base.service.impl.BaseServiceImpl;
import com.ysq.theTourGuide.entity.LikeComment;
import com.ysq.theTourGuide.mapper.LikeCommentMapper;
import com.ysq.theTourGuide.service.LikeCommentService;
import org.springframework.stereotype.Service;

@Service
public class LikeCommentServiceImpl extends BaseServiceImpl<LikeCommentMapper, LikeComment> implements LikeCommentService {
}
