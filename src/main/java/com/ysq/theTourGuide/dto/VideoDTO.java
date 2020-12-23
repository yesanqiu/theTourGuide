package com.ysq.theTourGuide.dto;

import com.ysq.theTourGuide.entity.Video;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoDTO {

    private Video video;
    private Double distance;
    private String guide_avatar_url;
    private Integer guide_level;
    private boolean is_like;
    private Integer comment_counts;
    private Integer likeNums;
    private String guide_name;
    private String cityName;
    private Double longitude;
    private Double latitude;
    private Double grade;
    private Long guideId;
    private Boolean is_fans;
}
