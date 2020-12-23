package com.ysq.theTourGuide.dto;


public class UploadFileResult {

    //上传状态码，成功200，失败500，异常400
    private String Code;
    //文件直接访问url
    private String HttpUrl;
    //文件路径
    private String objectName;

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getHttpUrl() {
        return HttpUrl;
    }

    public void setHttpUrl(String httpUrl) {
        HttpUrl = httpUrl;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public UploadFileResult() {
    }
}
