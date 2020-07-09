package com.whu.dadatraffic.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.whu.dadatraffic.MainActivity;
import com.whu.dadatraffic.R;

public class AccountAndSecurity extends AppCompatActivity {

    private ImageButton button1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_and_security);
        button1=(ImageButton)findViewById(R.id.backToSetting);//返回按钮
        //跳转至设置页面
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToSetting = new Intent();
                //设置跳转的起始界面和目的界面
                intentToSetting.setClass(AccountAndSecurity.this, SettingActivity.class);
                //启动跳转
                startActivity(intentToSetting);
            }
        });
    }
}