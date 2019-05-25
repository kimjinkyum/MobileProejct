package com.app.termproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class DiaryDetail extends AppCompatActivity
{
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
        diaryDate.setText(intent.getStringExtra("date"));
        iv.setImageResource(intent.getIntExtra("img", 0));
    }
}
