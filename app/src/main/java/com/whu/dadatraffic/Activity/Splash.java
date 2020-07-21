package com.whu.dadatraffic.Activity;
/**
 * author:王子皓
 * create time：2020.07.07
 * 功能为应用启动界面
 */
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.whu.dadatraffic.MainActivity;

import com.whu.dadatraffic.R;

import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity {

    public static Splash instance = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        instance = this;
        Timer timer = new Timer();
        timer.schedule(task, 2000);
    }

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            startActivity(new Intent(Splash.this, DriverMainActivity.class));
        }
    };

}
