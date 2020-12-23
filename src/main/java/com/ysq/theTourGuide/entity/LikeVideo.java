package com.ysq.theTourGuide.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "like_video")
@NoArgsConstructor
@AllArgsConstructor
public class LikeVideo  implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    /**
     * 导游id
     */
    @Column(name = "tourist_id")
    private Long touristId;

    /**
     * 喜欢的视频id
     */
    @Column(name = "like_video_id")
    private Long likeVideoId;

    public static final String ID = "id";

    public static final String TOURIST_ID = "touristId";

    public static final String LIKE_VIDEO_ID = "likeVideoId";

    public LikeVideo(Long videoId, Long touristId){
        this.likeVideoId = videoId;
        this.touristId = touristId;
    }

    public LikeVideo(Long videoId){
        this.likeVideoId = videoId;
    }
}