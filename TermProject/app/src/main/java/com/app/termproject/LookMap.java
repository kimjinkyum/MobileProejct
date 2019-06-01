package com.app.termproject;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class LookMap extends Fragment {

    View view;
    ArrayList<ArrayList<String>> arrayLists;
    ArrayList<String> uri;
    ArrayList<String> latitude;
    ArrayList<String> longitude;

    public LookMap() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_look_map, container, false);
        ((Diary) getActivity()).getPostInformation(1);

        Log.d("aaa",arrayLists.size()+"");

        uri = arrayLists.get(2);
        latitude = arrayLists.get(3);
        longitude = arrayLists.get(4);
        Intent intent = new Intent(getActivity(), MapsActivity.class);
        startActivity(intent);
        return view;
    }

    public void show(ArrayList<ArrayList<String>> groupList) {
        ArrayList<String> postName = groupList.get(0);
        ArrayList<String> postContent = groupList.get(1);
        ArrayList<String> uri = groupList.get(2);
        ArrayList<String> latitude = groupList.get(3);
        ArrayList<String> longitude = groupList.get(4);
        for (int i = 0; i < postName.size(); i++) {
            Log.d("cccc", postName.get(i));
            Log.d("cccc", postContent.get(i));
            Log.d("cccc", uri.get(i));
            Log.d("cccc", latitude.get(i));
            Log.d("cccc", longitude.get(i));
        }
    }

}