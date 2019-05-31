package com.app.termproject;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class LookPIN extends Fragment {

    private TextView textView;

    public LookPIN() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        textView.setText(savedInstanceState.getString("pinnumber"));
        return (ViewGroup)inflater.inflate(R.layout.fragment_get_my_pinnum, container, false);
    }

}