package com.ysq.theTourGuide.service.impl;

import com.ysq.theTourGuide.base.service.impl.BaseServiceImpl;
import com.ysq.theTourGuide.entity.Administrator;
import com.ysq.theTourGuide.mapper.AdministratorMapper;
import com.ysq.theTourGuide.service.AdministratorService;
import org.springframework.stereotype.Service;

@Service
public class AdministratorServiceImpl extends BaseServiceImpl<AdministratorMapper, Administrator> implements AdministratorService {
}
