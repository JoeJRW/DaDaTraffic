package com.whu.dadatraffic.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.whu.dadatraffic.R;

public class OrdermakeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordermake);

        //点击投诉按钮跳转投诉界面
        Button bt=findViewById(R.id.ing_tousu_button);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(OrdermakeActivity.this,TousuActivity.class);
                startActivity(intent);
            }
        });
    }
}