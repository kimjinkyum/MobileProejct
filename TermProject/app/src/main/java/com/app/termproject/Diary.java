package com.app.termproject;

import android.support.annotation.IdRes;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class Diary extends AppCompatActivity {

    ShowingMap showingMap;
    LookDiary lookDiary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        showingMap = new ShowingMap();
        lookDiary=new LookDiary();
        BottomBar bottomBar = findViewById(R.id.bottombar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch (tabId) {
                    case 0: {
                        transaction.replace(R.id.contentContainer,showingMap).commit();
                        break;
                    }
                    case 1:{
                        transaction.replace(R.id.contentContainer,lookDiary).commit();
                        break;
                    }
                }
            }
        });
    }
}
