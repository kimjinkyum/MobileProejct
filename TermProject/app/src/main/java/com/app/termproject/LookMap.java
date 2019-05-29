package com.app.termproject;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

        Intent intent= new Intent(getActivity(),MapsActivity.class);
        startActivity(intent);

        return inflater.inflate(R.layout.fragment_look_map, container, false);
    }

}
