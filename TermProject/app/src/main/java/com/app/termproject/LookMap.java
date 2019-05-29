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
public class LookMap extends Fragment {


    public LookMap() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle=this.getArguments();
        if(bundle!=null)
        {
            Log.d("fragment",bundle.getString("uid"));
            Log.d("fragment",bundle.getString("pinnumber"));
        }
        return inflater.inflate(R.layout.fragment_look_map, container, false);
    }

}
