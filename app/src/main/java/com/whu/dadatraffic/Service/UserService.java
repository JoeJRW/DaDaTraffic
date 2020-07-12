/*
*author: 李俊
*create: time: 2020-07-10
*update: time:
*/

package com.whu.dadatraffic.Service;

import android.app.ApplicationErrorReport;
import android.app.ApplicationExitInfo;

import com.whu.dadatraffic.Base.User;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private ArrayList<User> users;

    public UserService() {
        users=new ArrayList<User>();
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    //添加用户
    public boolean addUser(User user) {
        if(users.contains(user))
            return false;
        users.add(user);
        return true;
    }

    //通过电话号码查找用户
    public User getUser(String phoneNumber) {
        for(int i=0;i<users.size();i++){
            if(phoneNumber==users.get(i).getPhoneNumber())
                return users.get(i);
        }
        return null;
    }

    //通过电话号码删除用户
    public boolean removeUser(String phoneNumber) {
        User user=getUser(phoneNumber);
        if(user !=null){
            users.remove(user);
            return true;
        }
        else
            return false;
    }

    //修改用户信息（姓名）
    public boolean updateUser(User newUser) {
        User oldUser=getUser(newUser.getPhoneNumber());
        if(oldUser==null)
            return false;
        users.remove(oldUser);
        users.add(newUser);
        return true;
    }
}
