package com.app.termproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;


public class LoginActivity extends AppCompatActivity {

    String idToken="";
    Button loginButton;//login Button
    Button signupButton;
    EditText idText;//EditText(id)
    EditText passwordText;//EditText(password)
    String id;//value of edit text(id)
    String password;//value of edit text(password)
    LoginFragment dialog;
    InputMethodManager imm;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth=FirebaseAuth.getInstance();


        loginButton = findViewById(R.id.loginButton);
        loginButton.setEnabled(false);
        idText = (EditText) findViewById(R.id.idText);
        passwordText = (EditText) findViewById(R.id.passwordText);
        signupButton=(Button)findViewById(R.id.signupButton);
        checkEnter(passwordText);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginEvent();
            }
        });
        signupButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
        registerEdit();
    }
    /*editText 줄넘김 이벤트*/
    public void registerEdit()
    {

        idText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        idText.setInputType(InputType.TYPE_CLASS_TEXT);

        passwordText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                imm=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {

                    imm.hideSoftInputFromWindow(passwordText.getWindowToken(),0);
                    return true;
                }
                return false;
            }
        });
        passwordText.setInputType(InputType.TYPE_CLASS_TEXT);

    }
    /*Edit text에 값이 입력 됬는지 확인하여
     * 입력이 됬으면 login 버튼을 활성화 시키는 거*/
    public void checkEnter(EditText edit) {

        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (idText.getText().toString().length() > 0)
                    loginButton.setEnabled(true);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void onAuthStateChanged(@NonNull FirebaseAuth f)
    {

        FirebaseUser user=f.getCurrentUser();

        Toast.makeText(this, idToken, Toast.LENGTH_LONG).show();

    }
    /*로그인정보 맞는지 확인하는 함수
     * <추후 DB랑 연동하여 다시 함수 작성 할 필요>
     * */
    public void loginEvent() {

        id = idText.getText().toString();
        password = passwordText.getText().toString();


        /*여기서 id 비번 null이면 그거 처리도 해줘야될듯...
         * */
        firebaseAuth.signInWithEmailAndPassword(id, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //onAuthStateChanged(firebaseAuth);
                    finish();
                    FirebaseUser user=firebaseAuth.getCurrentUser();
                    Intent intent = new Intent(LoginActivity.this, Basic.class);
                    intent.putExtra("UserEmail",user.getEmail());
                    startActivity(intent);

                }
                else {
                    dialog = LoginFragment.newInstance("null");
                    dialog.show(getSupportFragmentManager(), "dialog");
                }

            }


        });


    }

}
