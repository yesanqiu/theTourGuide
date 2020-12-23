package com.ysq.theTourGuide.service.redis.impl;

import com.ysq.theTourGuide.service.redis.GuideGeoService;
import com.ysq.theTourGuide.service.redis.IGeoService;
import com.ysq.theTourGuide.utils.Location;
import com.ysq.theTourGuide.utils.MyMathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GuideGeoServiceImpl implements GuideGeoService {

    private final static String GEO_KEY = "guide_locations";

    @Value("${geo.guide.within}")
    private int within;

    @Autowired
    private IGeoService geoService;

    @Override
    public Long saveGuideLocation(Location location) {
        return geoService.saveLocationToRedis(location,GEO_KEY);
    }

    @Override
    public Point getGuideLocation(String guideId) {
        return geoService.getLocationPos(guideId,GEO_KEY);
    }

    @Override
    public List<Point> getGuideLocation(String[] guideIds) {
        return geoService.getLocationPos(guideIds,GEO_KEY);
    }

    @Override
    public List<String> getNearByGuideList(Point point) {
        List<String> nameList = new ArrayList<>();
        for(GeoResult<RedisGeoCommands.GeoLocation<String>> geoResult:
                geoService.getPointRadius (new Circle(point, new Distance(within,Metrics.KILOMETERS)),null,GEO_KEY))
        {
            nameList.add(geoResult.getContent().getName());
        }
        return nameList;
    }

    @Override
    public Double getGuideDistance(Point point,String guideId) {
        return MyMathUtil.getTwoPointDist(getGuideLocation(guideId),point);
    }
}
