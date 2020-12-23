package com.ysq.theTourGuide.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ysq.theTourGuide.entity.Guide;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoANDLevel {


    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    /**
     * 导游id
     */
    @Column(name = "guide_id")
    private Long guideId;

    /**
     * 路线id
     */
    @Column(name = "route_id")
    private Long routeId;


    /**
     * 景区id
     */
    @Column(name = "scenic_id")
    private Long scenicId;

    /**
     * 视频地址
     */
    @Column(name = "video_url")
    private String videoUrl;

    /**
     * 点赞数
     */
    @Column(name = "like_nums")
    private Integer likeNums;

    /**
     * 描述
     */
    @Column(name = "v_describe")
    private String vDescribe;


    /**
     * 时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date date;

    private Guide guide;
}
