package com.whu.dadatraffic.Base;

public class User {
    private String phoneNumber;    //用户手机号
    private String password;       //用户密码
    private String name;           //用户姓名

    public User(String phoneNumber,String password,String name){
        this.phoneNumber=phoneNumber;
        this.name=name;
        this.password =password;
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

    public void setPassword(String newPassword){this.password=newPassword;}
}
