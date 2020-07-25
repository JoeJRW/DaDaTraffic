package com.whu.dadatraffic.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.MobSDK;
import com.whu.dadatraffic.R;
import com.whu.dadatraffic.Service.UserService;


import org.json.JSONObject;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;


public class changePasswordActivity extends AppCompatActivity {
    private UserService userService = new UserService();
    private TextView getVerificationCode=null;
    private EditText phoneEditText=null;
    private EditText verificationCodeEditText=null;
    private EditText newPasswordEditText=null;
    private EditText cfPasswordEditText=null;
    private Button confirmChangePassword=null;
    private String phone="";
    private String code="";
    private String newPassword="";
    private String confirmPassword="";
    private int i=60;
    private boolean verificationCodeIsTrue=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        MobSDK.init(this, "302d311aed140", "df8f512666f47b13bf0f78941c293e0c");
        init();

        // 注册一个事件回调，用于处理SMSSDK接口请求的结果
        SMSSDK.registerEventHandler(eventHandler);

        // 请求验证码时调用，其中country表示国家代码，如“86”；phone表示手机号码，如“13800138000”
        getVerificationCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                phone = phoneEditText.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(getApplicationContext(), "手机号码不能为空",
                            Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    //判断手机号是否有误
                    if(phone.length() != 11){
                        Toast.makeText(getApplicationContext(), "手机号码为11位数噢",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }else{
                        SMSSDK.getVerificationCode("86", phone);
                        getVerificationCode.setClickable(false);
                        //开始倒计时
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                for (; i > 0; i--) {
                                    handler.sendEmptyMessage(-1);
                                    if (i <= 0) {
                                        break;
                                    }
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                //倒计时结束执行
                                handler.sendEmptyMessage(-2);
                            }
                        }).start();
                    }
                }
            }
        });

        // 提交验证码时调用，其中的code表示验证码，如“1357”
        confirmChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                phone = phoneEditText.getText().toString();
                code = verificationCodeEditText.getText().toString();
                newPassword=newPasswordEditText.getText().toString();
                confirmPassword=cfPasswordEditText.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(getApplicationContext(), "手机号码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    //判断手机号是否有误
                    if(phone.length() != 11){
                        Toast.makeText(getApplicationContext(), "手机号码为11位数噢",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }else{
                        if (TextUtils.isEmpty(code)) {
                            Toast.makeText(getApplicationContext(), "验证码不能为空",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }else{
                            if(newPassword.length()<6||confirmPassword.length()<6)
                            {
                                Toast.makeText(getApplicationContext(), "密码至少为6位",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }else{
                                if(!newPassword.equals(confirmPassword))
                                {
                                    Toast.makeText(getApplicationContext(), "两次输入密码不一致",
                                            Toast.LENGTH_SHORT).show();
                                    return;
                                }else{
                                    userService.changePassword(newPassword,phone);
                                    SMSSDK.submitVerificationCode("86", phone, code);
                                }
                            }
                        }
                    }
                }
            }
        });
    }



    private void init() {
        getVerificationCode=findViewById(R.id.getVerificationCode);
        phoneEditText=findViewById(R.id.phoneEditText);
        verificationCodeEditText=findViewById(R.id.verificationCodeText);
        newPasswordEditText=findViewById(R.id.passwordEditText_reg);
        cfPasswordEditText=findViewById(R.id.cfPasswordEditText);
        confirmChangePassword=findViewById(R.id.confirmChangePassword);
    }


    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == -1) {
                //修改控件文本进行倒计时  i 以60秒倒计时为例
                getVerificationCode.setText( i+" s");
            } else if (msg.what == -2) {
                //修改控件文本，进行重新发送验证码
                getVerificationCode.setText("重新发送");
                getVerificationCode.setClickable(true);
                i = 60;
            } else {
                int event = msg.arg1;
                int result = msg.arg2;
                Object data = msg.obj;

                // 短信验证码验证成功后，修改密码，返回LoginActivity
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    if (result == SMSSDK.RESULT_COMPLETE) {

                        Toast.makeText(changePasswordActivity.this, "修改密码成功",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setClass(changePasswordActivity.this, LoginActivity.class);
                        startActivity(intent);

                        onDestroy();
                    }else
                    {
                        Toast.makeText(changePasswordActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                    }
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    if (result == SMSSDK.RESULT_COMPLETE) {
                    Toast.makeText(getApplicationContext(), "验证码已经发送",
                            Toast.LENGTH_SHORT).show();
                    }
                   else {
                        ((Throwable) data).printStackTrace();
                    }
                } else if (result == SMSSDK.RESULT_ERROR) {
                    try {
                        Throwable throwable = (Throwable) data;
                        throwable.printStackTrace();
                        JSONObject object = new JSONObject(throwable.getMessage());
                        String des = object.optString("detail");//错误描述
                        int status = object.optInt("status");//错误代码
                        if (status > 0 && !TextUtils.isEmpty(des)) {
                            Toast.makeText(changePasswordActivity.this, des, Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } catch (Exception e) {
                    //do something
                        Toast.makeText(changePasswordActivity.this, "验证失败，请重试", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    };


    EventHandler eventHandler = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object data) {
            Message msg = new Message();
            msg.arg1 = event;
            msg.arg2 = result;
            msg.obj = data;
            handler.sendMessage(msg);
        }
    };
    // 使用完EventHandler需注销，否则可能出现内存泄漏
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }
}