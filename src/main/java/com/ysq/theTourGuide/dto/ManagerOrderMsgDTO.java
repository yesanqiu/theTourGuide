package com.ysq.theTourGuide.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManagerOrderMsgDTO {

    private String line;
    private String guideName;
    private String start;
    private Integer nop;
    private String name;
    private String IdNumber;
    private String phone;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date meetTime;
}
