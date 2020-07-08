package com.whu.dadatraffic.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.whu.dadatraffic.R;
import com.whu.dadatraffic.MainActivity;

import java.util.Timer;
import java.util.TimerTask;

public class OrderendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderend);

        //点击投诉按钮跳转投诉界面
        Button bt1=findViewById(R.id.end_tousu_button);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(OrderendActivity.this,TousuActivity.class);
                startActivity(intent);
            }
        });

        //点击提交按钮，显示提交成功
        Button bt2=findViewById(R.id.submitcomment);
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(OrderendActivity.this,"提交成功",Toast.LENGTH_SHORT).show();
                Timer timer = new Timer();
                timer.schedule(task, 3000);
            }
            TimerTask task=new TimerTask() {
                @Override
                public void run() {
                    startActivity(new Intent(OrderendActivity.this, MainActivity.class));
                }
            };
        });
    }
}