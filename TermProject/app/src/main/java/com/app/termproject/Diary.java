package com.app.termproject;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;
import java.util.Collections;

public class Diary extends AppCompatActivity {

    LookMap lookMap;
    LookDiary lookDiary;
    LookPIN lookPIN;
    FrameLayout frameLayout;
    String pinnumber;
    ArrayList<ArrayList<String>> groupList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_diary);

        lookMap = new LookMap();
        lookDiary=new LookDiary();
        lookPIN = new LookPIN();
        //listView=findViewById(R.id.diaryList);
        frameLayout=findViewById(R.id.contentContainer);
        groupList=new ArrayList<>();

        //앞에서 보낸 pinnumber 가져오기
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        pinnumber = bundle.getString("pinnumber");
        String name=bundle.getString("name");
        Bundle bundle2 = new Bundle();
        bundle2.putString("pinnumber", pinnumber);
        bundle2.putString("name",name);
        Log.d("down","diaryclass");
        lookDiary.setArguments(bundle2);
        lookPIN.setArguments(bundle2);
        //Toast.makeText(getApplicationContext(), string+"가 선택되었습니다.", Toast.LENGTH_SHORT).show();


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contentContainer, lookDiary).commit();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_diary:
                        getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer, lookDiary).commit();

                        return true;
                    case R.id.menu_photo:
                        getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer,lookPIN).commit();

                        return true;
                    case R.id.menu_map:
                        getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer, lookMap).commit();

                        return true;
                }
                return false;
            }

        });
    }

    /*디비 읽어오는 함순데 그 map하고 diary일때 다르니까 index==0일때는 Diary한테 보내는거고 index==1일떄는 map에게*/
    public void getPostInformation(final int index) {
        int count = 0;
        Log.d("down","getPositionFunction");
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        Query databaseReference;
        databaseReference = firebaseDatabase.getReference("diary").child(pinnumber).orderByChild("date");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                ArrayList<String>postNameList=new ArrayList<>();
                ArrayList<String>postContentList=new ArrayList<>();
                ArrayList<String>latitudeList=new ArrayList<>();
                ArrayList<String>longitudeList=new ArrayList<>();
                ArrayList<String>uriList=new ArrayList<>();
                ArrayList<String>postKey=new ArrayList<>();
                ArrayList<String>fileName=new ArrayList<>();
                ArrayList<String>date=new ArrayList<>();
                ArrayList<String>weather=new ArrayList<>();
                groupList.clear();
                for (DataSnapshot message : dataSnapshot.getChildren()) {
                    Log.d("ccc", message.getKey());
                    String value = message.getKey();
                    if (!value.equals("diaryname")) {
                        String postName = dataSnapshot.child(value).child("postName").getValue().toString();
                        postKey.add(value);
                        postNameList.add(postName);
                        postContentList.add(dataSnapshot.child(value).child("content").getValue().toString());
                        latitudeList.add((dataSnapshot.child(value).child("latitude").getValue().toString()));
                        longitudeList.add((dataSnapshot.child(value).child("longitude").getValue().toString()));
                        uriList.add(dataSnapshot.child(value).child("uri").getValue().toString());
                        fileName.add(dataSnapshot.child(value).child("fileName").getValue().toString());
                        date.add(dataSnapshot.child(value).child("date").getValue().toString());
                        weather.add(dataSnapshot.child(value).child("weather").getValue().toString());
                        Log.d("weather",weather.get(0));
                        //adapter.add(diaryname);
                    }
                    //list.add(value);
                    //adapter.add(diaryname);
                }
                groupList.add(postNameList);
                groupList.add(postContentList);
                groupList.add(uriList);
                groupList.add(latitudeList);
                groupList.add(longitudeList);
                groupList.add(postKey);
                groupList.add(fileName);
                groupList.add(date);
                groupList.add(weather);
                if (index==0)
                {
                    lookDiary.show(groupList);

                }
                else if(index==1)
                {
                    lookMap.show(groupList);

                }
                //adapter.notifyDataSetChanged();
                //listView.setSelection(adapter.getCount()-1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}