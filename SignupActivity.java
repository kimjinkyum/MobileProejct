package com.app.termproject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SignupActivity extends Activity {

    boolean confirmPassword=false;
    EditText signupIdText,signupNameText, signupPasswordText,signupPasswordConfirmText;
    ImageView confirmImage;
    ImageButton signupConfirmButton;
    InputMethodManager imm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        init();
    }

    /*find view object*/
    public void init()
    {
        signupIdText=findViewById(R.id.signupIdText);
        signupNameText=findViewById(R.id.signupNameText);
        signupPasswordText=findViewById(R.id.signupPasswordText);
        signupPasswordConfirmText=findViewById(R.id.signupPasswordConfirmText);
        confirmImage=findViewById(R.id.passwordConfirmImage);
        signupConfirmButton=findViewById(R.id.signupConfirmButton);
        imm=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        /*함수 호출*/
        registerEdit(signupIdText);
        registerEdit(signupNameText);
        registerEdit(signupPasswordText);
        confirmPassword();
        checkUniqueId();
        checkUniqueName();

        /**
         * 마지막 Confirm 텍스트에서 엔터키 누르면 줄넘김이 아니라 키보드 내려가게
         */
        signupPasswordConfirmText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    imm.hideSoftInputFromWindow(signupPasswordConfirmText.getWindowToken(),0);
                    return true;
                }
                return false;
            }
        });
        signupPasswordConfirmText.setInputType(InputType.TYPE_CLASS_TEXT);
        signupConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSignup();

            }
        });
    }

    /*엔터키 누르면 줄넘김이 아니라 다음으로 넘어 갈 수 있게 하는 코드 */
    public void registerEdit(EditText editText)
    {
        editText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
    }

  /*아이디 중복 체크*/
    public void checkUniqueId()
    {
        //compare=아이디 읽어오기
        signupIdText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
    /*이름 중복 체크*/
    public void checkUniqueName()
    {
        //compare=이름 읽어오기
        signupNameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    /*비밀번호 확인 입력시 같으면 초록색 체크 표시, 다르면 빨간색 X표시 */
    public void confirmPassword()
    {
        signupPasswordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(signupPasswordText.getText().toString().equals(signupPasswordConfirmText.getText().toString()))
                {
                    confirmImage.setImageResource(R.drawable.checked);
                    confirmPassword=true;
                }
                else
                {
                    confirmImage.setImageResource(R.drawable.xmark);
                    confirmPassword=false;
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        signupPasswordConfirmText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(signupPasswordText.getText().toString().equals(signupPasswordConfirmText.getText().toString()))
                {
                    confirmImage.setImageResource(R.drawable.checked);
                    confirmPassword=true;
                }
                else
                    {
                        confirmImage.setImageResource(R.drawable.xmark);
                        confirmPassword=false;
                    }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /*아이디, 이름 중복 확인이 끝났는지 체크하는 함수*/
    public void checkSignup()
    {
        if(confirmPassword==false)
        {
            Toast.makeText(getApplicationContext(),"df",Toast.LENGTH_LONG).show();
        }
        else
            {
                finish();
            }
    }
}
