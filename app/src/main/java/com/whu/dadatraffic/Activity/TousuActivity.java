package com.whu.dadatraffic.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.whu.dadatraffic.R;

public class TousuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tousu);

        //点击提交按钮，显示提交成功
        Button bt1=findViewById(R.id.submit_tousu);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TousuActivity.this,"提交成功",Toast.LENGTH_SHORT).show();
            }
        });
    }
}