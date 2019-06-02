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

public class SearchingPIN extends Dialog implements View.OnClickListener {
    private EditText editText;
    private Button positiveButton;
    private Button negativeButton;
    private Context context;
    String isadress;
    TextView textView;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference ;

    public SearchingPIN(Context context){
        super(context);
        this.context=context;
        isadress="no";
    }
    public SearchingPIN(Context context, String adress)
    {
        super(context);
        this.context=context;
        isadress="yes";
    }

    private SearchingPINListener searchingPINListener;

    interface SearchingPINListener{
        void onPositiveClicked(String pin);
        void onNegativeClicked(String address);
    }
    public void setDialogListener(SearchingPINListener searchingPINListener){
        this.searchingPINListener=searchingPINListener;
    }

    protected void onCreate(Bundle savedInstanceState){
               super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_searchingpin);
        positiveButton=findViewById(R.id.positiveButton);
        negativeButton=findViewById(R.id.negativeButton);
        textView=findViewById(R.id.insertPIN);
        editText=findViewById(R.id.pinNum);
        positiveButton.setOnClickListener(this);
        negativeButton.setOnClickListener(this);

        if(isadress=="yes")
        {
            textView.setText("사진 속에 주소가 없어요");
            editText.setHint("주소를 입력 해주세요");
            negativeButton.setText("확인");
            positiveButton.setVisibility(View.INVISIBLE);
        }
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
                String address=editText.getText().toString();
                searchingPINListener.onNegativeClicked(address);
                cancel();
                break;
        }
    }
}
