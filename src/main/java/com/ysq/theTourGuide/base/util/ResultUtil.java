package com.ysq.theTourGuide.base.util;


import com.ysq.theTourGuide.base.dto.ResultDTO;
import com.ysq.theTourGuide.config.ErrorCode;

/**
 * @author ReMidDream
 * @date 2018-02-22 15:55
 **/
public class ResultUtil {

    public static ResultDTO Success(Object object){
        ResultDTO<Object> resultDto = new ResultDTO<Object>();
        resultDto.setData(object);
        resultDto.setMsg("成功");
        resultDto.setCode("200");
        return resultDto;
    }

    public static ResultDTO Success(){
        return Success(null);
    }

    public static ResultDTO Error(ErrorCode errorCode){
        ResultDTO resultDto = new ResultDTO();
        resultDto.setMsg(errorCode.getMsg());
        resultDto.setCode(errorCode.getCode());
        return resultDto;
    }

    public static ResultDTO Error(String code, String msg){
        ResultDTO resultDto = new ResultDTO();
        resultDto.setMsg(msg);
        resultDto.setCode(code);
        return resultDto;
    }
}
