/*
*author: 李俊
*create time: 2020-07-09
*update time:2020-07-18
*/

package com.whu.dadatraffic.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.media.Rating;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.whu.dadatraffic.R;
import com.whu.dadatraffic.MainActivity;
import com.whu.dadatraffic.Service.OrderService;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

public class OrderendActivity extends AppCompatActivity {
    private OrderService orderService = new OrderService();
    public double score=0.0;           //记录星级评分
    public String tourComment="";    //记录行程意见及建议
    private RatingBar rating;
    private EditText tour_Comment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderend);

        //隐藏标题栏
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }

        //显示司机姓别
        String driverFistName = OrderService.curOrder.getDriverName().substring(0,1);   //7.21修改------------------------------------------------------------------
        CharSequence driverName=driverFistName+"师傅";
        TextView textView1=findViewById(R.id.drivername3);
        textView1.setText(driverName);

        //显示司机车牌号
        String carID = OrderService.curOrder.getCarID(); //7.21修改-------------------------------------------------------
        TextView carID3=findViewById(R.id.carID3);
        carID3.setText(carID);

        //显示司机评分
        DecimalFormat df =new DecimalFormat("#.0");
        double driverScore = Double.parseDouble(df.format(OrderService.curOrder.getDriverScore()));    //7.21修改----------------------------------------
        CharSequence ScoreText=String.valueOf(driverScore);
        TextView textView2=findViewById(R.id.driverscore3);
        textView2.setText(ScoreText);

        //点击投诉按钮跳转投诉界面
        Button bt1=findViewById(R.id.end_tousu_button);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(OrderendActivity.this,TousuActivity.class);
                //传递数据到投诉界面
                intent.putExtra("driverPhone",OrderService.curOrder.getDriverPhone());
                startActivity(intent);
            }
        });

        //获取星级评分
        rating=findViewById(R.id.score);

        //获取行程评价文本输入信息
        tour_Comment=findViewById(R.id.tour_comment);

        //点击提交按钮，显示提交成功,并跳转回主页面
        Button bt2=findViewById(R.id.submitcomment);
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //记录星级得分
                DecimalFormat df =new DecimalFormat("#.0");
                score=Double.parseDouble(df.format(rating.getRating()));
                //记录行程评价文本信息
                tourComment=tour_Comment.getText().toString();
                //将评分和评价发送到服务器
                orderService.evaluate((int)score,tourComment);
                Toast.makeText(OrderendActivity.this,"提交成功",Toast.LENGTH_SHORT).show();
                Timer timer = new Timer();
                timer.schedule(task, 3000);
            }
            TimerTask task=new TimerTask() {
                @Override
                public void run() {
                    startActivity(new Intent(OrderendActivity.this, MainActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            };
        });

        //点击电话按钮，对司机进行拨号-----------------------版本2修改---------------------------------
        ImageButton imageButton=findViewById(R.id.calldriver3);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = OrderService.curOrder.getDriverPhone();       //7.21修改-------------------------------
                Context context = OrderendActivity.this;
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    context.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(context, "请检查您的手机，无法拨打电话！", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}