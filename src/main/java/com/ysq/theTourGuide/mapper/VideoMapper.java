package com.ysq.theTourGuide.mapper;

import com.ysq.theTourGuide.entity.Video;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface VideoMapper extends Mapper<Video> {



    @Select("select count(*) from video")
    Integer getCounts();

    @Select(" select * from video where province like CONCAT('%',#{attr},'%') or city like CONCAT('%',#{attr},'%')")
    @ResultMap("BaseResultMap")
    List<Video> findVideoByLocation(String attr);

}