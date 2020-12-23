package com.ysq.theTourGuide.mapper;

import com.ysq.theTourGuide.entity.LookVideo;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface LookVideoMapper extends Mapper<LookVideo> {

    @Select("SELECT * FROM `look_video` WHERE TO_DAYS(date) = TO_DAYS(NOW()) and tourist_id = #{touristId}")
    List<LookVideo> findToday(Long touristId);
}