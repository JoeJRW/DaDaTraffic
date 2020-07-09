package com.whu.dadatraffic.Service;

import com.whu.dadatraffic.Base.Driver;
import com.whu.dadatraffic.Base.User;

import java.util.ArrayList;

public class DriverService {
    private ArrayList<Driver> drivers;

    public DriverService() {
        drivers=new ArrayList<Driver>();
    }

    public ArrayList<Driver> getDrivers() {
        return drivers;
    }

    //添加司机
    public boolean addDriver(Driver driver) {
        if(drivers.contains(driver))
            return false;
        drivers.add(driver);
        return true;
    }

    //通过电话号码查找司机
    public Driver getDriver(String phoneNumber) {
        for(int i=0;i<drivers.size();i++){
            if(phoneNumber==drivers.get(i).getPhoneNumber())
                return drivers.get(i);
        }
        return null;
    }

    //通过电话号码删除司机
    public boolean removeUser(String phoneNumber) {
        Driver driver=getDriver(phoneNumber);
        if(driver!=null){
            drivers.remove(driver);
            return true;
        }
        else
            return false;
    }

    //修改用户信息（姓名、车牌号）
    public boolean updateDriver(Driver newDriver) {
        Driver oldDriver=getDriver(newDriver.getPhoneNumber());
        if(oldDriver==null)
            return false;
        drivers.remove(oldDriver);
        drivers.add(newDriver);
        return true;
    }
}
