package com.ysq.theTourGuide.service.redis.impl;

import com.alibaba.fastjson.JSON;
import com.ysq.theTourGuide.service.redis.IGeoService;
import com.ysq.theTourGuide.utils.Location;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class GeoServiceImpl implements IGeoService {


    /**
     * redis 客户端
     */
    private final StringRedisTemplate redisTemplate;

    @Autowired
    public GeoServiceImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @Override
    public Long saveLocationToRedis(Location location,String geoKey) {
        GeoOperations<String, String> ops = redisTemplate.opsForGeo();
        return ops.add(geoKey,new RedisGeoCommands.GeoLocation<String>(
                location.getId(), new Point(location.getLongitude(), location.getLatitude())));
    }

    @Override
    public Long saveLocationToRedis(Collection<Location> locations,String geoKey) {

        log.info("start to save location info: {}.", JSON.toJSONString(locations));

        GeoOperations<String, String> ops = redisTemplate.opsForGeo();

        Set<RedisGeoCommands.GeoLocation<String>> locationSet = new HashSet<>();
        locations.forEach(ci -> locationSet.add(new RedisGeoCommands.GeoLocation<String>(
                ci.getId(), new Point(ci.getLongitude(), ci.getLatitude())
        )));

        log.info("done to save location info.");

        return ops.add(geoKey, locationSet);
    }

    @Override
    public Point getLocationPos(String member,String geoKey) {

        GeoOperations<String, String> ops = redisTemplate.opsForGeo();
        String[] members = {member};
        return ops.position(geoKey, members).get(0);
    }

    @Override
    public List<Point> getLocationPos(String[] members,String geoKey) {

        GeoOperations<String, String> ops = redisTemplate.opsForGeo();

        return ops.position(geoKey, members);
    }

    @Override
    public Distance getTwoLocationDistance(String member1, String member2, Metric metric,String geoKey) {

        GeoOperations<String, String> ops = redisTemplate.opsForGeo();

        return metric == null ?
                ops.distance(geoKey, member1, member2) : ops.distance(geoKey, member1, member2, metric);
    }

    @Override
    public GeoResults<RedisGeoCommands.GeoLocation<String>> getPointRadius(
            Circle within, RedisGeoCommands.GeoRadiusCommandArgs args,String geoKey
    ) {

        GeoOperations<String, String> ops = redisTemplate.opsForGeo();

        return args == null ?
                ops.radius(geoKey, within) : ops.radius(geoKey, within, args);
    }

    @Override
    public GeoResults<RedisGeoCommands.GeoLocation<String>> getMemberRadius(
            String member, Distance distance, RedisGeoCommands.GeoRadiusCommandArgs args,String geoKey
    ) {

        GeoOperations<String, String> ops = redisTemplate.opsForGeo();

        return args == null ?
                ops.radius(geoKey, member, distance) : ops.radius(geoKey, member, distance, args);
    }

    @Override
    public List<String> getLocationGeoHash(String[] locations,String geoKey) {

        GeoOperations<String, String> ops = redisTemplate.opsForGeo();

        return ops.hash(geoKey, locations);
    }
}