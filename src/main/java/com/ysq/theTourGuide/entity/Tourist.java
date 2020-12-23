package com.ysq.theTourGuide.entity;

import com.ysq.theTourGuide.dto.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tourist  implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "open_id")
    private String openId;

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
    @Column(name = "avatar_url")
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
    @Column(name = "is_VIP")
    private Boolean isVip;

    /**
     * 是否导游
     */
    @Column(name = "is_guide")
    private Boolean isGuide;

    public static final String OPEN_ID = "openId";

    public static final String NICKNAME = "nickname";

    public static final String GENDER = "gender";

    public static final String AVATAR_URL = "avatarUrl";

    public static final String SCORE = "score";

    public static final String CITY = "city";

    public static final String PROVINCE = "province";

    public static final String COUNTRY = "country";

    public static final String IS_VIP = "isVip";

    public static final String IS_GUIDE = "isGuide";

    public Tourist(Long id,UserInfo userInfo)throws Exception{
        this.id = id;
        this.openId = userInfo.getOpenId();
        this.nickname = userInfo.getNickName();
        this.avatarUrl = userInfo.getAvatarUrl();
        this.gender = userInfo.getGender();
        this.province = userInfo.getProvince();
        this.city = userInfo.getCity();
        this.country = userInfo.getCountry();
        this.score = 0;
        this.isGuide = false;
        this.isVip = false;
    }

    public Tourist(UserInfo userInfo)throws Exception{
        this.openId = userInfo.getOpenId();
        this.nickname = userInfo.getNickName();
        this.avatarUrl = userInfo.getAvatarUrl();
        this.gender = userInfo.getGender();
        this.province = userInfo.getProvince();
        this.city = userInfo.getCity();
        this.country = userInfo.getCountry();
        this.score = 0;
        this.isGuide = false;
        this.isVip = false;
    }

    public Tourist(String openId){
        this.openId = openId;
    }


    public Tourist(Long id ,Boolean isGuide){
        this.id = id;
        this.isGuide = isGuide;
    }

}