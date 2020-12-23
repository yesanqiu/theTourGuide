package com.ysq.theTourGuide.config;

public enum ErrorCode {
    INVALID_PARAMETERS("202","未接受到正确的参数或未接受到参数"),
    UNKNOWERROR("203","未知错误"),
    NOEXIST("204","你操作的实体不存在"),
    ISEXIST("205","已存在"),
    ERROR_PSW("206","密码错误"),
    ADMINISTRATOR_NOEXIST("207","账户不存在"),
    LIMITED_AUTHORITY("208","权限不够"),
    CANTDOIT("209","您无法这样子做"),
    NOPOVER("210","人数超过限制"),
    BAN("211","该导游已被封禁"),
    NOVIP("212","不是vip用户"),
    FANS_MYSELF("213","您无法关注自己"),
    ALREADY_RECEIVE("214","本月代金卷已领取"),
    NOT_YOURS("215","不是你的代金卷"),
    NULL("216","没有或者不存在，为空");
    private String code;
    private String msg;
    private ErrorCode(String code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
