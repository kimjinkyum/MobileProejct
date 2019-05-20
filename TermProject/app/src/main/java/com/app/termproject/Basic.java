package com.app.termproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.os.Message;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class Basic extends AppCompatActivity {

    String userEmail = "";
    Button createButton;
    SearchingPIN dialog;

    LinearLayout con;
    private FirebaseFirestore firestore;
    ArrayList<Object>temp=new ArrayList<>();
    private Map<String, Object> diary = new HashMap<>();
    private ArrayList<Object> Directory=new ArrayList<>();


    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Intent intent = new Intent(Basic.this, Diary.class);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
        Intent pass = getIntent();
        userEmail = pass.getStringExtra("UserEmail");
        firestore = FirebaseFirestore.getInstance();
        createButton = findViewById(R.id.createDiary);
         con = findViewById(R.id.diaryView);
         getDiary();
        Sub_Basic sub;
         //여기서 반복문을 통해 DB에 있는 정보를 가져와 초기화를 한다
        //파라미터가 현재 하나인데 2개를 만들어 db정보만 넣음 될듯
        ArrayList<String> name = new ArrayList<>();
        name.add("1");
        name.add("2");
        for (int i = 0; i < name.size(); i++) {
            sub = new Sub_Basic(getApplicationContext(), name.get(i));
            con.addView(sub);
            sub.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Message msg = handler.obtainMessage();
                    handler.sendMessage(msg);
                }
            });
        }


        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Basic.this, CreateDiary.class);
                startActivity(intent);
                //다이어리 생성과 동시에 여기 밑에 DB 연동 필요
//                Toast.makeText(Basic.this, randomDictionary(), Toast.LENGTH_SHORT).show();
                createDB();


            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.basic_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void getDiary()
    {

        ArrayList<Object>sdf=new ArrayList<>();
        firestore.collection("Diary").whereEqualTo("userEmail",userEmail).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {   QueryDocumentSnapshot d;
                Sub_Basic sub_basic;
                int i=0;

                if(task.isSuccessful())
                {
                    for (QueryDocumentSnapshot document : task.getResult())
                    {
                        d=document;
                        //getDiary.add(document.getId());
                        Log.d("sdfsdf", document.getId() + " => " + document.getData());
                        temp.add(i++,d.get("diaryPin"));
                        Log.d("sdfsdfkd",temp.get(i-1).toString());
                        //test.append("\n"+temp.get(i-1).toString());
                        sub_basic = new Sub_Basic(getApplicationContext(), temp.get(i-1).toString());
                        con.addView(sub_basic);

                    }
                }
                else
                    {

                    }
                }
        });

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
    public void search(){
        dialog = SearchingPIN.newInstance("null");
        dialog.show(getSupportFragmentManager(), "dialog");

    }

    public String randomDictionary() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String pinnumber = user.getUid();
        Toast.makeText(this, pinnumber, Toast.LENGTH_LONG).show();
        return pinnumber;
    }

    public int randomDiaryPinNumber() {
        int k = new Random().nextInt();
        if (k < 0)
            k = k * -1;
        int r = new Random().nextInt(500);
        int m = new Random().nextInt(10);
        return k + (m * r);
    }

    public void createDB() {
        int pin = randomDiaryPinNumber();
        diary.put("userEmail", userEmail);
        diary.put("diaryName", "abc");
        diary.put("diaryPin", pin);


        firestore.collection("Diary").document(randomDictionary()+" "+pin).set(diary).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

}
