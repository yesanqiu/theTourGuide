package com.ysq.theTourGuide.service.redis;

import com.ysq.theTourGuide.utils.Location;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;

import java.util.Collection;
import java.util.List;

/**
 * @author 叶三秋
 * @date 2020/2/15
 */
public interface IGeoService {

    /**
     * 存储地理位置信息
     * @param location
     * @param geoKey
     * @return
     */
    Long saveLocationToRedis(Location location,String geoKey);

    /**
     * 存储地理位置信息
     * @param locations
     * @param geoKey
     * @return
     */
    Long saveLocationToRedis(Collection<Location> locations,String geoKey);

    /**
     * 获取给定成员的坐标
     * @param members
     * @param geoKey
     * @return
     */
    Point getLocationPos(String members,String geoKey);


    /**
     * 获取给定成员的坐标
     * @param members
     * @param geoKey
     * @return
     */
    List<Point> getLocationPos(String[] members,String geoKey);

    /**
     * 获取两个成员之间的距离
     * @param member1
     * @param member2
     * @param metric
     * @param geoKey
     * @return
     */
    Distance getTwoLocationDistance(String member1, String member2, Metric metric,String geoKey);

    /**
     * 根据给定地理位置坐标获取指定范围内的地理位置集合
     * @param within
     * @param args
     * @param geoKey
     * @return
     */
    GeoResults<RedisGeoCommands.GeoLocation<String>> getPointRadius(
            Circle within, RedisGeoCommands.GeoRadiusCommandArgs args,String geoKey);

    /**
     * 根据给定成员地理位置获取指定范围内的地理位置集合
     * */
    GeoResults<RedisGeoCommands.GeoLocation<String>> getMemberRadius(
            String member, Distance distance, RedisGeoCommands.GeoRadiusCommandArgs args,String geoKey);

    /**
     * 获取某个地理位置的 geohash 值
     * @param locations 给定id
     * @param geoKey
     * @return city geohashs
     * */
    List<String> getLocationGeoHash(String[] locations,String geoKey);
}
