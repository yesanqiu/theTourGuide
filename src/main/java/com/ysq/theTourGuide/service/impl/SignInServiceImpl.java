package com.ysq.theTourGuide.service.impl;

import com.ysq.theTourGuide.base.service.impl.BaseServiceImpl;
import com.ysq.theTourGuide.entity.SignIn;
import com.ysq.theTourGuide.mapper.SignInMapper;
import com.ysq.theTourGuide.service.SignInService;
import org.springframework.stereotype.Service;

@Service
public class SignInServiceImpl extends BaseServiceImpl<SignInMapper, SignIn> implements SignInService {
}
