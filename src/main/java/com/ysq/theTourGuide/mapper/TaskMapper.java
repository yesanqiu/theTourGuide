package com.ysq.theTourGuide.mapper;

import com.ysq.theTourGuide.entity.Task;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

public interface TaskMapper extends Mapper<Task> {

    @Select("SELECT Count(*) FROM `task` WHERE TO_DAYS(t_time) = TO_DAYS(NOW()) and tourist_id = #{touristId} and t_title = #{title};")
    int findToday(Long touristId,String title);

    @Select("SELECT count(*) FROM task WHERE DATE_FORMAT( t_time, '%Y%m' ) = DATE_FORMAT( CURDATE( ) , '%Y%m' ) and t_title = #{title} and tourist_id = #{touristId};")
    int findThisMonth(Long touristId,String title);
}