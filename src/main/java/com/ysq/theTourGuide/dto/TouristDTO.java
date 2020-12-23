package com.ysq.theTourGuide.dto;

import com.ysq.theTourGuide.entity.Tourist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TouristDTO {
    private Long id;


    /**
     * 昵称
     */
    private String nickname;

    /**
     * 性别
     */
    private String gender;

    /**
     * 头像
     */
    private String avatarUrl;

    /**
     * 积分
     */
    private Integer score;

    /**
     * 城市
     */
    private String city;

    /**
     * 省份
     */
    private String province;

    /**
     * 国家
     */
    private String country;

    /**
     * 是否会员
     */
    private Boolean isVip;

    /**
     * 是否导游
     */
    private Boolean isGuide;


    public TouristDTO(Tourist t){
        this.id = t.getId();
        this.nickname = t.getNickname();
        this.gender = t.getGender();
        this.avatarUrl = t.getAvatarUrl();
        this.score = t.getScore();
        this.city = t.getCity();
        this.province = t.getProvince();
        this.country = t.getCountry();
        this.isVip = t.getIsVip();
        this.isGuide = t.getIsGuide();
    }
}
