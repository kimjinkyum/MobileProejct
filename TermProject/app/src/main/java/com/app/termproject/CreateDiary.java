package com.app.termproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

public class CreateDiary extends AppCompatActivity {

    EditText diaryName;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creatediary);

        diaryName=findViewById(R.id.diaryName);

        //받은 다이어리 이름을 DB에 저장할 것
    }
}
