/*
*author: 李俊
*create: time: 2020-07-10
*update: time:
*/

package com.whu.dadatraffic.Service;

import android.os.AsyncTask;
import android.os.Message;

import com.whu.dadatraffic.Activity.LoginActivity;
import com.whu.dadatraffic.Activity.RegisterActivity;
import com.whu.dadatraffic.MainActivity;
import com.whu.dadatraffic.Utils.DBConstent;
import com.whu.dadatraffic.Base.Driver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class DriverService {
    public static Driver curDriver;
    private String depatrue = "";//出发地
    private String destination = "";//目的地
    private boolean flag = false;
    /*
    public DriverService() {
        drivers=new ArrayList<Driver>();
    }

    public ArrayList<Driver> getDrivers() {
        return drivers;
    }

    /*
    //通过电话号码查找司机
    public Driver getDriver(String phoneNumber) {
        for(int i=0;i<drivers.size();i++){
            if(phoneNumber==drivers.get(i).getPhoneNumber())
                return drivers.get(i);
        }
        return null;
    }

     */


    /*
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

     */
/*
    //修改用户信息（姓名、车牌号）
    public boolean updateDriver(Driver newDriver) {
        Driver oldDriver=getDriver(newDriver.getPhoneNumber());
        if(oldDriver==null)
            return false;
        drivers.remove(oldDriver);
        drivers.add(newDriver);
        return true;
    }

 */

    /**
     * 司机注册时调用该函数
     * @param driver 新建的driver对象
     */
    public void register(Driver driver)
    {
        //执行注册操作的服务端地址
        final String registerUrlStr = DBConstent.URL_Register + "?type=driver&phonenumber=" + driver.getPhoneNumber() + "&password=" + driver.getPassword()
                +"&username=" + driver.getName() + "&carnumber=" + driver.getCarId();
        //第二个参数代表操作类型
        new DriverAsyncTask().execute(registerUrlStr,"register");
    }

    /**
     * 司机登录时调用该函数
     * @param phoneNumber 司机注册时使用的手机号
     * @param password 司机当前登录所用手机号对应的密码
     */
    public void login(String phoneNumber,String password){
        //执行登录操作的服务端地址
        final String loginUrlStr = DBConstent.URL_Login + "?type=driver&phonenumber=" + phoneNumber + "&password=" + password;
        //第二个参数代表操作类型
        new DriverAsyncTask().execute(loginUrlStr,"login");
    }


    /**
     * 司机接单时调用该函数
     * 每隔10s向服务器发送一次请求，如果有订单就会分配
     */
    public void open(){
        final String checkUrlStr = DBConstent.URL_Driver + "?type=check";

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                new DriverAsyncTask().execute(checkUrlStr,"check");
                if(flag){
                    return;
                }
            }
        },0,10000);//每隔10秒做一次run()操作，查询是否有可接订单
    }

    /**
     * 从服务器获取司机的姓名评分等信息并设置给当前司机
     */
    public void setCurDriver(){
        //执行登录操作的服务端地址
        final String setUrlStr = DBConstent.URL_Driver + "?type=driver&phonenumber=" + curDriver.getPhoneNumber();
        //第二个参数代表操作类型
        new DriverAsyncTask().execute(setUrlStr,"setInfo");
    }

    /**
     * AsyncTask类的三个泛型参数：
     * （1）Param 在执行AsyncTask是需要传入的参数，可用于后台任务中使用
     * （2）后台任务执行过程中，如果需要在UI上先是当前任务进度，则使用这里指定的泛型作为进度单位
     * （3）任务执行完毕后，如果需要对结果进行返回，则这里指定返回的数据类型
     */
    public class DriverAsyncTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            //Log.w("WangJ", "task onPreExecute()");
        }

        /**
         * @param params 这里的params是一个数组，即AsyncTask在激活运行是调用execute()方法传入的参数
         */
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            StringBuilder response = new StringBuilder();
            try {
                URL url = new URL(params[0]); // 声明一个URL
                connection = (HttpURLConnection) url.openConnection(); // 打开该URL连接
                connection.setRequestMethod("GET"); // 设置请求方法，“POST或GET”，我们这里用GET，在说到POST的时候再用POST
                connection.setConnectTimeout(80000); // 设置连接建立的超时时间
                connection.setReadTimeout(80000); // 设置网络报文收发超时时间
                InputStream in = connection.getInputStream();  // 通过连接的输入流获取下发报文，然后就是Java的流处理
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(params[1].equals("register"))
            {
                if(response.toString().equals("100")) {
                    RegisterActivity.instance.registerFail("当前号码已注册");
                }
                else if(response.toString().equals("200")) {
                    RegisterActivity.instance.registerSuccess("注册成功");
                }
                else {
                    RegisterActivity.instance.registerFail("注册失败");
                }
            }
            else if(params[1].equals("login"))
            {
                if(response.toString().equals("100")) {
                    LoginActivity.instance.loginFail("密码不匹配或账号未注册");

                }
                else if(response.toString().equals("200")) {
                    LoginActivity.instance.loginSuccess_Driver("登录成功");
                }
                else {
                    LoginActivity.instance.loginFail("登录失败");
                }
            }
            else if(params[1].equals("setInfo")){
                String info[]=response.toString().split(";");
                curDriver.setName(info[0]);
                curDriver.setScore(Double.parseDouble(info[1]));
            }
            else if (params[1].equals("check")){
                if(response.toString().equals("200")) {flag=true;}
                else {flag=false;}
            }
            return response.toString(); // 这里返回的结果就作为onPostExecute方法的入参
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // 如果在doInBackground方法，那么就会立刻执行本方法
            // 本方法在UI线程中执行，可以更新UI元素，典型的就是更新进度条进度，一般是在下载时候使用
        }

        /**
         * 运行在UI线程中，所以可以直接操作UI元素
         * @param
         */

        @Override
        protected void onPostExecute(String result) {
        }

    }
}
