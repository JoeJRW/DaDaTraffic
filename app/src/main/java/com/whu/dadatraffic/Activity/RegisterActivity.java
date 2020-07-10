package com.whu.dadatraffic.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.whu.dadatraffic.Base.DBConstent;
import com.whu.dadatraffic.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class RegisterActivity extends AppCompatActivity {
    private Button registerBtn = null;//确认注册按钮
    private EditText phoneNumberEt = null;//手机号输入框
    private EditText nameEt = null;//姓名输入框
    private EditText passwordEt = null;//密码输入框
    private EditText confirmPasswordEt = null;//确认密码输入框
    private TextView hintTv1 = null;//提示框1
    private TextView hintTv2 = null;//提示框2
    private TextView hintTv3 = null;//提示框3
    private TextView hintTv4 =null;//提示框4
    private CheckBox isDriverCb_reg = null;//我是司机复选框
    private TextView carNumTv = null;
    private EditText carNumEt = null;

    private boolean canRegister = false;
    private boolean isDriver = false;
    private String phoneNumber = null;
    private String name = null;
    private String password1 = null;
    private String password2 = null;
    private String carNumber = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initUI();

        //给确认注册按钮绑定事件
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneNumber = phoneNumberEt.getText().toString();
                name = nameEt.getText().toString();
                password1 = passwordEt.getText().toString();
                password2 = confirmPasswordEt.getText().toString();

                //判断手机号是否有误
                if(phoneNumber.length() != 11){
                    hintTv1.setText("输入手机号有误");
                    canRegister = false;
                }

                //判断是否输入姓名
                if(name.isEmpty()){
                    hintTv2.setText("请输入姓名");
                    canRegister = false;
                }

                //判断是否输入密码
                if(password1.isEmpty()){
                    hintTv3.setText("请输入密码");
                    canRegister = false;
                }

                //判断确认密码与原密码是否一致
                if(!password1.equals(password2)){
                    hintTv4.setText("两次输入的密码不一致");
                    canRegister = false;
                }

                if (isDriver){
                    carNumber = carNumEt.getText().toString();
                    if(carNumber.isEmpty()){
                        Toast.makeText(getApplicationContext(),"车牌号不可为空",Toast.LENGTH_SHORT);
                    }
                }

                //将数据传给服务器
                register();

                /*
                //当注册成功时
                if(canRegister)
                {
                    //跳转回登录界面
                    //定义跳转对象
                    Intent intentToLogin = new Intent().setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    //设置跳转的起始界面和目的界面
                    intentToLogin.setClass(RegisterActivity.this, LoginActivity.class);
                    //将用户手机号传到登录界面
                    intentToLogin.putExtra("phone",phoneNumber);
                    //启动跳转
                    startActivity(intentToLogin);
                }
                canRegister = true;

                 */
            }
        });

        //给“我是司机”单选框绑定事件
        isDriverCb_reg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                //当选中我是司机时，要求填写车牌号
                if(isChecked){
                    isDriver = true;
                    carNumEt.setVisibility(View.VISIBLE);
                    carNumTv.setVisibility(View.VISIBLE);
                }
                //当取消选中时，隐藏车牌号相关控件
                else{
                    isDriver = false;
                    carNumEt.setVisibility(View.INVISIBLE);
                    carNumTv.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    //初始化控件，将控件与变量建立关联
    private void initUI(){
        registerBtn = (Button)findViewById(R.id.cfRegisterBtn);
        phoneNumberEt = (EditText)findViewById(R.id.phoneEditText);
        nameEt = (EditText)findViewById(R.id.nameEditText);
        passwordEt = (EditText)findViewById(R.id.passwordEditText_reg);
        confirmPasswordEt = (EditText)findViewById(R.id.cfPasswordEditText);
        hintTv1 = (TextView)findViewById(R.id.hintTextView1);
        hintTv2 = (TextView)findViewById(R.id.hintTextView2);
        hintTv3 = (TextView)findViewById(R.id.hintTextView3);
        hintTv4 = (TextView)findViewById(R.id.hintTextView4);
        isDriverCb_reg = (CheckBox)findViewById(R.id.isDriverCheckBox_reg);
        carNumTv = (TextView)findViewById(R.id.carNumberTextView);
        carNumEt = (EditText)findViewById(R.id.carNumberEditText);

        //隐藏车牌号相关控件
        carNumEt.setVisibility(View.INVISIBLE);
        carNumTv.setVisibility(View.INVISIBLE);
    }

    //将账户注册到服务器
    private void register() {
        final String registerUrlStr = DBConstent.URL_Register + "?phonenumber=" + phoneNumber + "&password=" + password1 +"&username="+name;
        new RegisterAsyncTask().execute(registerUrlStr);
        /*
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(registerUrlStr); // 声明一个URL,注意——如果用百度首页实验，请使用https
                    connection = (HttpURLConnection) url.openConnection(); // 打开该URL连接
                    connection.setRequestMethod("GET"); // 设置请求方法，“POST或GET”，我们这里用GET，在说到POST的时候再用POST
                    connection.setConnectTimeout(8000); // 设置连接建立的超时时间
                    connection.setReadTimeout(8000); // 设置网络报文收发超时时间
                    InputStream in = connection.getInputStream();  // 通过连接的输入流获取下发报文，然后就是Java的流处理
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null){
                        response.append(line);
                    }

                    //tvContent.setText(response.toString()); // 地雷
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
*/
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
                Toast.makeText(getApplicationContext(), "当前手机号已注册", Toast.LENGTH_SHORT);
                canRegister = false;
            }
            else if(result.equals("200")) {
                Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_LONG);
                canRegister = true;
                //hintTv2.setText(result);
                //跳转回登录界面
                //定义跳转对象
                Intent intentToLogin = new Intent().setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                //设置跳转的起始界面和目的界面
                intentToLogin.setClass(RegisterActivity.this, LoginActivity.class);
                //将用户手机号传到登录界面
                intentToLogin.putExtra("phone",phoneNumber);
                //启动跳转
                startActivity(intentToLogin);
            }
            else {
                Toast.makeText(getApplicationContext(), "注册失败", Toast.LENGTH_SHORT);
                canRegister = false;
            }
        }

    }

}