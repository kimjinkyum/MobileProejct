package com.app.termproject;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.FrameLayout;


// 일기장에 대해 정의하는 클래스
public class Sub_Basic extends FrameLayout {

    Button button;

     public Sub_Basic(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        init(context,"");
    }

    public Sub_Basic(Context context,String string)
    {
        super(context);
        init(context,string);
    }

    public void init(Context context,String string){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.activity_subbasic,this,true);

        button=(Button)findViewById(R.id.eachDiary);

        //여기다가 diary DB에서 가져온 이름만 집어넣자
        button.setText(string);
        button.setTextSize(30);
    }


}
