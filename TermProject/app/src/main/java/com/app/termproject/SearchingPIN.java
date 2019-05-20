package com.app.termproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class SearchingPIN extends DialogFragment implements View.OnClickListener {
    private static final String TAG = "LoginFragment";
    private static final String ARG_DIALOG_MAIN_MSG = "Login message";
    EditText editText;

    public static SearchingPIN newInstance(String m) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_DIALOG_MAIN_MSG, m);
        SearchingPIN fragment = new SearchingPIN();
        fragment.setArguments(bundle);
        return fragment;
    }

    public void onCreate(Bundle s) {
        super.onCreate(s);

    }

    public Dialog onCreateDialog(Bundle s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.fragment_searching, null)).setPositiveButton("검색", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface d, int w) {
                String pinNumber = editText.getText().toString();
                dismissDialog();
            }
        });

        AlertDialog alert = builder.create();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(255, 237, 21, 96)));

        alert.show();
        return alert;
    }

    private void dismissDialog() {
        this.dismiss();
    }

    public void onClick(View v) {

    }
}
