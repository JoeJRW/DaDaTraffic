/*
 *作者：施武轩 创建时间：2020.7.7 更新时间：2020.7.13
 */

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

import com.whu.dadatraffic.Base.Driver;
import com.whu.dadatraffic.Base.User;
import com.whu.dadatraffic.R;
import com.whu.dadatraffic.Service.DriverService;
import com.whu.dadatraffic.Service.UserService;
import com.whu.dadatraffic.Utils.LocalStorageUtil;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private UserService userService = new UserService();
    private DriverService driverService = new DriverService();
    public static RegisterActivity instance = null;
    private Button registerBtn = null;//确认注册按钮
    private EditText phoneNumberEt = null;//手机号输入框
    private EditText nameEt = null;//姓名输入框
    private EditText passwordEt = null;//密码输入框
    private EditText confirmPasswordEt = null;//确认密码输入框
    private TextView hintTv = null;//提示框
    private CheckBox isDriverCb_reg = null;//我是司机复选框
    private TextView carNumTv = null;
    private EditText carNumEt = null;//车牌号输入框

    private boolean isDriver = false;//表示当前注册用户身份是否是司机
    private String phoneNumber = null;
    private String name = null;
    private String password1 = null;
    private String password2 = null;
    private String carNumber = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        instance = this;

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
                    hintTv.setText("手机号格式错误");
                    //canRegister = false;
                    return;
                }

                //判断是否输入姓名
                if(name.isEmpty()){
                    hintTv.setText("请输入姓名");
                    //canRegister = false;
                    return;
                }

                //判断是否输入密码
                if(password1.isEmpty()){
                    hintTv.setText("请输入密码");
                    //canRegister = false;
                    return;
                }

                //判断确认密码与原密码是否一致
                if(!password1.equals(password2)){
                    hintTv.setText("两次输入的密码不一致");
                    //canRegister = false;
                    return;
                }

                if (isDriver){
                    carNumber = carNumEt.getText().toString();
                    if(carNumber.isEmpty()){
                        hintTv.setText("车牌号不可为空");
                        return;
                    }
                }

                if(isDriver){
                    //司机注册
                    driverService.register(new Driver(phoneNumber,password1,name,carNumber));
                }
                else {
                    //用户注册
                    userService.register(new User(phoneNumber,password1,name));
                }

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
        hintTv = (TextView)findViewById(R.id.hintTv);
        isDriverCb_reg = (CheckBox)findViewById(R.id.isDriverCheckBox_reg);
        carNumTv = (TextView)findViewById(R.id.carNumberTextView);
        carNumEt = (EditText)findViewById(R.id.carNumberEditText);

        //隐藏车牌号相关控件
        carNumEt.setVisibility(View.INVISIBLE);
        carNumTv.setVisibility(View.INVISIBLE);
    }

    //注册成功后的UI操作
    public void registerSuccess(String result){
        hintTv.setText(result);
        //跳转回登录界面
        //定义跳转对象
        Intent intentToLogin = new Intent().setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        //设置跳转的起始界面和目的界面
        intentToLogin.setClass(RegisterActivity.this, LoginActivity.class);
        //清空偏好配置
        LocalStorageUtil.clearSettingNote(LoginActivity.instance,"userPreferences");
        //把手机号保存到本地
        Map<String, String> map = new HashMap<String, String>();
        map.put("userphone", phoneNumber);
        LocalStorageUtil.saveSettingNote(LoginActivity.instance,"userPreferences",map);
        //启动跳转
        startActivity(intentToLogin);
    }

    //注册失败后的UI操作
    public void registerFail(String result)
    {
        hintTv.setText(result);
    }

}