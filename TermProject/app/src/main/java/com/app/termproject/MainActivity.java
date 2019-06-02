package com.app.termproject;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        matchImage();

    }

    /*기기마다 로고 이미지 사이즈 조정
    * */
    protected void matchImage()
    {

        DisplayMetrics metrics= new DisplayMetrics();

        WindowManager windowManager=(WindowManager)getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);

        ImageView img= (ImageView) findViewById(R.id.logo);
        LinearLayout.LayoutParams params=(LinearLayout.LayoutParams) img.getLayoutParams();
        params.width=metrics.widthPixels;
        params.height=metrics.heightPixels;
        img.setLayoutParams(params);
        moveToLogin();
    }

    /*2초있다가 자동으로 loginActivity 로 전환.
    * */
    protected void moveToLogin()
    {
        Handler timer=new Handler();
        timer.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(MainActivity.this,WelcomeActivity.class);
                startActivity(intent);
                finish();
            }
        },2000);

    }
}
