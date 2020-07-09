package com.whu.dadatraffic.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.whu.dadatraffic.R;

public class RegisterActivity extends AppCompatActivity {

    private Button registerBtn = null;//确认注册按钮
    private EditText phoneNumberEt = null;//手机号输入框
    private EditText passwordEt = null;//密码输入框
    private EditText confirmPasswordEt = null;//确认密码输入框
    private TextView hintTv1 = null;//提示框1
    private TextView hintTv2 = null;//提示框2
    private TextView hintTv3 = null;//提示框3
    private CheckBox isDriverCb_reg = null;//我是司机复选框
    private TextView carNumTv = null;
    private EditText carNumEt = null;

    private boolean canRegister = true;
    private boolean isDriver = false;
    private String phoneNumber = null;
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
                password1 = passwordEt.getText().toString();
                password1 = confirmPasswordEt.getText().toString();

                //判断手机号是否有误
                if(phoneNumber.length() != 11){
                    hintTv1.setText("输入手机号有误");
                    canRegister = false;
                }

                //判断是否输入密码
                if(password1.isEmpty()){
                    hintTv2.setText("请输入密码");
                    canRegister = false;
                }

                //判断确认密码与原密码是否一致
                if(!password1.equals(password2)){
                    hintTv3.setText("两次输入的密码不一致");
                    canRegister = false;
                }


                if (isDriver){
                    carNumber = carNumEt.getText().toString();
                    if(carNumber.isEmpty()){
                        Toast.makeText(getApplicationContext(),"车牌号不可为空",Toast.LENGTH_SHORT);
                    }
                }

                //将数据传给服务器并获得一个结果

                //跳转回登录界面
                //定义跳转对象
                Intent intentToLogin = new Intent().setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                //设置跳转的起始界面和目的界面
                intentToLogin.setClass(RegisterActivity.this, LoginActivity.class);
                //启动跳转
                startActivity(intentToLogin);

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
        passwordEt = (EditText)findViewById(R.id.passwordEditText);
        confirmPasswordEt = (EditText)findViewById(R.id.cfPasswordEditText);
        hintTv1 = (TextView)findViewById(R.id.hintTextView1);
        hintTv2 = (TextView)findViewById(R.id.hintTextView2);
        hintTv3 = (TextView)findViewById(R.id.hintTextView3);
        isDriverCb_reg = (CheckBox)findViewById(R.id.isDriverCheckBox_reg);
        carNumTv = (TextView)findViewById(R.id.carNumberTextView);
        carNumEt = (EditText)findViewById(R.id.carNumberEditText);

        //隐藏车牌号相关控件
        carNumEt.setVisibility(View.INVISIBLE);
        carNumTv.setVisibility(View.INVISIBLE);
    }
}