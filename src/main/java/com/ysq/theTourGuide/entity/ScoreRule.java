package com.ysq.theTourGuide.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Table(name = "score_rule")
public class ScoreRule  implements Serializable {
    private Integer id;

    private Integer previous;

    private Integer brevious;

    private Integer score;

    @Column(name = "type_id")
    private Integer typeId;

    @Column(name = "next_id")
    private Integer nextId;

    public static final String ID = "id";

    public static final String PREVIOUS = "previous";

    public static final String BREVIOUS = "brevious";

    public static final String SCORE = "score";

    public static final String TYPE_ID = "typeId";

    public static final String NEXT_ID = "nextId";
}