package com.app.termproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class CreateDiary extends AppCompatActivity {
    Button button;
    EditText diaryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creatediary);

        diaryName = findViewById(R.id.diaryName);
        button = findViewById(R.id.checkDiary);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                String name = diaryName.getText().toString();
                Log.d("co", name);
                i.putExtra("diary_name", name);
                setResult(RESULT_OK, i);
                finish();
            }
        });


        //받은 다이어리 이름을 DB에 저장할 것
    }
}