/*
 *作者：施武轩 创建时间：2020.7.7 更新时间：2020.7.11
 */

package com.whu.dadatraffic.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.whu.dadatraffic.Base.User;
import com.whu.dadatraffic.MainActivity;
import com.whu.dadatraffic.R;
import com.whu.dadatraffic.Service.DriverService;
import com.whu.dadatraffic.Service.UserService;
import com.whu.dadatraffic.Utils.LocalStorageUtil;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    public static LoginActivity instance = null;
    private UserService userService = new UserService();
    private DriverService driverService = new DriverService();
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
        instance=this;

        //关闭初始化界面
        Splash.instance.finish();

        initUI();

        //隐藏标题栏
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }

        if(autoLogin){
            //从服务器端验证密码并登录
            if(isPassager){//乘客登录
                userService.login(phoneNumber,password);
            }
            else {//司机登录
                driverService.login(phoneNumber,password);
            }
        }

        //给登录按钮绑定事件
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取用户名
                phoneNumber = usernameEt.getText().toString();
                //获取密码
                password = passwordEt.getText().toString();

                //从服务器端验证密码并登录
                if(isPassager){//乘客登录
                    userService.login(phoneNumber,password);
                }
                else {//司机登录
                    driverService.login(phoneNumber,password);
                }
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
                    isPassager = true;
                }
                else if(i == driverRbtn.getId())
                {
                    //当前选择的是司机
                    isPassager = false;
                }
            }
        });

        //给自记住密码框绑定事件
        rememberPswCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                rememberPsw = isChecked;
            }
        });

        //给自动登录框绑定事件
        autoLoginCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                autoLogin = isChecked;
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

        //从而本地文件中读取上次保存的账号和密码
        phoneNumber = LocalStorageUtil.getSettingNote(LoginActivity.this,"userPreferences","userphone");
        password = LocalStorageUtil.getSettingNote(LoginActivity.this,"userPreferences","userpwd");

        if(!password.equals("")){
            rememberPswCheckBox.setChecked(true);
            rememberPsw=true;
        }

        if(LocalStorageUtil.getSettingNote(LoginActivity.this,"userPreferences","userphone").equals("true")){
            autoLoginCheckBox.setChecked(true);
            autoLogin = true;
        }

        usernameEt.setText(phoneNumber);
        passwordEt.setText(password);
    }

    /*public void setPhoneNumber(String phoneNum){
        this.phoneNumber = phoneNum;
    }

     */


    public void loginSuccess_Passenger(String result){
        messageTv.setText(result);
        //登录成功时检查是否记住密码
        if(rememberPsw){
            //把手机号和密码保存到本地
            Map<String, String> map = new HashMap<String, String>();
            map.put("userphone", phoneNumber);
            map.put("userpwd", password);
            LocalStorageUtil.saveSettingNote(LoginActivity.this,"userPreferences",map);
        }
        else {
            LocalStorageUtil.deleteSettingNote(LoginActivity.this,"userPreferences","userpwd");
        }
        if(autoLogin){
            //把自动登录设置保存到本地
            Map<String, String> map = new HashMap<String, String>();
            map.put("autologin", "true");
            LocalStorageUtil.saveSettingNote(LoginActivity.this,"userPreferences",map);
        }
        else {
            LocalStorageUtil.deleteSettingNote(LoginActivity.this,"userPreferences","autologin");
        }

        //跳转到乘客的主界面
        //定义跳转对象
        Intent intentToMain = new Intent().setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        //设置跳转的起始界面和目的界面
        intentToMain.setClass(LoginActivity.this, MainActivity.class);
        //TODO
        //UserService.curUser = new User(phoneNumber);

        //传递当前登录手机号
        intentToMain.putExtra("phone",phoneNumber);
        //启动跳转
        startActivity(intentToMain);
    }

    public void loginSuccess_Driver (String result){
        messageTv.setText(result);
        //登录成功时检查是否记住密码
        if(rememberPsw){
            //把密码保存到本地
            Map<String, String> map = new HashMap<String, String>();
            map.put("userphone", phoneNumber);
            map.put("userpwd", password);
            LocalStorageUtil.saveSettingNote(LoginActivity.this,"userPreferences",map);
        }
        else {
            LocalStorageUtil.deleteSettingNote(LoginActivity.this,"userPreferences","userpwd");
        }
        if(autoLogin){
            //把自动登录设置保存到本地
            Map<String, String> map = new HashMap<String, String>();
            map.put("autologin", "true");
            LocalStorageUtil.saveSettingNote(LoginActivity.this,"userPreferences",map);
        }
        else {
            LocalStorageUtil.deleteSettingNote(LoginActivity.this,"userPreferences","autologin");
        }
        //跳转到司机的主界面
        //定义跳转对象
        Intent intentToMain = new Intent().setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        //设置跳转的起始界面和目的界面
        intentToMain.setClass(LoginActivity.this, DriverMainActivity.class);
        //启动跳转
        startActivity(intentToMain);
    }

    public void loginFail(String result){
        messageTv.setText(result);
    }

}