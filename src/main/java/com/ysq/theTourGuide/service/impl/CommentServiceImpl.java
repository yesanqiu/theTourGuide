package com.ysq.theTourGuide.service.impl;

import com.ysq.theTourGuide.base.service.impl.BaseServiceImpl;
import com.ysq.theTourGuide.entity.Comment;
import com.ysq.theTourGuide.mapper.CommentMapper;
import com.ysq.theTourGuide.service.CommentService;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl extends BaseServiceImpl<CommentMapper, Comment> implements CommentService {
}
