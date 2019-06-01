package com.app.termproject;

import android.content.Context;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.SupportErrorDialogFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    boolean confirmPassword = false;
    boolean confirmID = false;

    EditText signupIdText, signupNameText, signupPasswordText, signupPasswordConfirmText;
    ImageView confirmImage, confirmIdImage;
    ImageButton signupConfirmButton;
    InputMethodManager imm;
    private FirebaseAuth mAuth;    //로그인 모튤 변수
    private FirebaseUser currentUser;//현재꺼
    AlertID alertID;
    AlertPassword alertPassword;
    AlertError alertError;
    AlertEmail alertEmail;
    AlertSuccess alertSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        init();
        mAuth = FirebaseAuth.getInstance();
        signupConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id=signupIdText.getText().toString();
                String name=signupNameText.getText().toString();
                String password=signupPasswordText.getText().toString();
                joinStart(id,name,password);
                //checkSignup();

            }
        });
    }
    /*가입함수*/

    /**
     * 이메일 포맷 체크
     *
     * @param email
     * @return
     */
    public boolean checkEmail(String email) {

        String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        boolean isNormal = m.matches();
        return isNormal;

    }


    public void joinStart(String id, final String name, String password) {

        mAuth.createUserWithEmailAndPassword(id, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthUserCollisionException e) {
                        alertID = AlertID.newInstance("null");
                        alertID.show(getSupportFragmentManager(),"AlertID");
                    } catch (FirebaseAuthWeakPasswordException e) {
//                        Toast.makeText(SignupActivity.this, "비밀번호가 간단해요..", Toast.LENGTH_SHORT).show();
                        alertPassword = AlertPassword.newInstance("null");
                        alertPassword.show(getSupportFragmentManager(),"");
                    } catch (FirebaseAuthInvalidCredentialsException e) {
//                        Toast.makeText(SignupActivity.this, "email 형식에 맞지 않습니다.", Toast.LENGTH_SHORT).show();
                        alertEmail = AlertEmail.newInstance("null");
                        alertEmail.show(getSupportFragmentManager(),"");
                    } catch (Exception e) {
//                        Toast.makeText(SignupActivity.this, "에러", Toast.LENGTH_LONG).show();
                        alertError = AlertError.newInstance("null");
                        alertError.show(getSupportFragmentManager(),"");
                    }
                } else {
                    currentUser = mAuth.getCurrentUser();
//                    Toast.makeText(SignupActivity.this, "가입성공" + name + currentUser.getEmail(), Toast.LENGTH_LONG).show();
                    alertSuccess = AlertSuccess.newInstance("null");
                    alertSuccess.show(getSupportFragmentManager(),"");

                }
            }
        });
    }

    /*find view object*/
    public void init() {
        signupIdText = findViewById(R.id.signupIdText);
        signupIdText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        signupNameText = findViewById(R.id.signupNameText);
        signupNameText.setInputType(InputType.TYPE_CLASS_TEXT);
        signupPasswordText = findViewById(R.id.signupPasswordText);
        signupPasswordText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        signupPasswordText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        signupPasswordConfirmText = findViewById(R.id.signupPasswordConfirmText);
        signupPasswordConfirmText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        signupPasswordConfirmText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        confirmImage = findViewById(R.id.passwordConfirmImage);
        confirmIdImage = findViewById(R.id.idConfirmImage);
        signupConfirmButton = findViewById(R.id.signupConfirmButton);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        /*함수 호출*/
        registerEdit(signupIdText);
        registerEdit(signupNameText);
        registerEdit(signupPasswordText);
        checkUniqueId();
        checkUniqueName();
        confirmPassword();

        /**
         * 마지막 Confirm 텍스트에서 엔터키 누르면 줄넘김이 아니라 키보드 내려가게
         */
        signupPasswordConfirmText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    imm.hideSoftInputFromWindow(signupPasswordConfirmText.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
        signupPasswordConfirmText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);

    }

    /*엔터키 누르면 줄넘김이 아니라 다음으로 넘어 갈 수 있게 하는 코드 */
    public void registerEdit(EditText editText) {
        editText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
    }

    /*아이디 중복 체크*/
    public void checkUniqueId() {
        //compare=아이디 읽어오기
        signupIdText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String getEmail = signupIdText.getText().toString();
                if (checkEmail(getEmail)) {
                    confirmIdImage.setImageResource(R.drawable.checked);
                    confirmID = true;
                } else {
                    confirmIdImage.setImageResource(R.drawable.xmark);
                    confirmID = false;
                }


            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    /*이름 중복 체크*/
    public void checkUniqueName() {
        //compare=이름 읽어오기
        signupNameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    /*비밀번호 확인 입력시 같으면 초록색 체크 표시, 다르면 빨간색 X표시 */
    public void confirmPassword() {
        signupPasswordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (signupPasswordText.getText().toString().equals(signupPasswordConfirmText.getText().toString())) {
                    confirmImage.setImageResource(R.drawable.checked);
                    confirmPassword = true;
                } else {
                    confirmImage.setImageResource(R.drawable.xmark);
                    confirmPassword = false;
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
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (signupPasswordText.getText().toString().equals(signupPasswordConfirmText.getText().toString())) {
                    confirmImage.setImageResource(R.drawable.checked);
                    confirmPassword = true;
                } else {
                    confirmImage.setImageResource(R.drawable.xmark);
                    confirmPassword = false;
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /*아이디, 이름 중복 확인이 끝났는지 체크하는 함수*/
    public void checkSignup() {
        if (confirmPassword == false || confirmID == false) {
            Toast.makeText(getApplicationContext(), "다시 한번 확인해주세요", Toast.LENGTH_LONG).show();
        } else {
            finish();
        }
    }
}
