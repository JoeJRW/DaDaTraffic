/*
*author: 李俊
*create: time: 2020-07-10
*update: time: 2020-07-19 施武轩
*/

package com.whu.dadatraffic.Service;

import android.os.AsyncTask;
import android.os.Message;

import com.whu.dadatraffic.Activity.LoginActivity;
import com.whu.dadatraffic.Activity.RegisterActivity;
import com.whu.dadatraffic.Activity.RouteActivity;
import com.whu.dadatraffic.Utils.DBConstent;
import com.whu.dadatraffic.Base.User;

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

public class UserService {
    public static User curUser;//表示当前登录应用的用户
    private boolean flag = false;
    /*
    private ArrayList<User> users;

    public UserService() {
        users=new ArrayList<User>();
    }
    public ArrayList<User> getUsers() {
        return users;
    }
    //String resMessage = "";

     */

    /**用户在积分商城兑换商品后调用此函数，扣除用户相应积分
     * @param cost 本次购买消费的积分
     */
    public void costCredit(int cost){
        final String costUrlStr = DBConstent.URL_User + "?type=cost&phonenumber="+ curUser.getPhoneNumber()+"costcredit="+cost;
        new UserAsyncTask().execute(costUrlStr);
        curUser.costCredit(cost);
    }

    //设置当前用户信息
    public void setCurrentUserInfo() {
        final String getUserUrlStr = DBConstent.URL_User + "?type=getinfo&phonenumber="+ curUser.getPhoneNumber();
        new UserAsyncTask().execute(getUserUrlStr,"getinfo");
    }

    /**
     * 查看当前订单是否处于"进行中"（即是否已有司机接单）
     */
    public void checkOrderIsRunning(){

        flag = false;

        final String checkUrlStr = DBConstent.URL_User + "?type=checkstate&orderid=" + OrderService.curOrder.getOrderID();
        //new OrderAsyncTask().execute(queryUrlStr);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                new UserAsyncTask().execute(checkUrlStr,"check");
                if(flag){
                    Message message=new Message();
                    message.what=1;
                    RouteActivity.tipHandler.sendMessage(message);
                }
            }
        },0,5000);//每隔5秒做一次run()操作，查询订单状态

    }

    //用户注册,把数据传给服务器
    public void register(User user)
    {
        final String registerUrlStr = DBConstent.URL_Register + "?type=passenger&phonenumber=" + user.getPhoneNumber() + "&password=" + user.getPassword() +"&username="+user.getName();
        new RegisterAsyncTask().execute(registerUrlStr);
        //return resMessage;
    }

    //用户登录
    public void login(String phoneNumber,String password)
    {
        final String loginUrlStr = DBConstent.URL_Login + "?type=passenger&phonenumber=" + phoneNumber + "&password=" + password;
        new LoginAsyncTask().execute(loginUrlStr);
    }

    /**
     * AsyncTask类的三个泛型参数：
     * （1）Param 在执行AsyncTask是需要传入的参数，可用于后台任务中使用
     * （2）后台任务执行过程中，如果需要在UI上先是当前任务进度，则使用这里指定的泛型作为进度单位
     * （3）任务执行完毕后，如果需要对结果进行返回，则这里指定返回的数据类型
     */
    public class RegisterAsyncTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            //Log.w("WangJ", "task onPreExecute()");
        }

        /**
         * @param params 这里的params是一个数组，即AsyncTask在激活运行是调用execute()方法传入的参数
         */
        @Override
        protected String doInBackground(String... params) {
            //Log.w("WangJ", "task doInBackground()");
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
            if(result.equals("100")) {
                RegisterActivity.instance.registerFail("当前号码已注册");
            }
            else if(result.equals("200")) {
                RegisterActivity.instance.registerSuccess("注册成功");
            }
            else {
                RegisterActivity.instance.registerFail("注册失败");
            }
        }

    }

    /**
     * AsyncTask类的三个泛型参数：
     * （1）Param 在执行AsyncTask是需要传入的参数，可用于后台任务中使用
     * （2）后台任务执行过程中，如果需要在UI上先是当前任务进度，则使用这里指定的泛型作为进度单位
     * （3）任务执行完毕后，如果需要对结果进行返回，则这里指定返回的数据类型
     */
    public class LoginAsyncTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            //Log.w("WangJ", "task onPreExecute()");
        }

        /**
         * @param params 这里的params是一个数组，即AsyncTask在激活运行是调用execute()方法传入的参数
         */
        @Override
        protected String doInBackground(String... params) {
            //Log.w("WangJ", "task doInBackground()");
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
            if(result.equals("100")) {
                LoginActivity.instance.loginFail("密码不匹配或账号未注册");

            }
            else if(result.equals("200")) {
                LoginActivity.instance.loginSuccess_Passenger("登录成功");
            }
            else {
                LoginActivity.instance.loginFail("登录失败");
            }
        }

    }


    public class UserAsyncTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            //Log.w("WangJ", "task onPreExecute()");
        }

        /**
         * @param params 这里的params是一个数组，即AsyncTask在激活运行是调用execute()方法传入的参数
         */
        @Override
        protected String doInBackground(String... params) {
            //Log.w("WangJ", "task doInBackground()");
            HttpURLConnection connection = null;
            StringBuilder response = new StringBuilder();
            String type=params[1];
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

            if(type.equals("getinfo"))
            {
                if(response.toString() != "100")
                {
                    String info[]=response.toString().split(";");
                    curUser.setName(info[0]);
                    curUser.setCredit(Integer.parseInt(info[1]));
                }
            }
            else if(type.equals("check")){
                String info[]=response.toString().split(";");
                if(info[0]=="200")
                {
                    flag = true;
                    OrderService.curOrder.setDriverName(info[1]);
                    OrderService.curOrder.setCarNumber(info[2]);
                }
            }
            return response.toString();
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
