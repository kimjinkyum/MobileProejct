package com.app.termproject;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CreateDiary extends Dialog implements View.OnClickListener {
    private android.support.design.widget.TextInputEditText editText;
    private Button button;
    private Context context;
    private android.support.design.widget.TextInputLayout textInputLayout;
    String string;


    public CreateDiary(Context context) {
        super(context);
        this.context = context;
    }

    private CreateDiary.CreateDiaryListener createDiaryistener;

    interface CreateDiaryListener {
        void onButtonClicked(String str);
    }

    public void setDialogListener(CreateDiary.CreateDiaryListener createDiaryListener) {
        this.createDiaryistener = createDiaryListener;
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_creatediary);

        textInputLayout = findViewById(R.id.textLayout);
        button = findViewById(R.id.button);
        editText = findViewById(R.id.edit1);

        textInputLayout.setCounterEnabled(true);
        textInputLayout.setCounterMaxLength(8);
        textInputLayout.setErrorEnabled(true);
        button.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button) {
            String createName = editText.getText().toString();
            if (createName.length() <= 8) {
                createDiaryistener.onButtonClicked(createName);
                dismiss();
            }

        }

    }

}