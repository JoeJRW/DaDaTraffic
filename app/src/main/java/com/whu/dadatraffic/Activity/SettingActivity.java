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

import com.whu.dadatraffic.MainActivity;
import com.whu.dadatraffic.R;

public class SettingActivity extends AppCompatActivity {

    private Button button1;
    private Button button4;
    private  ImageButton button2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        button1=(Button)findViewById(R.id.button); //账号与安全按钮
        button4=findViewById(R.id.button4);

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
}