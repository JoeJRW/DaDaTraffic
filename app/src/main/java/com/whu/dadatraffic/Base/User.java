/*
*author: 李俊
*create: time: 2020-07-10
*update: time: 2020-07-19 施武轩
*/

package com.whu.dadatraffic.Base;

public class User {
    private String phoneNumber;    //用户手机号
    private String password;       //用户密码
    private String name;           //用户姓名
    private int credit;            //用户积分
    private String commonAddress="";


    public User(String phoneNumber,String password,String name) {
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.name = name;

    }
    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    /*
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber=phoneNumber;
    }
     */
    public String getName() {
        return name;
    }

    /*public void setName(String name) {
        this.name = name;
    }

     */

    public String getPassword(){return password;}

    public void setName(String name) {
        this.name = name;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public int getCredit() {
        return credit;
    }

    public void changeCredit(int newCredit){
        credit=newCredit;
    }

    public String getCommonAddress() {
        return commonAddress;
    }

    public void setCommonAddress(String commonAddress) {
        this.commonAddress = commonAddress;
    }
}
