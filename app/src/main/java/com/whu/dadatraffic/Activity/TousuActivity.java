/*
*author: 李俊
*create: time: 2020-07-09
*update：time: 2020-07-21 李俊
*/

package com.whu.dadatraffic.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.whu.dadatraffic.MainActivity;
import com.whu.dadatraffic.R;
import com.whu.dadatraffic.Service.UserService;

import java.util.Timer;
import java.util.TimerTask;

public class TousuActivity extends AppCompatActivity {
    private String driverPhone = "";//投诉的司机的手机号
    public String tousuInf="";   //记录投诉信息
    public String tousuCommentText="";   //记录投诉文本信息
    private EditText tousu_comment_text;
    private CheckBox tousu1,tousu2,tousu3,tousu4,tousu5,tousu6,tousu7,tousu8,tousu9,tousu10,tousu11,tousu12;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tousu);

        //隐藏标题栏
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }

        //获取跳转前界面传递的数据
        driverPhone=getIntent().getStringExtra("driverPhone");

        //点击提交按钮，获取投诉选择内容和文本信息，显示提交成功
        Button submitTousu=findViewById(R.id.submit_tousu);
        tousu_comment_text=findViewById(R.id.tousu_comment);   //投诉文本框信息
        tousu1=findViewById(R.id.tousu1);
        tousu2=findViewById(R.id.tousu2);
        tousu3=findViewById(R.id.tousu3);
        tousu4=findViewById(R.id.tousu4);
        tousu5=findViewById(R.id.tousu5);
        tousu6=findViewById(R.id.tousu6);
        tousu7=findViewById(R.id.tousu7);
        tousu8=findViewById(R.id.tousu8);
        tousu9=findViewById(R.id.tousu9);
        tousu10=findViewById(R.id.tousu10);
        tousu11=findViewById(R.id.tousu11);
        tousu12=findViewById(R.id.tousu12);
        submitTousu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tousu1.isChecked()){
                    tousuInf+=tousu1.getText().toString()+"、";
                }
                if(tousu2.isChecked()){
                    tousuInf+=tousu2.getText().toString()+"、";
                }
                if(tousu3.isChecked()){
                    tousuInf+=tousu3.getText().toString()+"、";
                }
                if(tousu4.isChecked()){
                    tousuInf+=tousu4.getText().toString()+"、";
                }
                if(tousu5.isChecked()){
                    tousuInf+=tousu5.getText().toString()+"、";
                }
                if(tousu6.isChecked()){
                    tousuInf+=tousu6.getText().toString()+"、";
                }
                if(tousu7.isChecked()){
                    tousuInf+=tousu7.getText().toString()+"、";
                }
                if(tousu8.isChecked()){
                    tousuInf+=tousu8.getText().toString()+"、";
                }
                if(tousu9.isChecked()){
                    tousuInf+=tousu9.getText().toString()+"、";
                }
                if(tousu10.isChecked()){
                    tousuInf+=tousu10.getText().toString()+"、";
                }
                if(tousu11.isChecked()){
                    tousuInf+=tousu11.getText().toString()+"、";
                }
                if(tousu12.isChecked()){
                    tousuInf+=tousu12.getText().toString()+"、";
                }
                //选项不能为空
                if(tousuInf.equals("")){
                    Toast.makeText(TousuActivity.this,"请选择投诉内容",Toast.LENGTH_SHORT).show();
                    return;
                } else
                    tousuInf=tousuInf.substring(0,tousuInf.length()-1);
                tousuCommentText=tousu_comment_text.getText().toString();
                //将投诉信息发送到服务器
                new UserService().complain(driverPhone,tousuInf+"---"+tousuCommentText);
                Toast.makeText(TousuActivity.this,"提交成功",Toast.LENGTH_SHORT).show();
                //提交完成后，等待1秒跳转到上层界面
                Timer timer = new Timer();
                timer.schedule(task, 1000);
            }
            TimerTask task=new TimerTask() {
                @Override
                public void run() {
                    finish();
                }
            };
        });

        //点击返回按钮，回到上层界面
        ImageButton imageButton1=findViewById(R.id.tousu_back);
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}