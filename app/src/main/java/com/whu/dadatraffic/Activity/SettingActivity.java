package com.whu.dadatraffic.Activity;
/** author：张余青
 * create:7/8
 * update:7/11
 */
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.whu.dadatraffic.MainActivity;
import com.whu.dadatraffic.R;
import com.whu.dadatraffic.Utils.LocalStorageUtil;

public class SettingActivity extends AppCompatActivity {

    private RelativeLayout button1;
    private RelativeLayout button4;
    private  ImageButton button2;
    private Button exitBtn;//退出登录按钮
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        button1=findViewById(R.id.line1); //账号与安全按钮
        button4=findViewById(R.id.line4);
        exitBtn = (Button)findViewById(R.id.exitBtn_set);

        //跳转至账号与安全
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToMain = new Intent();
                //设置跳转的起始界面和目的界面
                intentToMain.setClass(SettingActivity.this, AccountAndSecurity.class);
                //启动跳转
                startActivity(intentToMain);
            }
        });

        button2=(ImageButton)findViewById(R.id.backToMain);//返回按钮
        //跳转至主页面
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToMain = new Intent();
                //设置跳转的起始界面和目的界面
                intentToMain.setClass(SettingActivity.this, MainActivity.class);
                //启动跳转
                startActivity(intentToMain);
            }
        });

        //跳转常用地址设置
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                //设置跳转的起始界面和目的界面
                i.setClass(SettingActivity.this, AddressActivity.class);
                //启动跳转
                startActivity(i);
            }
        });

        //为退出登录按钮绑定事件
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //清除偏好设置
                LocalStorageUtil.clearSettingNote(LoginActivity.instance,"userPreferences");
                //跳转到登录界面并关闭当前所有界面
                Intent intent = new Intent(SettingActivity.this,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
    //显示home按钮
    @Override
    protected void onStart() {
        super.onStart();
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    //返回父活动
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View view) {
    }
}