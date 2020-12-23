package com.ysq.theTourGuide.entity;

import lombok.Data;

import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Table(name = "score_type")
public class ScoreType  implements Serializable {
    private Integer id;

    private String name;

    private BigDecimal precent;

    public static final String ID = "id";

    public static final String NAME = "name";

    public static final String PRECENT = "precent";
}