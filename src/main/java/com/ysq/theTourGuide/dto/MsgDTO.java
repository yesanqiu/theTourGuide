package com.ysq.theTourGuide.dto;

import com.ysq.theTourGuide.entity.Route;
import com.ysq.theTourGuide.entity.Video;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MsgDTO {

    private Route route;
    private Video video;
}
