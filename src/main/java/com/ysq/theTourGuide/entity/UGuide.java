package com.ysq.theTourGuide.entity;

import com.ysq.theTourGuide.dto.GuideResiterDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "u_guide")
@AllArgsConstructor
@NoArgsConstructor
public class UGuide extends Guide {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    /**
     * 导游对应的游客登录id
     */
    @Column(name = "tourist_id")
    private Long touristId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 电话
     */
    private String phone;

    /**
     * 导游证
     */
    @Column(name = "tourist_certificate_url")
    private String touristCertificateUrl;

    /**
     * 等级
     */
    private Integer level;

    /**
     * 导游证号
     */
    @Column(name = "the_guide_number")
    private String theGuideNumber;

    /**
     * 所在机构
     */
    private String organization;

    /**
     * 期限
     */
    private String date;

    /**
     * 评分
     */
    private Double grade;

    /**
     * 导游年份
     */
    private String years;

    /**
     * 获赞次数
     */
    @Column(name = "like_nums")
    private Integer likeNums;

    /**
     * 申请时间
     */
    private Date time;

    /**
     * 导游语言
     */
    @Column(name = "g_language")
    private String gLanguage;

    /**
     * 状态（0为待审核，1通过，2不通过,3信息修改）
     */
    private Integer state;

    public static final String ID = "id";

    public static final String TOURIST_ID = "touristId";

    public static final String NAME = "name";

    public static final String PHONE = "phone";

    public static final String TOURIST_CERTIFICATE_URL = "touristCertificateUrl";

    public static final String LEVEL = "level";

    public static final String THE_GUIDE_NUMBER = "theGuideNumber";

    public static final String ORGANIZATION = "organization";

    public static final String DATE = "date";

    public static final String GRADE = "grade";

    public static final String YEARS = "years";

    public static final String LIKE_NUMS = "likeNums";

    public static final String TIME = "time";

    public static final String G_LANGUAGE = "gLanguage";

    public static final String STATE = "state";

    public UGuide(GuideResiterDTO g){
        this.touristId = g.getTouristId();
        this.name = g.getName();
        this.phone = g.getPhone();
        this.touristCertificateUrl = g.getTouristCertificateUrl();
        this.level = g.getLevel();
        this.theGuideNumber = g.getGuide_number();
        this.organization = g.getOrganization();
        this.date = g.getDate();
        this.gLanguage = g.getLanguage();
        this.likeNums = 0;
        this.grade = 0.00;
        this.years = "0";
        this.time = new Date();
    }

    public UGuide(Long touristId){
        this.touristId = touristId;
    }
}