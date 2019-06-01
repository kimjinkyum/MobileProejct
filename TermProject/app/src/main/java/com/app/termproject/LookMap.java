package com.app.termproject;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.Serializable;
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
        //arrayLists = new ArrayList<ArrayList<String>>();

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_look_map, container, false);

        // this should be executed
        // ((Diary) getActivity()).getPostInformation(1);

        /*
        Log.d("aaa",arrayLists.size()+"");

        uri = arrayLists.get(2);
        latitude = arrayLists.get(3);
        longitude = arrayLists.get(4);
        */



        // test
        uri = new ArrayList<String>();
        uri.add("https://firebasestorage.googleapis.com/v0/b/termproject-12d58.appspot.com/o/KakaoTalk_20190531_175626070.jpg?alt=media&token=8e9ff651-340d-474a-9513-349ba2a3cab2");
        uri.add("https://firebasestorage.googleapis.com/v0/b/termproject-12d58.appspot.com/o/imagesimage%3A216?alt=media&token=051350ac-6848-4474-bf5a-27a401dab41e");
        latitude = new ArrayList<String>();
        latitude.add("37");
        latitude.add("36");
        longitude = new ArrayList<String>();
        longitude.add("127");
        longitude.add("127");






        Intent intent = new Intent(getActivity(), MapsActivity.class);
        intent.putExtra("lat", latitude);
        intent.putExtra("lng", longitude);
        intent.putExtra("uri", uri);
        startActivity(intent);
        return view;
    }

    public void show(ArrayList<ArrayList<String>> groupList) {
        //ArrayList<String> postName = groupList.get(0);
        //ArrayList<String> postContent = groupList.get(1);
        uri = groupList.get(2);
        latitude = groupList.get(3);
        longitude = groupList.get(4);
        /*
        for (int i = 0; i < postName.size(); i++) {
            Log.d("cccc", postName.get(i));
            Log.d("cccc", postContent.get(i));
            Log.d("cccc", uri.get(i));
            Log.d("cccc", latitude.get(i));
            Log.d("cccc", longitude.get(i));
        }
        */
    }


    /*
    public class infoToMap implements Serializable {
        private ArrayList<String> sentUri;
        private ArrayList<String> sentLat;
        private ArrayList<String> sentLng;

        /* this should be executed
        public infoToMap(ArrayList<String> uri, ArrayList<String> lat, ArrayList<String> lng) {
            sentUri = uri;
            sentLat = lat;
            sentLng = lng;
        }


        // test
        public infoToMap(ArrayList<String> uri, ArrayList<String> lat, ArrayList<String> lng) {
            sentUri = new ArrayList<String>();
            sentUri.add("https://firebasestorage.googleapis.com/v0/b/termproject-12d58.appspot.com/o/KakaoTalk_20190531_175626070.jpg?alt=media&token=8e9ff651-340d-474a-9513-349ba2a3cab2");
            sentUri.add("https://firebasestorage.googleapis.com/v0/b/termproject-12d58.appspot.com/o/imagesimage%3A216?alt=media&token=051350ac-6848-4474-bf5a-27a401dab41e");
            sentLat = new ArrayList<String>();
            sentLat.add("37");
            sentLat.add("36");
            sentLng = new ArrayList<String>();
            sentLng.add("127");
            sentLng.add("127");
        }


    }
    */

}