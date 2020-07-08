package com.whu.dadatraffic;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Order {
    private String customerPhoneNum = null;//乘客手机号
    private String orderID = null;//订单编号
    private String driverName = null;//司机姓名
    private String carNumber = null;//车牌号
    private String createTime = null;//订单创建时间
    private String startPoint = null;//行程起始地点
    private String destination = null;//行程目的地
    private double price = 0.00;//本次行程价格
    private String orderState = null;//订单状态
    private String evalution = "";//行程评价
    private float score = 1.0f;//得分

    public Order(String PhoneNum ,String orderID, String driverName, String startPoint, String destination, String carNumber, double price){
        Date date = new Date(System.currentTimeMillis());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        createTime = simpleDateFormat.format(date);
        orderState = "进行中";

        this.customerPhoneNum = PhoneNum;
        this.orderID = orderID;
        this.driverName = driverName;
        this.startPoint = startPoint;
        this.destination = destination;
        this.carNumber = carNumber;
        this.price = price;
    }

    public void orderCancel()
    {
        this.orderState = "已取消";
    }

    public void orderComplete(){
        this.orderState = "已支付";
    }

    public String getOrderID(){return orderID;}

    public String getCreateTime(){return createTime;}

    public String getDriverName(){return driverName;}

    public String getStartPoint(){return startPoint;}

    public String getDestination(){return destination;}

    public double getPrice(){return  price;}

    public void setEvalution(String evalution){this.evalution = evalution;}
    public String getEvalution(){return evalution;}

    public void setScore(float score){this.score = score;}
    public float getScore(){return score;}

    /*
    //实现接口
    @Override
    public int describeContents() {
        return 0;
    }

    //实现接口用于对象传递
    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(customerPhoneNum);
        out.writeString(orderID);
        out.writeString(carNumber);
        out.writeString(createTime);
        out.writeString(driverName);
        out.writeString(startPoint);
        out.writeString(destination);
        out.writeString(evalution);
        out.writeDouble(price);
        out.writeFloat(score);
    }

    public Order(Parcel in)
    {
        customerPhoneNum = in.readString();
        orderID=in.readString();
        carNumber=in.readString();
        createTime=in.readString();
        driverName=in.readString();
        startPoint = in.readString();
        destination=in.readString();
        evalution=in.readString();
        price=in.readDouble();
        score = in.readFloat();
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

     */
}
