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
@NoArgsConstructor
@AllArgsConstructor
public class Route  implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;


    @Column(name = "video_id")
    private Long videoId;

    /**
     * 路线
     */
    private String line;

    /**
     * 时长_天数
     */
    private Integer rDay;

    /**
     * 时长_夜数
     */
    private Integer rNight;

    /**
     * 景点个数
     */
    private Integer noss;

    /**
     * 经典景点个数
     */
    private Integer nosss;

    /**
     * 是否购物
     */
    @Column(name = "h_shop")
    private Boolean hShop;

    /**
     * 语言
     */
    private String gLanguage;

    /**
     * 人数上限
     */
    @Column(name = "n_o_p")
    private Integer nOP;

    /**
     * 价格
     */
    private Double price;

    /**
     * 优惠类型id
     */
    @Column(name = "discount_type_id")
    private Integer discountTypeId;

    /**
     * 优惠额度
     */
    @Column(name = "discount_value")
    private Integer discountValue;

    /**
     * 描述
     */
    private String rDescribe;

    /**
     * 导游id
     */
    @Column(name = "guide_id")
    private Long guideId;

    public static final String ID = "id";

    public static final String LINE = "line";

    public static final String TIME = "time";

    public static final String NOSS = "noss";

    public static final String NOSSS = "nosss";

    public static final String H_SHOP = "hShop";

    public static final String G_LANGUAGE = "gLanguage";

    public static final String N_O_P = "nOP";

    public static final String PRICE = "price";

    public static final String DISCOUNT_TYPE_ID = "discountTypeId";

    public static final String DISCOUNT_VALUE = "discountValue";

    public static final String R_DESCRIBE = "rDescribe";

    public static final String GUIDE_ID = "guideId";

    public Route(Long guideId){
        this.guideId = guideId;
    }
}