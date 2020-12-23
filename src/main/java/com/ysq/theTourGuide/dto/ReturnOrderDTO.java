package com.ysq.theTourGuide.dto;

import com.ysq.theTourGuide.entity.Guide;
import com.ysq.theTourGuide.entity.Route;
import com.ysq.theTourGuide.entity.TheOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnOrderDTO {

    private TheOrder theOrder;

    private Guide guide;

    private TouristDTO tourist;

    private Route route;

    private Double price;

}
