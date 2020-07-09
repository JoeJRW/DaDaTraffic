package com.whu.dadatraffic.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.Timer;
import java.util.TimerTask;

public class OrderendActivity extends AppCompatActivity {
    public float score;           //记录星级评分
    public String tourComment;    //记录行程意见及建议
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
        String driverFistName="某";   //需修改-------------------------------------------------------
        CharSequence driverName=driverFistName+"师傅";
        TextView textView1=findViewById(R.id.drivername3);
        textView1.setText(driverName);

        //显示司机评分
        double driverScore=5.0;    //需修改----------------------------------------------------------
        CharSequence ScoreText=String.valueOf(driverScore);
        TextView textView2=findViewById(R.id.driverscore3);
        textView2.setText(ScoreText);

        //点击投诉按钮跳转投诉界面
        Button bt1=findViewById(R.id.end_tousu_button);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(OrderendActivity.this,TousuActivity.class);
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
                score=rating.getRating();
                //记录行程评价文本信息
                tourComment=tour_Comment.getText().toString();
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

        //点击电话按钮，对司机进行拨号-----------------------------------------------------------------
        ImageButton imageButton=findViewById(R.id.calldriver3);
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


    }
}