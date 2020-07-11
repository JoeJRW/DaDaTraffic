package com.whu.dadatraffic.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.whu.dadatraffic.R;
import android.widget.Button;
import android.widget.RelativeLayout;


public class WalletActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        Button ticketBtn = (Button) findViewById(R.id.ticketBtn);//优惠券按钮
        ticketBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到优惠券界面
                //定义跳转对象
                Intent intentToTicket = new Intent();
                //设置跳转的起始界面和目的界面
                //TODO 数据库搭建完成后，判断用户优惠券数量，如果为零则跳往noneTicketActivity
                intentToTicket.setClass(WalletActivity.this, TicketActivity.class);
                //启动跳转
                startActivity(intentToTicket);
            }
        });

        RelativeLayout scoreLayout = findViewById(R.id.score_layout);//积分文本
        scoreLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到积分商城界面
                //定义跳转对象
                Intent intentToWallet = new Intent();
                //设置跳转的起始界面和目的界面
                intentToWallet.setClass(WalletActivity.this, MarketActivity.class);
                //启动跳转
                startActivity(intentToWallet);
            }
        });

        RelativeLayout moneyLayout = findViewById(R.id.money_layout);//余额文本
        moneyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到充值界面
                //定义跳转对象
                Intent intentToWallet = new Intent();
                //设置跳转的起始界面和目的界面
                intentToWallet.setClass(WalletActivity.this, MarketActivity.class);
                //启动跳转
                startActivity(intentToWallet);
            }
        });

        setCustomActionBar();
    }

//显示home按钮与标题
    private void setCustomActionBar() {
        //1.获取 actionbar 对象
        ActionBar actionBar = this.getSupportActionBar();
        //2.显示home键
        actionBar.setDisplayHomeAsUpEnabled(true);
        //3.设置标题
        actionBar.setTitle("我的钱包");
    }


    //返回父活动
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}