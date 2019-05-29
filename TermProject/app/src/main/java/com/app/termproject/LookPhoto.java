package com.app.termproject;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class LookPhoto extends Fragment {


    public LookPhoto() {
        // Required empty public constructor
        Bundle bundle=this.getArguments();
        if(bundle!=null)
        {
            String pinnumber, uid;
            pinnumber=bundle.getString("pinnumber");
            uid=bundle.getString("uid");
            Log.d("abc",pinnumber);
            Log.d("abc",uid);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return (ViewGroup)inflater.inflate(R.layout.fragment_look_photo, container, false);
    }

}
