package com.app.termproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class AlertSuccess extends DialogFragment implements View.OnClickListener {
    private static final String TAG="";
    private static final String ARG_DIALOG_MAIN_MSG="";

    public static AlertSuccess newInstance(String m)
    {
        Bundle bundle=new Bundle();
        bundle.putString(ARG_DIALOG_MAIN_MSG,m);
        AlertSuccess fragment=new AlertSuccess();
        fragment.setArguments(bundle);
        return fragment;
    }
    public void onCreate(Bundle s)
    {
        super.onCreate(s);

    }
    public Dialog onCreateDialog(Bundle s)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.fragment_alert_success,null)).setPositiveButton("확인",new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface d, int w)
            {
                dismissDialog();

                getActivity().finish();
            }
        });
        return builder.create();
    }
    private void dismissDialog()
    {
        this.dismiss();
    }
    public void onClick(View v)
    {
    }
}
