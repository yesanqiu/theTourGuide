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
public class Fans  implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    /**
     * 游客id
     */
    @Column(name = "tourist_id")
    private Long touristId;

    /**
     * 喜欢的导游id
     */
    @Column(name = "guide_id")
    private Long guideId;

    public static final String ID = "id";

    public static final String TOURIST_ID = "touristId";

    public static final String GUIDE_ID = "guideId";

    public Fans(Long touristId,Long guideId){
        this.touristId = touristId;
        this.guideId = guideId;
    }
}