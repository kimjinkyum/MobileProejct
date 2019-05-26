package com.app.termproject;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SearchingPIN extends Dialog implements View.OnClickListener {
    private EditText editText;
    private Button positiveButton;
    private Button negativeButton;
    private Context context;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference ;

    public SearchingPIN(Context context){
        super(context);
        this.context=context;
    }

    private SearchingPINListener searchingPINListener;

    interface SearchingPINListener{
        void onPositiveClicked(String pin);
        void onNegativeClicked();
    }

    public void setDialogListener(SearchingPINListener searchingPINListener){
        this.searchingPINListener=searchingPINListener;
    }


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_searching);

        positiveButton=findViewById(R.id.positiveButton);
        negativeButton=findViewById(R.id.negativeButton);
        editText=findViewById(R.id.pinNum);

        positiveButton.setOnClickListener(this);
        negativeButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.positiveButton:
                String pin=editText.getText().toString();

                searchingPINListener.onPositiveClicked(pin);
                dismiss();
                break;
            case R.id.negativeButton:
                cancel();
                break;
        }
    }
}
