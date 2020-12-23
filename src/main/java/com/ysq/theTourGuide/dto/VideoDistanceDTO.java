package com.ysq.theTourGuide.dto;

import com.ysq.theTourGuide.entity.Video;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoDistanceDTO {

    private Video video;

    private Double distance;
}
