package com.app.termproject;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ALERT extends Dialog implements View.OnClickListener {
    private TextView textView;
    private Button positiveButton;
    private Button negativeButton;
    private Context context;


    public ALERT(Context context) {
        super(context);
        this.context = context;
    }

    private ALERT.ALERTListener alertListener;

    interface ALERTListener {
        void onPositiveClicked();
        void onNegativeClicked();
    }

    public void setDialogListener(ALERT.ALERTListener alertListener) {
        this.alertListener = alertListener;
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_alert);

        positiveButton = findViewById(R.id.positiveButton);
        negativeButton=findViewById(R.id.negativeButton);
        textView = findViewById(R.id.text);

        positiveButton.setOnClickListener(this);
        negativeButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.positiveButton:
                alertListener.onPositiveClicked();
                dismiss();
                break;
            case R.id.negativeButton:
                cancel();
                break;
        }
    }
}
