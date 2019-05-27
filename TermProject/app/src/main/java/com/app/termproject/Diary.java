package com.app.termproject;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.IdRes;
import android.support.v4.app.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
    ListView listView;
    FrameLayout frameLayout;
    String diaryPinNumber;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_diary);


        lookMap = new LookMap();
        lookDiary=new LookDiary();
        lookPhoto=new LookPhoto();
        listView=findViewById(R.id.diaryList);
        frameLayout=findViewById(R.id.contentContainer);
        Intent passIntent=getIntent();
        diaryPinNumber=passIntent.getStringExtra("pinnumber");
        uid=passIntent.getStringExtra("uid");
        Bundle bundle=new Bundle();
        bundle.putString("pinnumber",diaryPinNumber);
        bundle.putString("uid",uid);

        lookDiary.setArguments(bundle);



        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contentContainer,lookDiary).commit();

        BottomBar bottomBar = findViewById(R.id.bottombar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.tabDiary:
                        {
                            Bundle bundle=new Bundle();
                            bundle.putString("pinnumber",diaryPinNumber);
                            bundle.putString("uid",uid);

                            lookDiary.setArguments(bundle);
                            getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer,lookDiary).commit();

                        break;
                    }
                    case R.id.tabPhoto:{
                        getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer,lookPhoto).commit();
                        break;
                    }
                    case R.id.tabMap:{
                        getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer, lookMap).commit();
                        break;
                    }
                }
            }
        });
    }
}
