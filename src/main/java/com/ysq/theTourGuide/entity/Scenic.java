package com.ysq.theTourGuide.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Scenic  implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    /**
     * 景区名字
     */
    private String name;

    /**
     * 详细位置
     */
    private String location;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 经度
     */
    private Double longitude;

    /**
     * 纬度
     */
    private Double latitude;

    /**
     * 开放时间
     */
    @Column(name = "opening_time")
    private String openingTime;

    /**
     * 优惠政策
     */
    private String policy;

    /**
     * 特色玩法
     */
    private String feature;

    /**
     * 温馨提示
     */
    @Column(name = "warm_tip")
    private String warmTip;

    /**
     * 管理员id
     */
    @Column(name = "administrator_id")
    private Integer administratorId;

    /**
     * 图片
     */
    @Column(name = "pic_url")
    private String picUrl;

    @Column(name = "pic_url_2")
    private String picUrl2;

    @Column(name = "pic_url_3")
    private String picUrl3;

    public static final String ID = "id";

    public static final String NAME = "name";

    public static final String LOCATION = "location";

    public static final String PROVINCE = "province";

    public static final String CITY = "city";

    public static final String LONGITUDE = "longitude";

    public static final String LATITUDE = "latitude";

    public static final String OPENING_TIME = "openingTime";

    public static final String POLICY = "policy";

    public static final String FEATURE = "feature";

    public static final String WARM_TIP = "warmTip";

    public static final String ADMINISTRATOR_ID = "administratorId";

    public static final String PIC_URL = "picUrl";

    public static final String PIC_URL_2 = "picUrl2";

    public static final String PIC_URL_3 = "picUrl3";

    public Scenic(String city){
        this.city = city;
    }

    public Scenic(Integer administratorId){
        this.administratorId = administratorId;
    }
}