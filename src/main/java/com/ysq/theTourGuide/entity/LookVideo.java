package com.ysq.theTourGuide.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "look_video")
public class LookVideo  implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    /**
     * 游客id
     */
    @Column(name = "tourist_id")
    private Long touristId;

    /**
     * 视频id
     */
    @Column(name = "video_id")
    private Long videoId;

    /**
     * 时长
     */
    private Long time;

    /**
     * 时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date date;

    public static final String ID = "id";

    public static final String TOURIST_ID = "touristId";

    public static final String VIDEO_ID = "videoId";

    public static final String TIME = "time";

    public static final String DATE = "date";


    public LookVideo(Long touristId,Long videoId,Long time){
        this.touristId = touristId;
        this.videoId = videoId;
        this.time = time;
        this.date = new Date();
    }
}