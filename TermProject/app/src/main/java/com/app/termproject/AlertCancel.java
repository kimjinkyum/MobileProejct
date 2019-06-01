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

public class AlertCancel extends Dialog implements View.OnClickListener
{
    private TextView textView;
    String string;
    private Button positiveButton;
    private Button negativeButton;
    private Context context;


    public AlertCancel(Context context,String string){
        super(context);
        this.context=context;
        this.string=string;
    }

    private AlertCancel.AlertCancelListener alertCancelListener;

    interface AlertCancelListener{
        void onPositiveClicked();
        void onNegativeClicked();
    }
    public void setDialogListener(AlertCancel.AlertCancelListener alertCancelListener){
        this.alertCancelListener=alertCancelListener;
    }

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frgment_alert_cancel);
        textView = findViewById(R.id.cancelChange);
        textView.setText(string);
        positiveButton=findViewById(R.id.cancelbutton);
        negativeButton=findViewById(R.id.cancelbutton1);
        positiveButton.setOnClickListener(this);
        negativeButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.positiveButton:
                alertCancelListener.onPositiveClicked();
                dismiss();
                break;
            case R.id.negativeButton:
                cancel();
                break;
        }
    }
}
