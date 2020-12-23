package com.ysq.theTourGuide.service.redis;

import com.ysq.theTourGuide.utils.Location;
import org.springframework.data.geo.Point;

import java.util.List;

public interface GuideGeoService  {

    Long saveGuideLocation(Location location);

    Point getGuideLocation(String guideId);

    List<Point> getGuideLocation(String[] guideIds);

    List<String> getNearByGuideList(Point point);

    Double getGuideDistance(Point point,String guideId);
}
