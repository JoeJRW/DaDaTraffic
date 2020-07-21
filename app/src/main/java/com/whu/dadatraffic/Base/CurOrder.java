package com.whu.dadatraffic.Base;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CurOrder {

    private String customerPhoneNum = null;//乘客手机号
    private String orderID = null;//当前订单编号
    private String driverPhone = "";//当前订单司机手机号
    private String driverName = "";//当前订单司机姓名
    private int driverScore = 0;//当前司机评分
    private String carID = "";//当前订单司机车牌号
    private String createTime = null;//订单创建时间
    private String startPoint = null;//行程起始地点
    private String destination = null;//行程目的地
    private String price = "";//本次行程价格
    public String orderState = null;//订单状态
    private String evalution = "";//行程评价
    private float score = 0.0f;//评分

    public CurOrder(String PhoneNum , String startPoint, String destination){
        Date date = new Date(System.currentTimeMillis());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        createTime = simpleDateFormat.format(date);
        orderState = "wait";
        this.customerPhoneNum = PhoneNum;
        this.startPoint = startPoint;
        this.destination = destination;
    }

    public int getDriverScore() {
        return driverScore;
    }

    public void setDriverScore(int driverScore) {
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

    public void setEvalution(String evalution) {
        this.evalution = evalution;
    }
    public String getEvalution(){return evalution;}

    public void setScore(float score){this.score = score;}
    public float getScore(){return score;}

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

}
