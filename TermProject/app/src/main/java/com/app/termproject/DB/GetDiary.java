package com.app.termproject.DB;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GetDiary {
    public String pinnumber;
    public String email;
    public String diary_name;
    public String uid;
    public boolean is=false;
    private DatabaseReference firebaseDatabase;

    public GetDiary() {

        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
    }
    public HashMap<String,String>getInfo()
    {
        HashMap<String, String>re=new HashMap<>();
        re.put("pinnnumber",this.pinnumber);
        re.put("diaryname",this.diary_name);
        return re;

    }
    public void setPin(String pinnumber)
    {
        this.pinnumber=pinnumber;
        Log.d("ddd",this.pinnumber);
    }

    public GetDiary(String uid, String email, String pinnumber, String diary_name) {
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        this.uid = uid;
        this.pinnumber = pinnumber;
        this.diary_name = diary_name;
        this.email = email;
    }
    public GetDiary(String uid, String email, String pinnumber) {
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        this.uid = uid;
        this.pinnumber = pinnumber;
        this.email = email;
    }
    public GetDiary( String pinnumber) {
        this.pinnumber = pinnumber;

    }

    public boolean pinCheck(){
        final DatabaseReference databaseReference =FirebaseDatabase.getInstance().getReference("diary");
        /*if(databaseReference.getKey())
        {
            Log.d("pin","correct pin");
            return true;
        }
        else{
            return false;
        }*/
        return true;
    }
    public void isPin()
    {
        final DatabaseReference databaseReference;
        databaseReference=FirebaseDatabase.getInstance().getReference("diary");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for(DataSnapshot message: dataSnapshot.getChildren())
                {
                    Log.d("ddd",message.getKey());
                    Log.d("ddd",pinnumber);
                    if(message.getKey().equals(pinnumber))
                    {
                        diary_name=dataSnapshot.child(message.getKey()).child("diaryname").getValue().toString();
                        Log.d("co","in");
                        is=true;
                        writeOld(uid,email,pinnumber);
                        break;
                    }
                    else {
                        Log.d("co","out");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void writeOld(String uid, String email, String pin)
    {
            GetDiary d = new GetDiary(uid, email, pin, this.diary_name);
            Map<String, Object> update = new HashMap<>();

            Log.d("ddd",uid+" "+email+" "+pin+" "+this.diary_name);
        Map<String, Object> value = toMap();
            update.put("/user-diary/" + uid + "/" + pin, value);
            firebaseDatabase.updateChildren(update);
            Log.d("ddd", "in write old");

            }

    public void set(String uid, String pinnumber,String diary_name) {
        this.uid = uid;
        this.pinnumber = pinnumber;
        this.diary_name=diary_name;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("diaryname", diary_name);
        return result;
    }

    public void writeNew(String uid, String email, String pin, String diary_name) {
        String key = firebaseDatabase.child("diary").push().getKey();
        GetDiary d = new GetDiary(uid, email, pin, diary_name);
        Map<String, Object> value = toMap();
        Map<String, Object> update = new HashMap<>();
        update.put("/diary/" + pinnumber, value);
        update.put("/user-diary/" +uid+ "/" + pinnumber,value);
        firebaseDatabase.updateChildren(update);

    }


}