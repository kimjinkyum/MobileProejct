package com.app.termproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
        builder.setView(inflater.inflate(R.layout.fragment_searching, null)).setPositiveButton("확인", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface d, int w) {
                String pinNumber = editText.getText().toString();
                dismissDialog();
            }
        });
        return builder.create();
    }

    private void dismissDialog() {
        this.dismiss();
    }

    public void onClick(View v) {

    }
}
