package com.ysq.theTourGuide.dto;

import com.ysq.theTourGuide.base.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO extends BaseDTO {
    private Long id;
    private Long touristId;
    private Long routeId;
    private Long guideId;
    private String tStart;
    private Integer nOP;
    private Date time;
    private String meetTime;
    private String tName;
    private String idNumber;
    private String phone;
    private String state = "222";

}
