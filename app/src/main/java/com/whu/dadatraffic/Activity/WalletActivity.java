package com.whu.dadatraffic.Activity;
/*
 *author：张朝勋
 * create time：7/6
 * update time: 7/16
 */
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.whu.dadatraffic.R;
import com.whu.dadatraffic.Service.TicketService;
import com.whu.dadatraffic.Service.UserService;

import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static java.lang.String.*;


public class WalletActivity extends AppCompatActivity {
    public static WalletActivity instance;
    TicketService ticketService = new TicketService();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        RelativeLayout ticketLayout = findViewById(R.id.wticket_layout);//优惠券按钮
        instance = this;
        ticketLayout.setOnClickListener(new View.OnClickListener() {
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
                //TODO 跳转到充值界面
                //定义跳转对象
                //Intent intentToCharge = new Intent();
                //设置跳转的起始界面和目的界面
                //intentToCharge.setClass(WalletActivity.this, MarketActivity.class);
                //启动跳转
                //startActivity(intentToCharge);
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

    @Override
    protected void onResume() {
        ticketService.queryAllTicket(true);
        super.onResume();
        TextView scoreTv = (TextView)findViewById(R.id.score);
        scoreTv.setText(UserService.curUser.getCredit()+"分");

    }

    public void showCount(){
        TextView ticketTv = (TextView)findViewById(R.id.wticketCount);
        ticketTv.setText(TicketService.ticketList.size()+"张");
    }

    public void onClick(View view) {
    }
}