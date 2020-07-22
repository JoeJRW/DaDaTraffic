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

    /**修改用户积分时调用该函数
     * @param newCredit 用户新的积分数
     */
    public void changeCredit(int newCredit){
        final String changeUrlStr = DBConstent.URL_ChangeUserInfo + "?type=change&phonenumber="+ curUser.getPhoneNumber()+"newcredit="+newCredit;
        new UserAsyncTask().execute(changeUrlStr,"change");
        curUser.changeCredit(newCredit);
    }

    //设置当前用户信息√
    public void setCurrentUserInfo() {
        final String getUserUrlStr = DBConstent.URL_User + "?type=getinfo&phonenumber="+ curUser.getPhoneNumber();
        new UserAsyncTask().execute(getUserUrlStr,"getinfo");
    }

    /**
     *用户对司机进行投诉的接口√
     * @param driverPhone 投诉的司机手机号
     * @param  content 投诉内容
     */
    public void complain(String driverPhone,String content){
        String complainUrlStr = DBConstent.URL_Complain+"?driverphone="+driverPhone+"&content="+content;
        new UserAsyncTask().execute(complainUrlStr,"complain");
    }

    /**
     * 为用户添加常用地址
     * @param commonAddress 用户的常用地址
     */
    public void addAddress(String commonAddress){
        String addUrlStr = DBConstent.URL_ChangeUserInfo+"?type=changeaddress&address="+commonAddress;
        new UserAsyncTask().execute(addUrlStr,"changeaddress");
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
