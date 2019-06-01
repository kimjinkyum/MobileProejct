package com.app.termproject;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class LookPIN extends Fragment {

    private TextView textView,userView,nameText;
    private String pinnumber,name;
    private ArrayList<String>user=new ArrayList<>();
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
            name=bundle.getString("name");
        }
        View view = inflater.inflate(R.layout.fragment_get_my_pinnum, container, false);
        textView=view.findViewById(R.id.getPinNum);
        nameText=view.findViewById(R.id.pinNumText1);
        textView.setText(pinnumber);
        userView=view.findViewById(R.id.pinNumText5);
        nameText.setText(name+"의 정보!");
        getUser();
        return view;
    }
    public void getUser()
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final Query databaseReference;
        databaseReference = firebaseDatabase.getReference("user-diary");
        databaseReference.addValueEventListener(new ValueEventListener()
        {
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot message : dataSnapshot.getChildren())
                {
                    String key=message.getKey();
                    Log.d("user",key);
                    for(DataSnapshot message1:dataSnapshot.child(key).getChildren())
                    {

                        String key1=message1.getKey();
                        if(key1.equals(pinnumber))
                        {
                            getUserName(key);
                        }
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void getUserName(final String key)
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final Query databaseReference2;

        databaseReference2=firebaseDatabase.getReference("user");
        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener()
        {
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot message : dataSnapshot.getChildren())
                {
                    if(key.equals(message.getKey()))
                    {
                        Log.d("user","in");
                        userView.append(dataSnapshot.child(message.getKey()).child("name").getValue().toString()+"\n");

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}