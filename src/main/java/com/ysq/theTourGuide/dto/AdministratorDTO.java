package com.ysq.theTourGuide.dto;

import com.ysq.theTourGuide.entity.Administrator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdministratorDTO {

    private Administrator administrator;
    private String typeName;
}
