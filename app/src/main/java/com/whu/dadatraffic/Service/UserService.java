/*
*author: 李俊
*create: time: 2020-07-10
*update: time:
*/

package com.whu.dadatraffic.Service;

import android.os.AsyncTask;

import com.whu.dadatraffic.Activity.LoginActivity;
import com.whu.dadatraffic.Activity.RegisterActivity;
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

public class UserService {
    private ArrayList<User> users;

    public UserService() {
        users=new ArrayList<User>();
    }
    public ArrayList<User> getUsers() {
        return users;
    }
    //String resMessage = "";

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

            if(response.toString().equals("100")) {
                RegisterActivity.instance.registerFail("当前号码已注册");
            }
            else if(response.toString().equals("200")) {
                RegisterActivity.instance.registerSuccess("注册成功");
            }
            else {
                RegisterActivity.instance.registerFail("注册失败");
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

            /*
            if(response.toString().equals("登录失败，密码不匹配或账号未注册")) {
                LoginActivity.instance.loginFail("密码不匹配或账号未注册");

            }
            else if(response.toString().equals("登陆成功")) {
                LoginActivity.instance.loginSuccess_Passenger("登录成功");
            }
            else {
                LoginActivity.instance.loginFail("登录失败");
            }

             */
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
}
