package com.ysq.theTourGuide.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ysq.theTourGuide.config.TaskEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Task  implements Serializable {

    @Column(name = "t_id")
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long tId;

    /**
     * 标题
     */
    @Column(name = "t_title")
    private String tTitle;

    /**
     * 时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @Column(name = "t_time")
    private Date tTime;

    /**
     * 分数
     */
    @Column(name = "t_score")
    private Integer tScore;

    /**
     * 游客id
     */
    @Column(name = "tourist_id")
    private Long touristId;

    public static final String T_ID = "tId";

    public static final String T_TITLE = "tTitle";

    public static final String T_TIME = "tTime";

    public static final String T_SCORE = "tScore";

    public static final String TOURIST_ID = "touristId";

    public Task(Long touristId, TaskEnum taskEnum, Date date){
        this.touristId = touristId;
        this.tTitle = taskEnum.getTitle();
        this.tScore = taskEnum.getScore();
        this.tTime = date;
    }

    public Task(Long touristId, TaskEnum taskEnum){
        this.touristId = touristId;
        this.tTitle = taskEnum.getTitle();
        this.tScore = taskEnum.getScore();
    }

    public Task(Long touristId){
        this.touristId = touristId;
    }
}