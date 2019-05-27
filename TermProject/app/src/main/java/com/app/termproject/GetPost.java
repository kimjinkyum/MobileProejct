package com.app.termproject;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class GetPost
{
    public String pinnumber;
    public String postName;
    public String uid;
    public String uri;
    public String contentPost;
    public double longitude;
    public double latitude;
    private DatabaseReference firebaseDatabase;
    public GetPost()
    {
        firebaseDatabase= FirebaseDatabase.getInstance().getReference();
    }
    public GetPost(String uid,String pinnumber, String postName,String uri, String contentPost,double longitude,double latitude)
    {
        firebaseDatabase= FirebaseDatabase.getInstance().getReference();
        this.pinnumber=pinnumber;
        this.postName=postName;
        this.uid=uid;
        this.uri=uri;
        this.contentPost=contentPost;
        this.longitude=longitude;
        this.latitude=latitude;
    }
    public Map<String, Object> toMap()
    {
        HashMap<String,Object> result=new HashMap<>();
        result.put("postName",postName);
        result.put("uri",uri);
        result.put("content",contentPost);
        result.put("longitude",longitude);
        result.put("latitude",latitude);
        return result;
    }
    public void writeNewPost(String uid,String pinnumber, String postName,String uri, String contentPost,double longitude,double latitude)
    {

        Log.d("confirm",pinnumber);
        String key=firebaseDatabase.child("diary").child(pinnumber).push().getKey();
        Log.d("confirm",key);
        GetPost p=new GetPost(pinnumber,postName,uid,uri,contentPost,longitude,latitude);
        Map<String,Object> value=toMap();
        Map<String,Object> update=new HashMap<>();
        update.put("/diary/"+pinnumber+"/"+key, value);
        //firebaseDatabase.child("diary").child(pinnumber).child("diaryname").setValue(postName);
        Log.d("confirm","update");
        firebaseDatabase.updateChildren(update);
        Log.d("confirm","confirm");
    }



}
