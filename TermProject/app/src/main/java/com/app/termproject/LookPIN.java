package com.app.termproject;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class LookPIN extends Fragment {

    private TextView textView;
    private String pinnumber;

    public LookPIN() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            pinnumber = bundle.getString("pinnumber");

        }
        View view = inflater.inflate(R.layout.fragment_get_my_pinnum, container, false);
        textView=view.findViewById(R.id.getPinNum);
        textView.setText(pinnumber);
        return view;
    }

}