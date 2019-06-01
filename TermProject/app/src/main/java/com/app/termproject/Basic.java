package com.app.termproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.os.Message;
import android.widget.Toast;

import com.app.termproject.DB.GetDiary;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;


public class Basic extends AppCompatActivity {

    NotFound notFound;
    FloatingActionButton createButton;
    ArrayList<String> list;
    ArrayList<String> nameList;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference;
    GridView diaryView;
    DiaryAdapter diaryAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_basic);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            }
        }
        createButton = findViewById(R.id.createDiary);
        list = new ArrayList<>();
        nameList=new ArrayList<>();

        int[] img = new int[]{
                R.drawable.addbutton, R.drawable.addbutton, R.drawable.addbutton, R.drawable.addbutton, R.drawable.addbutton, R.drawable.addbutton, R.drawable.addbutton, R.drawable.addbutton, R.drawable.addbutton, R.drawable.addbutton};


        // 커스텀 아답타 생성
        diaryAdapter = new DiaryAdapter(this, nameList);
        diaryView = findViewById(R.id.diaryView);

        diaryView.setAdapter(diaryAdapter);  // 커스텀 아답타를 GridView 에 적용



        showDiaryList();

        createButton.show();
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Basic.this, CreateDiary.class);

                startActivityForResult(intent, 1);

            }
        });

        diaryView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("kk", list.get(position));
                Intent intent = new Intent(getApplicationContext(), Diary.class);

                //GetDiary d= new GetDiary();

                intent.putExtra("pinnumber", list.get(position));
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String name = data.getStringExtra("diary_name");
        createDB(name);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.basic_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logOut:
                Intent intent = new Intent(Basic.this, LoginActivity.class);
                startActivity(intent);

                //수정!!
            case R.id.searchingPIN:
                SearchingPIN searchingPIN = new SearchingPIN(this);
                searchingPIN.setDialogListener(new SearchingPIN.SearchingPINListener() {
                    @Override
                    public void onPositiveClicked(String pin) {
                        Toast.makeText(getApplicationContext(), pin, Toast.LENGTH_SHORT).show();

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String uid = user.getUid();
                        String email = user.getEmail();
                        Log.d("ddd", pin);
                        GetDiary getDiary = new GetDiary(uid, email, pin);
//                        getDiary.isPin();
                        if (getDiary.pinCheck())
                        {
                            //notFound = NotFound.newInstance("null");
                            //notFound.show(getSupportFragmentManager(), "dialog");
                            Log.d("pin", "correct pin");

                        } else
                            { //찾으면
                            //Toast.makeText(getApplicationContext(), "성공~", Toast.LENGTH_SHORT).show();
                            //Log.d("pin", "wrong pin");
                                //getDiary.writeOld(uid,email,pin);
                        }

                    }

                    @Override
                    public void onNegativeClicked() {

                    }
                });
                searchingPIN.show();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public int randomDiaryPinNumber() {
        int k = new Random().nextInt();
        if (k < 0)
            k = k * -1;
        int r = new Random().nextInt(500);
        int m = new Random().nextInt(10);
        return k + (m * r);
    }

    public void createDB(String diary_name) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        String email = user.getEmail();
        String name=user.getDisplayName();
        String diary_pin = Integer.toString(randomDiaryPinNumber());
        GetDiary d = new GetDiary();
        d.set(uid, diary_pin, diary_name);
        d.writeNew(uid, email, diary_pin, diary_name);
    }

    public void showDiaryList() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        databaseReference = firebaseDatabase.getReference("user-diary/" + uid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                diaryAdapter.clear();
                list.clear();
                for (DataSnapshot message : dataSnapshot.getChildren()) {
                    //Log.d("ddd", message.child(message.getKey()).getValue().toString());
                    Log.d("ddd", message.getKey());
                    String value = message.getKey();
                    String diaryname = dataSnapshot.child(value).child("diaryname").getValue().toString();
                    Log.d("Ddd", diaryname);
                    list.add(value);
                    nameList.add(diaryname);

                }
                diaryAdapter.notifyDataSetChanged();
                //listView.setSelection(adapter.getCount()-1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}