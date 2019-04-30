package com.app.termproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    Button loginButton;//login Button
    EditText idText;//EditText(id)
    EditText passwordText;//EditText(password)
    String id;//value of edit text(id)
    String password;//value of edit text(password)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton=findViewById(R.id.loginButton);
        loginButton.setEnabled(false);
        idText=(EditText)findViewById(R.id.idText);
        passwordText=findViewById(R.id.passwordText);

       checkEnter(passwordText);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                id=idText.getText().toString();

            }
        });

    }



    /*Edit text에 값이 입력 됬는지 확인하여
    * 입력이 됬으면 login버튼을 활성화 시키는 거*/
    public void checkEnter(EditText edit)
    {

        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                 }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if(idText.getText().toString().length()>0)
                    loginButton.setEnabled(true);

            }
        });

          }


}
