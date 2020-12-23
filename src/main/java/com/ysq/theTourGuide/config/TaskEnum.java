package com.ysq.theTourGuide.config;

public enum TaskEnum {
    SIGN_IN("任务-签到奖励",10),
    SIGN_IN_SEVEN("任务-签到满七天奖励",10),
    DAY_VIDEO("任务-每日视频",10),
    ORDER("任务-预约加速",10),
    ORDER_SEVEN("任务-预约加速满七次",10),
    LIKE_VIDEO("任务-点赞视频",10),
    YEAR_VIP("任务-开通年费会员",200),
    AUTO_RENEWAL("任务-开通自动续费",100),
    FIRST_VIP("首次开通会员",50),
    LEVEL_ONE_TO_TWO("V1升级V2",30),
    LEVEL_TWO_TO_THREE("V2升级V3",50),
    LEVEL_THREE_TO_FOUR("V3升级V4",80),
    LEVEL_FOUR_TO_FIVE("V4升级V5",100),
    ;


    private String title;
    private Integer score;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    private TaskEnum(String title, Integer score){
        this.title = title;
        this.score = score;
    }
}
