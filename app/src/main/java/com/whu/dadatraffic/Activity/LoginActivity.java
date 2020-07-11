package com.whu.dadatraffic.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.whu.dadatraffic.Base.DBConstent;
import com.whu.dadatraffic.MainActivity;
import com.whu.dadatraffic.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    private Button loginBtn = null;  //登录按钮
    private Button registerBtn = null;//注册按钮
    private TextView messageTv = null;//消息提示框
    private EditText usernameEt = null;//手机号输入框
    private EditText passwordEt = null;//密码输入框
    private RadioButton passengerRbtn = null;//乘客选择按钮
    private RadioButton driverRbtn = null;//司机选择按钮
    private RadioGroup roleRadioGroup = null;//单选组
    private CheckBox rememberPswCheckBox = null;
    private CheckBox autoLoginCheckBox = null;

    private boolean isPassager = true;
    private boolean autoLogin = false;
    private boolean rememberPsw = false;
    private String phoneNumber = "";
    private String password = "";

    //public static LoginActivity instance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //关闭初始化界面
        Splash.instance.finish();

        phoneNumber=getIntent().getStringExtra("phone");

        initUI();

        //隐藏标题栏
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }


        //messageTv.setText("是乘客");


        //给登录按钮绑定事件
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取用户名
                phoneNumber = usernameEt.getText().toString();
                //获取密码
                password = passwordEt.getText().toString();

                //从服务器端验证密码并登录
                login();
            }
        });

        //给注册按钮绑定事件
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到主界面
                //定义跳转对象
                Intent intentToReg = new Intent();
                //设置跳转的起始界面和目的界面
                intentToReg.setClass(LoginActivity.this, RegisterActivity.class);
                //启动跳转
                startActivity(intentToReg);
            }
        });

        //给单选框绑定事件
        roleRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                //判断当前选择是哪个单选按钮
                if(i == passengerRbtn.getId())
                {
                    //当前选择的是乘客
                    //messageTv.setText("是乘客");
                    isPassager = true;
                }
                else if(i == driverRbtn.getId())
                {
                    //当前选择的是司机
                    //messageTv.setText("是司机");
                    isPassager = false;
                }
            }
        });

        //给自记住密码框绑定事件
        rememberPswCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked == true){
                    rememberPsw = true;
                    //修改服务器数据
                }
            }
        });

        //给自动登录框绑定事件
        autoLoginCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked == true){
                    autoLogin = true;
                    //修改服务器数据
                }
            }
        });

    }

    //初始化控件，将控件与变量建立关联
    public void initUI(){
        loginBtn = (Button)findViewById(R.id.loginBtn);
        registerBtn = (Button)findViewById(R.id.cfRegisterBtn);
        messageTv = (TextView)findViewById(R.id.msgTextView);
        usernameEt = (EditText)findViewById(R.id.userEditText);
        passwordEt = (EditText)findViewById(R.id.pswEditText);
        passengerRbtn = (RadioButton)findViewById(R.id.rbtPassenger);
        driverRbtn = (RadioButton)findViewById(R.id.rbtDriver);
        roleRadioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        rememberPswCheckBox = (CheckBox)findViewById(R.id.rememberCheckBox);
        autoLoginCheckBox = (CheckBox)findViewById(R.id.autoLoginCheckBox);

        usernameEt.setText(phoneNumber);
    }

    public void setPhoneNumber(String phoneNum){
        this.phoneNumber = phoneNum;
    }

    //将账户注册到服务器
    private void login() {
        final String loginUrlStr = DBConstent.URL_Login + "?phonenumber=" + phoneNumber + "&password=" + password;
        new LoginActivity.LoginAsyncTask().execute(loginUrlStr);
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
                messageTv.setText("密码不匹配或账号未注册");

            }
            else if(result.equals("200")) {
                messageTv.setText("登录成功");

                //跳转到主界面
                //定义跳转对象
                Intent intentToMain = new Intent().setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                //设置跳转的起始界面和目的界面
                intentToMain.setClass(LoginActivity.this, MainActivity.class);
                //启动跳转
                startActivity(intentToMain);
            }
            else {
                messageTv.setText("登录失败");
            }
        }

    }

}