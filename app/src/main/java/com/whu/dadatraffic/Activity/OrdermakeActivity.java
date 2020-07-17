/*
*author: 李俊
*create: time: 2020-07-08
*update: time:
*/

package com.whu.dadatraffic.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.webkit.WebView;
import android.widget.Toast;

import com.whu.dadatraffic.R;

public class OrdermakeActivity extends AppCompatActivity {
    public static final int REQUEST_CALL_PERMISSION = 10111; //拨号请求码
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordermake);

        //隐藏标题栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        //显示司机姓别
        String driverFistName = "某";   //版本2修改-------------------------------------------------------
        CharSequence driverName = driverFistName + "师傅";
        TextView textView1 = findViewById(R.id.drivername1);
        textView1.setText(driverName);

        //显示司机车牌号               //版本2修改-------------------------------------------------------
        String carID = "鄂A123456";
        TextView carID1 = findViewById(R.id.carID1);
        carID1.setText(carID);

        //显示司机评分
        double driverScore = 5.0;    //版本2修改----------------------------------------------------------
        CharSequence ScoreText = String.valueOf(driverScore);
        TextView textView2 = findViewById(R.id.driverscore1);
        textView2.setText(ScoreText);


        //点击投诉按钮跳转投诉界面
        Button bt = findViewById(R.id.ing_tousu_button);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrdermakeActivity.this, TousuActivity.class);
                startActivity(intent);
            }
        });

        //点击电话按钮，对司机进行拨号
        ImageButton imageButton = findViewById(R.id.calldriver1);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取输入的电话号码
                String phone = "13871142476";//--------------------------------------需获取司机电话
                Context context = OrdermakeActivity.this;
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    context.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(context, "请检查您的手机，无法拨打电话！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //获取司机结束行程、收费动作，跳转至支付页面----------------版本2添加-----------------------------
    }
}