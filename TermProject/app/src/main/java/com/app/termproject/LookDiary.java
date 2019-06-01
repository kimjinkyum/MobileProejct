package com.app.termproject;



import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.app.termproject.DB.GetPost;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class LookDiary extends Fragment {
    View view;
    Button postButton;
    String pinnumber;
    String uid;
    ArrayList<ArrayList<String>> groupPostList;
    private PostAdapter adapter;
    String file;
    RecyclerView recyclerView;
    PostListItem item;
    List<PostListItem> items;
    CardView cardView;
    public LookDiary() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        Bundle bundle=this.getArguments();
        if(bundle!=null)
        {
            pinnumber=bundle.getString("pinnumber");

            Log.d("pinnumber",pinnumber);

        }
        view=inflater.inflate(R.layout.fragment_look_diary, container, false);


        //listView=view.findViewById(R.id.postListView);
        groupPostList=new ArrayList<>();
        recyclerView=view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
       cardView=view.findViewById(R.id.cardview);

        postButton=view.findViewById(R.id.createPost);

        postButton.setOnClickListener(new View.OnClickListener()
        {
           public void onClick(View v)
           {
               Intent i=new Intent(view.getContext(),CreatePost.class);
               i.putExtra("pinnumber",pinnumber);
               i.putExtra("uid",uid);
               startActivityForResult(i,11);
           }
        });
        ((Diary)getActivity()).getPostInformation(0);
        items=new ArrayList<>();


        // Inflate the layout for this fragment
        return view;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);


        if (data == null) {
            return;
        }


        String postNameText=data.getStringExtra("postName");
        String download=data.getStringExtra("uri");
        String postContentText=data.getStringExtra("postContent");
        String fileName=data.getStringExtra("fileName");
        String date=data.getStringExtra("date");
        float latitude=data.getFloatExtra("latitude",0);
        float longitude=data.getFloatExtra("longitude",0);
        GetPost post=new GetPost(pinnumber,postNameText,download,postContentText,latitude,longitude,fileName,date);
        post.writeNewPost(pinnumber,postNameText,download,postContentText,latitude,longitude,fileName,date);
    }

    private void getImage() {
        if (file != null) {
            Uri uri = Uri.parse(file);
            Log.d("newPost",uri.toString());
            //Glide.with(view.getContext()).load(uri).into(imageView);
        }
    }
    public void show(ArrayList<ArrayList<String>> groupList)
    {
        if(groupList.size()>0)
        {
            items.clear();
            ArrayList<String> postName = groupList.get(0);
            ArrayList<String> postContent = groupList.get(1);
            ArrayList<String> uri = groupList.get(2);
            ArrayList<String> latitude = groupList.get(3);
            ArrayList<String> longitude = groupList.get(4);
            ArrayList<String>postKey=groupList.get(5);
            ArrayList<String>fileName=groupList.get(6);
            ArrayList<String>date=groupList.get(7);

            groupPostList.add(postName);
            groupPostList.add(postContent);
            groupPostList.add(uri);
            groupPostList.add(latitude);
            groupPostList.add(longitude);
            groupPostList.add(postKey);
            groupPostList.add(fileName);
            groupPostList.add(date);
            for (int i = 0; i < postName.size(); i++)
            {
                PostListItem a=new PostListItem(uri.get(i),postName.get(i),postContent.get(i),postKey.get(i),pinnumber,fileName.get(i),date.get(i));
                items.add(a);
            }
            groupList.clear();
            adapter=new PostAdapter( view.getContext(),items,R.layout.fragment_look_diary);
            recyclerView.setAdapter(new PostAdapter(view.getContext(),items,R.layout.fragment_look_diary));

        }
    }

}
    // 검색에 사용될 데이터를 리스트에 추가한다.


