package com.ysq.theTourGuide.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vip  implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "vip_id")
    private Long vipId;

    /**
     * VIP等级
     */
    @Column(name = "vip_level")
    private String vipLevel;

    /**
     * VIP期限
     */
    @Column(name = "vip_time")
    private Date vipTime;

    /**
     * VIP期限
     */
    @Column(name = "vip_date")
    private Integer vipDate;

    /**
     * VIP成长值
     */
    @Column(name = "vip_score")
    private Integer vipScore;

    /**
     * 游客id
     */
    @Column(name = "tourist_id")
    private Long touristId;

    /**
     * 自动续费
     */
    @Column(name = "auto_renewal")
    private Boolean autoRenewal;


    public static final String VIP_ID = "vipId";

    public static final String VIP_LEVEL = "vipLevel";

    public static final String VIP_TIME = "vipTime";

    public static final String VIP_DATE = "vipDate";

    public static final String VIP_SCORE = "vipScore";

    public static final String TOURIST_ID = "touristId";

    public static final String AUTO_RENEWAL = "autoRenewal";


    public Vip(Integer date,Long touristId){
        this.vipDate = date;
        this.touristId = touristId;
        this.vipLevel = "v1";
        this.vipScore = 0;
        this.autoRenewal = false;
    }

    public Vip(Long touristId){
        this.touristId = touristId;
    }

    public Vip(Long vipId,Integer score){
        this.vipId = vipId;
        this.vipScore = score;
    }

    public Vip(Long vipId,String vipLevel,Integer score){
        this.vipId = vipId;
        this.vipLevel = vipLevel;
        this.vipScore = score;
    }

}