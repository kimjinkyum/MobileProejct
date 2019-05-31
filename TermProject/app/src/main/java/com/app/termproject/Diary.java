package com.app.termproject;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class Diary extends AppCompatActivity {

    LookMap lookMap;
    LookDiary lookDiary;
    LookPIN lookPhoto;
    ListView listView;
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_diary);

        lookMap = new LookMap();
        lookDiary=new LookDiary();
        lookPhoto=new LookPIN();
        listView=findViewById(R.id.diaryList);
        frameLayout=findViewById(R.id.contentContainer);

        //앞에서 보낸 pinnumber 가져오기
        Intent intent=getIntent();
        Bundle bundle =intent.getExtras();
        String string = bundle.getString("pinnumber");

        Toast.makeText(getApplicationContext(), string+"가 선택되었습니다.", Toast.LENGTH_SHORT).show();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contentContainer,lookDiary).commit();

        BottomNavigationView bottomNavigationView=(BottomNavigationView)findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.menu_diary:
                        getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer,lookDiary).commit();

                        return true;
                    case R.id.menu_photo:
                        getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer,lookPhoto).commit();

                        return true;
                    case R.id.menu_map:
                        getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer,lookMap).commit();

                        return true;
                }
                return false;
            }
        });

    }
}
