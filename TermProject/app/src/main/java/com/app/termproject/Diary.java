package com.app.termproject;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.IdRes;
import android.support.v4.app.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class Diary extends AppCompatActivity {

    LookMap lookMap;
    LookDiary lookDiary;
    LookPhoto lookPhoto;
    Button button;
    ListView listView;
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_diary);

        lookMap = new LookMap();
        lookDiary=new LookDiary();
        lookPhoto=new LookPhoto();
        button=findViewById(R.id.search);
        listView=findViewById(R.id.diaryList);
        frameLayout=findViewById(R.id.contentContainer);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Diary.this,Search.class);
                startActivity(intent);
            }
        });
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contentContainer,lookDiary).commit();
        BottomBar bottomBar = findViewById(R.id.bottombar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.tab_photo: {
                        getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer,lookPhoto).commit();
                        break;
                    }
                    case R.id.tab_diary:{
                        getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer,lookDiary).commit();
                        break;
                    }
                    case R.id.tab_map:{
                        getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer, lookMap).commit();
                        break;
                    }
                }
            }
        });
    }
}
