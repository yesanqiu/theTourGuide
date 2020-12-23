package com.ysq.theTourGuide.dto;

import com.ysq.theTourGuide.entity.Guide;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GuideAndAvataturl {

    private Guide guide;
    private String avatarUrl;
}
