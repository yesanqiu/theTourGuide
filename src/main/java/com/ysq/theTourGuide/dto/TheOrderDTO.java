package com.ysq.theTourGuide.dto;

import com.ysq.theTourGuide.entity.TheOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TheOrderDTO {

    private TheOrder theOrder;
    private String line;
    private String guideName;
    private Integer time_day;
    private Integer time_night;

}
