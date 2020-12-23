package com.ysq.theTourGuide.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManagerOrderDTO {

    private Long orderId;
    private String line;
    private String touristName;
    private String time;
    private String state;

}
