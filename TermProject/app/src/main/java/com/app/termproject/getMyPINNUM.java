package com.app.termproject;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class getMyPINNUM extends Dialog {
    private TextView textView;
    private Context context;
    private String pinNum;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference ;

    public getMyPINNUM(Context context,String pin){
        super(context);
        this.context=context;
        this.pinNum=pin;
    }

    private getMyPINNUM.getPINListener getPINListener;

    interface getPINListener{
    }

    public void setDialogListener(getMyPINNUM.getPINListener getPINListener){
        this.getPINListener=getPINListener;
    }


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_get_my_pinnum);

        textView=findViewById(R.id.getPinNum);

        textView.setText(pinNum);
    }

}