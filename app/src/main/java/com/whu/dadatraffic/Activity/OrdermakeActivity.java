/*
*author: 李俊
*create: time: 2020-07-08
*update: time:
*/

package com.whu.dadatraffic.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.whu.dadatraffic.R;

public class OrdermakeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordermake);

        //隐藏标题栏
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }

        //显示司机姓别
        String driverFistName="某";   //版本2修改-------------------------------------------------------
        CharSequence driverName=driverFistName+"师傅";
        TextView textView1=findViewById(R.id.drivername1);
        textView1.setText(driverName);

        //显示司机车牌号               //版本2修改-------------------------------------------------------
        String carID="鄂A123456";
        TextView carID1=findViewById(R.id.carID1);
        carID1.setText(carID);

        //显示司机评分
        double driverScore=5.0;    //版本2修改----------------------------------------------------------
        CharSequence ScoreText=String.valueOf(driverScore);
        TextView textView2=findViewById(R.id.driverscore1);
        textView2.setText(ScoreText);


        //点击投诉按钮跳转投诉界面
        Button bt=findViewById(R.id.ing_tousu_button);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(OrdermakeActivity.this,TousuActivity.class);
                startActivity(intent);
            }
        });

        //点击电话按钮，对司机进行拨号--------------------------------版本2修改-----------------------
        ImageButton imageButton=findViewById(R.id.calldriver1);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取输入的电话号码
                //String phone = et_phone.getText().toString().trim();
                //创建打电话的意图
                //Intent intent = new Intent();
                //设置拨打电话的动作
                //intent.setAction(Intent.ACTION_CALL);
                //设置拨打电话的号码
                //intent.setData(Uri.parse("tel:" + phone));
                //开启打电话的意图
                //startActivity(intent);
            }
        });

        //获取司机结束行程、收费动作，跳转至支付页面----------------版本2添加-----------------------------

    }
}