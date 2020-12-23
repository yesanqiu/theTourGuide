package com.ysq.theTourGuide.config;

public enum OrderState {

    TODO("222"),FINISH("111"),CANCEL("333");

    private String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    private OrderState(String state){
        this.state = state;
    }
}
