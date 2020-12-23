package com.ysq.theTourGuide.dto;

import com.ysq.theTourGuide.entity.Guide;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GuideMsgDTO {

    private Guide guide;

    private Integer guideLevel;

    private Integer guideLevelScore;

    private Long videoNums;

    private Integer videoNumsScore;

    private Long orderNums;

    private Integer orderNumsScore;

    private Integer likeNums;

    private Integer likeNumsScore;
}
