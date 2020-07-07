package com.whu.dadatraffic.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.whu.dadatraffic.MainActivity;
import com.whu.dadatraffic.R;

public class LoginActivity extends AppCompatActivity {

    private Button loginBtn = null;  //登录按钮
    private Button registerBtn = null;//注册按钮
    private TextView messageTv = null;//消息提示框
    private EditText usernameEt = null;//手机号输入框
    private EditText passwordEt = null;//密码输入框

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUI();

        //给登录按钮绑定事件
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //跳转到主界面
               //定义跳转对象
                Intent intentToMain = new Intent();
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
    }

    //初始化控件，将控件与变量建立关联
    public void initUI(){
        loginBtn = (Button)findViewById(R.id.loginBtn);
        registerBtn = (Button)findViewById(R.id.registerBtn);
        messageTv = (TextView)findViewById(R.id.msgTextView);
        usernameEt = (EditText)findViewById(R.id.userEditText);
    }
}