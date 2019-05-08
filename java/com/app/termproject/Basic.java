package com.app.termproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class Basic extends AppCompatActivity {

    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);

        button=findViewById(R.id.createDiary);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //다이어리 생성과 동시에 여기 밑에 DB 연동 필요
                Sub_Basic sub=new Sub_Basic(getApplicationContext());
                LinearLayout con=findViewById(R.id.diaryView);
                con.addView(sub);
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
            finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
