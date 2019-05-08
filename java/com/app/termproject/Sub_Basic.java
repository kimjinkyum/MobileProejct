package com.app.termproject;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

// 일기장에 대해 정의하는 클래스
public class Sub_Basic extends LinearLayout {

    public Sub_Basic(Context context, AttributeSet attributeSet){
        super(context,attributeSet);

        init(context);
    }

    public Sub_Basic(Context context){
        super(context);

        init(context);
    }

    public void init(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.activity_subbasic,this,true);
    }
}
