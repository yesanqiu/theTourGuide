package com.ysq.theTourGuide.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TheOrder  implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    /**
     * 游客id
     */
    @Column(name = "tourist_id")
    private Long touristId;

    /**
     * 路线id
     */
    @Column(name = "route_id")
    private Long routeId;

    /**
     * 导游id
     */
    @Column(name = "guide_id")
    private Long guideId;

    /**
     * 出发点
     */
    @Column(name = "t_start")
    private String tStart;

    /**
     * 人数
     */
    @Column(name = "n_o_p")
    private Integer nOP;

    /**
     * 预定时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date time;

    /**
     * 碰面时间
     */
    @Column(name = "meet_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date meetTime;

    /**
     * 订单状态（222为待出行，111为已完成，333为取消）
     */
    private String state;

    /**
     * 姓名
     */
    @Column(name = "t_name")
    private String tName;

    /**
     * 身份证号
     */
    @Column(name = "ID_number")
    private String idNumber;

    /**
     * 电话
     */
    private String phone;


    /**
     * 订单成交时的价格
     */
    private Double price;

    /**
     * 卡卷id
     */
    private Long cardId;

    public static final String ID = "id";

    public static final String TOURIST_ID = "touristId";

    public static final String TITLE = "title";

    public static final String GUIDE_ID = "guideId";

    public static final String T_START = "tStart";

    public static final String N_O_P = "nOP";

    public static final String TIME = "time";

    public static final String MEET_TIME = "meetTime";

    public static final String STATE = "state";

    public static final String T_NAME = "tName";

    public static final String ID_NUMBER = "idNumber";

    public static final String PHONE = "phone";

    public static final String PRICE = "price";

    public static final String CARD_ID = "cardId";


    public TheOrder(Long guideId){
        this.guideId = guideId;
    }

    public TheOrder(Long guideId,String state){
        this.guideId = guideId;
        this.state = state;
    }

    public TheOrder(Long touristId,Long routeId,Date time){
        this.touristId = touristId;
        this.routeId = routeId;
        this.time = time;
    }



}