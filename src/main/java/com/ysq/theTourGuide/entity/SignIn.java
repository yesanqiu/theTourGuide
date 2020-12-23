package com.ysq.theTourGuide.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "sign_in")
public class SignIn  implements Serializable {
    @Column(name = "s_id")
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long sId;

    /**
     * 游客id
     */
    @Column(name = "tourist_id")
    private Long touristId;

    /**
     * 时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;

    public static final String S_ID = "sId";

    public static final String TOURIST_ID = "touristId";

    public static final String DATE = "date";

    public SignIn(Long touristId){
        this.touristId = touristId;
        this.date = new Date();
    }
}