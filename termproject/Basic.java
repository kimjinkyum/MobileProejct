package com.app.termproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class Basic extends AppCompatActivity {

    String userEmail="";
    Button button;
    private FirebaseFirestore firestore;
    private Map<String, Object> diary=new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
        Intent pass=getIntent();
        userEmail=pass.getStringExtra("UserEmail");
        firestore=FirebaseFirestore.getInstance();
        button=findViewById(R.id.createDiary);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //다이어리 생성과 동시에 여기 밑에 DB 연동 필요
                Sub_Basic sub=new Sub_Basic(getApplicationContext());
                LinearLayout con=findViewById(R.id.diaryView);
                con.addView(sub);
                Toast.makeText(Basic.this, randomDictionary(), Toast.LENGTH_SHORT).show();
                createDB();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.basic_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.logOut:
            Intent intent=new Intent(Basic.this,LoginActivity.class);
            startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public String randomDictionary()
    {
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        String pinnumber=user.getUid();
        Toast.makeText(this,pinnumber,Toast.LENGTH_LONG).show();
        return pinnumber;
    }
    public int randomDiaryPinNumber()
    {
        int k=new Random().nextInt();
        if(k<0)
            k=k*-1;
        int r=new Random().nextInt(500);
        int m=new Random().nextInt(10);
        return k+(m*r);
    }
    public void createDB()
    {
        int pin=randomDiaryPinNumber();
        diary.put("userEmail",userEmail);
        diary.put("diaryName","abc");
        diary.put("diaryPin",pin);


        firestore.collection("Diary").document(randomDictionary()+" "+Integer.toString(pin)).set(diary).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid)
            {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });




                /*addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference)
            {
                Log.d("k", "DocumentSnapshot added with ID: " + documentReference.getId());

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("k", "error");

            }
        });*/
    }

}
