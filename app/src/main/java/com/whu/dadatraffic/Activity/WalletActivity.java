package com.whu.dadatraffic.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.TextView;

import com.whu.dadatraffic.MainActivity;
import com.whu.dadatraffic.R;

public class WalletActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        Button ticketBtn = (Button) findViewById(R.id.ticketBtn);//优惠券按钮
        ImageButton backBtn = (ImageButton) findViewById(R.id.BackBtn) ;
        ticketBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到优惠券界面
                //定义跳转对象
                Intent intentToTicket = new Intent();
                //设置跳转的起始界面和目的界面
                intentToTicket.setClass(WalletActivity.this, TicketActivity.class);
                //启动跳转
                startActivity(intentToTicket);
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //实现返回
                finish();
            }
        });
    }
}