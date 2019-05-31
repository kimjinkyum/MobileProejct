package com.app.termproject;

import android.app.Application;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DiaryDetail extends AppCompatActivity
{
    Button deleteButton;
    Button editButton;
    String pinnumber;
    String key;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_detail);
        TextView diaryTitle = (TextView)findViewById(R.id.textView1);
        TextView diaryDate = (TextView)findViewById(R.id.textView2);
        ImageView iv = (ImageView)findViewById(R.id.imageView1);
        Intent intent = getIntent(); // 보내온 Intent를 얻는다
        diaryTitle.setText(intent.getStringExtra("name"));
        diaryDate.setText(intent.getStringExtra("content"));
        String image=intent.getStringExtra("uri");
       key=intent.getStringExtra("key");
         pinnumber=intent.getStringExtra("pinnumber");
        Glide.with(getApplicationContext()).load(image).into(iv);
        deleteButton=findViewById(R.id.postDeleteButton);
        editButton=findViewById(R.id.postEditButton);

        deleteButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                delete();
            }
        });
    }
    public void delete()
    {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference;
        databaseReference = firebaseDatabase.getReference("diary").child(pinnumber).child(key);
        databaseReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(),"삭제 완료",Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}
