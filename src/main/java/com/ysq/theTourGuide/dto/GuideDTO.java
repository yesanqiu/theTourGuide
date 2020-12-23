package com.ysq.theTourGuide.dto;

import com.ysq.theTourGuide.base.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GuideDTO extends BaseDTO {


    private Long id;
    /**
     * 姓名
     */
    private String name;

    /**
     * 电话
     */
    private String phone;

    /**
     * 导游证
     */
    private String touristCertificateUrl;


    /**
     * 导游证号
     */
    private Long theGuideNumber;

    /**
     * 所在机构
     */
    private String organization;

    /**
     * 期限
     */
    private Date date;


    /**
     * 导游年份
     */
    private String years;

}
