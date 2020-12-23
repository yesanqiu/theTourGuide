package com.ysq.theTourGuide.config;

public enum RecommendAttrs {

    DIS("distance"),LEV("level"),GN("goodNums");

    private String attr;

    private  RecommendAttrs(String str){
        this.attr = str;
    }

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }
}
