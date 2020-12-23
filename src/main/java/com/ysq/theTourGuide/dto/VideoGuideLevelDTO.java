package com.ysq.theTourGuide.dto;

import com.ysq.theTourGuide.entity.Video;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoGuideLevelDTO {

    private Video video;

    private Integer guideLevel;
}
