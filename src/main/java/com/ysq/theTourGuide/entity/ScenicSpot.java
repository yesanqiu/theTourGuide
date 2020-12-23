package com.ysq.theTourGuide.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "scenic_spot")
public class ScenicSpot  implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    /**
     * 景区id
     */
    @Column(name = "scenic_id")
    private String scenicId;

    /**
     * 景点名字
     */
    private String name;

    public static final String ID = "id";

    public static final String SCENIC_ID = "scenicId";

    public static final String NAME = "name";
}