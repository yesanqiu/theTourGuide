package com.ysq.theTourGuide.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Video implements Serializable {
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
     * 景区Id
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

    /**
     * 经度
     */
    private Double longitude;

    /**
     * 维度
     */
    private Double latitude;

    /**
     * 城市
     */
    private String city;

    /**
     * 省份
     */
    private String province;

    public static final String ID = "id";

    public static final String GUIDE_ID = "guideId";

    public static final String ROUTE_ID = "routeId";

    public static final String SCENIC_ID = "scenicId";

    public static final String VIDEO_URL = "videoUrl";

    public static final String LIKE_NUMS = "likeNums";

    public static final String V_DESCRIBE = "vDescribe";

    public static final String DATE = "date";

    public static final String LONGITUDE = "longitude";

    public static final String LATITUDE = "latitude";

    public static final String CITY = "city";

    public static final String PROVINCE = "province";

    public Video(Long guideId){
        this.guideId = guideId;
    }

    public Video(Long id,Integer likeNums){
        this.id = id;
        this.likeNums = likeNums;
    }
}