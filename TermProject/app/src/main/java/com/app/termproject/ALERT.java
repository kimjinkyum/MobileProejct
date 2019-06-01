package com.app.termproject;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class ALERT extends Dialog implements View.OnClickListener {
    private TextView textView;
    private Button button;
    private Context context;
    String string;

    public ALERT(Context context,String string) {
        super(context);
        this.context = context;
        this.string=string;
    }

    private ALERT.ALERTListener alertListener;

    interface ALERTListener {
        void onButtonClicked();
    }

    public void setDialogListener(ALERT.ALERTListener alertListener) {
        this.alertListener = alertListener;
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_alert);

        button = findViewById(R.id.button);
        textView = findViewById(R.id.textChange);
        textView.setText(string);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.button)
        {
            alertListener.onButtonClicked();
            dismiss();
        }
    }
}
