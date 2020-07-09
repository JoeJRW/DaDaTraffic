package com.whu.dadatraffic.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.whu.dadatraffic.MainActivity;
import com.whu.dadatraffic.R;

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
                //phoneNumber = usernameEt.getText().toString();
                //获取密码
                //password = passwordEt.getText().toString();

                //从服务器端验证密码

               //跳转到主界面
               //定义跳转对象
                Intent intentToMain = new Intent().setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                //设置跳转的起始界面和目的界面
                intentToMain.setClass(LoginActivity.this, MainActivity.class);
                //启动跳转
                startActivity(intentToMain);
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
        passwordEt = (EditText)findViewById(R.id.passwordEditText);
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
}