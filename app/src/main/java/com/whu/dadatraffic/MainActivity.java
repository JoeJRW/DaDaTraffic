package com.whu.dadatraffic;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.whu.dadatraffic.Activity.LoginActivity;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);

        initActionBar();

        RelativeLayout userLayout = findViewById(R.id.user_layout);

        userLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Todo 编写界面跳换代码
                //代码示例
                //Intent intent=new Intent(MainActivity.this, LoginActivity.class);
                //startActivity(intent);
            }
        });

        RelativeLayout orderLayout = findViewById(R.id.order_layout);
        orderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Todo 编写界面跳换代码
                //代码示例
                //Intent intent=new Intent(MainActivity.this, LoginActivity.class);
                //startActivity(intent);
            }
        });

        RelativeLayout walletLayout = findViewById(R.id.wallet_layout);
        walletLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Todo 编写界面跳换代码
                //代码示例
                //Intent intent=new Intent(MainActivity.this, LoginActivity.class);
                //startActivity(intent);
            }
        });

        RelativeLayout settingLayout = findViewById(R.id.setting_layout);
        settingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Todo 编写界面跳换代码
                //代码示例
                //Intent intent=new Intent(MainActivity.this, LoginActivity.class);
                //startActivity(intent);
            }
        });
    }

    private void initActionBar() {
        //1.获取 actionbar 对象
        ActionBar actionBar = getSupportActionBar();
        //2.设置 图标、标题
        actionBar.setTitle(R.string.app_name);
        actionBar.setSubtitle("祝您一路顺风！");
        //3.启用、显示 home 按钮
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        //4.替换 home 按钮的图标 （现在显示的是 三条横线 符号）
        toggle = new ActionBarDrawerToggle(this,
                drawerLayout, 0, 0);
        //设置 actionbar 和 drawerlayout 同步状态
        toggle.syncState();
        //5.三条横线 添加动画 （现在显示的是 三条横线与←符号切换的效果）
        addAnamator(toggle);
    }

    private void addAnamator(ActionBarDrawerToggle toggle) {
        drawerLayout.setDrawerListener(toggle);
    }

    /**
     * 点击 actionbar 的 home 按钮，会执行该方法
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home://点击 actionbar 的 home 按钮的点击事件
                setHomeButtonState(item);
                //或
//                setHomeButtonState();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 设置 actionbar 的 home 按钮的点击事件
     *
     * @param item
     */
    private void setHomeButtonState(MenuItem item) {
        toggle.onOptionsItemSelected(item);
    }




}