package com.app.termproject;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AlertCancel extends Dialog implements View.OnClickListener {
    private EditText editText;
    private Button positiveButton;
    private Button negativeButton;
    private Context context;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference ;

    public AlertCancel(Context context){
        super(context);
        this.context=context;
    }

    private AlertCancelListener AlertCancelListener;

    interface AlertCancelListener{
        void onPositiveClicked(String pin);
        void onNegativeClicked();
    }
    public void setDialogListener(AlertCancelListener searchingPINListener){
        this.AlertCancelListener=searchingPINListener;
    }

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frgment_alert_cancel);
        positiveButton=findViewById(R.id.mapButton);
        editText=findViewById(R.id.mapEdit);
        positiveButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.positiveButton:
                String pin=editText.getText().toString();
                AlertCancelListener.onPositiveClicked(pin);
                dismiss();
                break;
            case R.id.negativeButton:
                cancel();
                break;
        }
    }
}
