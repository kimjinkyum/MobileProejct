package com.app.termproject;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.Toast;
import android.os.Message;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class Basic extends AppCompatActivity {

    ImageButton createButton;
    SearchingPIN dialog;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference ;
    LinearLayout con;
    ListView listView;
    Button testButton;
    EditText test;
    private Map<String, Object> diary = new HashMap<>();
    HashMap<String,String> result=new HashMap<>();




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
        con = findViewById(R.id.diaryView);
        listView=findViewById(R.id.listView);
        list=new ArrayList<>();
        adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);
        Sub_Basic sub;
        //여기서 반복문을 통해 DB에 있는 정보를 가져와 초기화를 한다
        //파라미터가 현재 하나인데 2개를 만들어 db정보만 넣음 될듯

        showDiaryList();

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Basic.this, CreateDiary.class);

                startActivityForResult(intent,1);

            }
        });
        testButton=findViewById(R.id.testbutton);
        test=findViewById(R.id.test);
        testButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                String k=test.getText().toString();
                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                String uid=user.getUid();
                String email=user.getEmail();
                Log.d("ddd",k);
                GetDiary d= new GetDiary();
                d.setPin(k);
                d.setUid(uid);
                d.writeOld(uid,email,k);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("kk",list.get(position));
                Intent intent=new Intent(getApplicationContext(), Diary.class);
                //GetDiary d= new GetDiary();
                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                String uid=user.getUid();
                intent.putExtra("pinnumber",list.get(position));
                intent.putExtra("uid",uid);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String name=data.getStringExtra("diary_name");
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
                search();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //여기 DB 부탁~
    //앞에 LoginFragment와 같이 SearchingPIN을 짬
    //LoginActivity 128번째 줄 loginEvent()함수와 같이 짜면 될듯
    //이코드는 104번째줄 돋보기 화면버튼을 눌렀을 때 실행되는 함수
    public void search() {
        dialog = SearchingPIN.newInstance("null");
        dialog.show(getSupportFragmentManager(), "dialog");

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
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        String uid=user.getUid();
        String email=user.getEmail();
        String diary_pin= Integer.toString(randomDiaryPinNumber());
        GetDiary d= new GetDiary();
        d.set(uid,diary_pin,diary_name);
        d.writeNew(uid,email,diary_pin,diary_name);
    }

    public void showDiaryList()
    {
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        String uid=user.getUid();
        databaseReference=firebaseDatabase.getReference("user-diary/"+uid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                adapter.clear();
                for(DataSnapshot message: dataSnapshot.getChildren())
                {
                    //Log.d("ddd", message.child(message.getKey()).getValue().toString());
                    Log.d("ddd",message.getKey());
                    String value=message.getKey();
                    String a= dataSnapshot.child(value).child("diaryname").getValue().toString() ;
                    Log.d("Ddd",a);
                    list.add(value);
                    adapter.add(a);
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
