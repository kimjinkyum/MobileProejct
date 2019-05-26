package com.app.termproject;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.os.Message;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class Basic extends AppCompatActivity {

    NotFound notFound;
    ImageButton createButton;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference;
    LinearLayout con;
    ListView diaryView;
    EditText test;


    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Intent intent = new Intent(Basic.this, Diary.class);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_basic);
        createButton = findViewById(R.id.createDiary);
        con = findViewById(R.id.container);
        diaryView = findViewById(R.id.diaryView);
        list = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        diaryView.setAdapter(adapter);

//        Sub_Basic sub;

        showDiaryList();

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
                SearchingPIN searchingPIN=new SearchingPIN(this);
                searchingPIN.setDialogListener(new SearchingPIN.SearchingPINListener() {
                    @Override
                    public void onPositiveClicked(String pin) {
                        String k = pin;
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String uid = user.getUid();
                        String email = user.getEmail();
                        Log.d("ddd", k);
                        //diary 이름을 모르겠음/ 이름까지 넣어야 writeOld 함수에서 해결할듯?
                        GetDiary d = new GetDiary(uid,email,k);
                        d.writeOld(uid, email, k);
                        d.isPin();
                        if(d.is!=true){
                            notFound=NotFound.newInstance("null");
                            notFound.show(getSupportFragmentManager(), "dialog");

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
                adapter.clear();
                for (DataSnapshot message : dataSnapshot.getChildren()) {
                    //Log.d("ddd", message.child(message.getKey()).getValue().toString());
                    Log.d("ddd", message.getKey());
                    String value = message.getKey();
                    String diaryname = dataSnapshot.child(value).child("diaryname").getValue().toString();
                    Log.d("Ddd", diaryname);
                    list.add(value);
                    adapter.add(diaryname);

                }
                adapter.notifyDataSetChanged();
                //listView.setSelection(adapter.getCount()-1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}