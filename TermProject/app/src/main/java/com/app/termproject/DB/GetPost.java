package com.app.termproject.DB;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class GetPost {
    public String pinnumber;
    public String postName;
    public String uid;
    public String uri;
    public String contentPost;
    public float longitude;
    public float latitude;
    public String fileName;
    public String date;
    public String weather;
    private DatabaseReference firebaseDatabase;

    public GetPost() {
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
    }


    public GetPost(String pinnumber, String postName, String uri, String contentPost, float latitude, float longitude, String fileName,String date,String weather) {
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        this.pinnumber = pinnumber;
        this.postName = postName;
        this.uri = uri;
        this.contentPost = contentPost;
        this.longitude = longitude;
        this.latitude = latitude;
        this.fileName = fileName;
        this.date=date;
        this.weather=weather;
    }
    public void setPostName(String name) {
        this.postName = name;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("postName", postName);
        result.put("uri", uri);
        result.put("content", contentPost);
        result.put("longitude", longitude);
        result.put("latitude", latitude);
        result.put("fileName", fileName);
        result.put("date",date);
        result.put("weather",weather);
        return result;
    }

    public void writeNewPost( String pinnumber, String postName, String uri, String contentPost, float latitude,float longitude,String filename,String date,String weather)
    {

        Log.d("confirm",pinnumber);
        String key=firebaseDatabase.child("diary").child(pinnumber).push().getKey();
        Log.d("confirm",key);
        GetPost p=new GetPost();
        Map<String,Object> value=toMap();
        Map<String,Object> update=new HashMap<>();
        update.put("/diary/"+pinnumber+"/"+key, value);
        firebaseDatabase.updateChildren(update);
        Log.d("confirm","confirm");

    }
}

