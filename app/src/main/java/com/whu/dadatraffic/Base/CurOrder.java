package com.whu.dadatraffic.Base;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CurOrder {

    private String customerPhoneNum = "";//乘客手机号
    private String orderID = "";//当前订单编号
    private String driverPhone = "";//当前订单司机手机号
    private String driverName = "";//当前订单司机姓名
    private double driverScore = 0.0;//当前司机评分
    private String carID = "";//当前订单司机车牌号
    private String createTime = "";//订单创建时间
    private String startPoint = "";//行程起始地点
    private String destination = "";//行程目的地
    private String price = "";//本次行程价格
    public String orderState = "";//订单状态
    private String evaluation = "";//行程评价
    private double score = 0.0;//评分

    public CurOrder(String PhoneNum , String startPoint, String destination){
        Date date = new Date(System.currentTimeMillis());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd,HH-mm");
        createTime = simpleDateFormat.format(date);
        orderState = "wait";
        this.customerPhoneNum = PhoneNum;
        this.startPoint = startPoint;
        this.destination = destination;
    }

    public double getDriverScore() {
        return driverScore;
    }

    public void setDriverScore(double driverScore) {
        this.driverScore = driverScore;
    }

    public String getOrderID(){return orderID;}

    public String getCreateTime(){return createTime;}

    public String getStartPoint(){return startPoint;}

    public String getDestination(){return destination;}

    public String getPrice() {
        return price;
    }

    public String getCustomerPhoneNum() {
        return customerPhoneNum;
    }

    public void setEvaluation(String evalution) {
        this.evaluation = evalution;
    }
    public String getEvaluation(){return evaluation;}

    public void setScore(double score){this.score = score;}
    public double getScore(){return score;}

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setOrderID(String id){orderID=id;}

    public String getCarID() {
        return carID;
    }

    public void setCarID(String carID) {
        this.carID = carID;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
