package com.ysq.theTourGuide.mapper;

import com.ysq.theTourGuide.entity.Scenic;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ScenicMapper extends Mapper<Scenic> {

    @Select("select * from scenic where city = #{cityName} and name like CONCAT('%',#{attr},'%')")
    List<Scenic> findScenic(String cityName,String attr);

    @Select("select * from scenic where city = #{cityName} and name like CONCAT('%',#{attr},'%') or location like CONCAT('%',#{attr},'%')")
    List<Scenic> findScenicTwo(String cityName,String attr);

    @Select("select * from scenic where province like concat('%',#{attr},'%') or city like CONCAT('%',#{attr},'%') or name like CONCAT('%',#{attr},'%') or location like CONCAT('%',#{attr},'%')")
    List<Scenic> findScenicThree(String attr);
}