/*
*author: 李俊
*create: time: 2020-07-10
*update: time:
*/

package com.whu.dadatraffic.Base;

public class Driver {
    private String phoneNumber;   //司机手机号
    private String password;     //司机密码
    private String name;          //司机姓名
    private String carId;         //司机车牌号
    private double score;         //司机评分
//    private String IDNumber;      //司机身份证号

    public Driver(String phoneNumber,String password,String name,String carId){
        this.phoneNumber=phoneNumber;
        this.password = password;
        this.name=name;
        this.carId=carId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getPassword(){return password;}


}
