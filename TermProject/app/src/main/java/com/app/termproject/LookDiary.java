package com.app.termproject;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class LookDiary extends Fragment {
    View view;
    Button postButton;
    String pinnumber;
    String uid;
    ImageView imageView;

    private ArrayList<String> list1;
    private ArrayList<String> list2;// 데이터를 넣은 리스트변수
    private ListView listView;
    private SearchAdapter adapter;      // 리스트뷰에 연결할 아답터
    private ArrayList<String> arraylist1;
    private ArrayList<String> arraylist2;
    private ArrayAdapter<String>adapterPost;
    private ArrayList<String>listPost;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference ;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter recycleAdapter;
    String file;
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

        /*listView=view.findViewById(R.id.diaryList);
        // 리스트를 생성한다.
        list1 = new ArrayList<>();
        list2 = new ArrayList<>();
        listPost=new ArrayList<>();
        adapter = new SearchAdapter(list1,list2, view.getContext());
        adapterPost=new ArrayAdapter<>(view.getContext(),android.R.layout.simple_list_item_1);
        listView.setAdapter(adapterPost);

        */
        imageView=view.findViewById(R.id.postImageView);
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


        // 검색에 사용할 데이터을 미리 저장한다.
        //settingList();
        //getImage();
        //getImage();
        // 리스트의 모든 데이터를 arraylist에 복사한다.// list 복사본을 만든다.
        /*arraylist1 = new ArrayList<String>();
        arraylist1.addAll(list1);
        arraylist2=new ArrayList<String>();
        arraylist2.addAll(list2);

        // 리스트에 연동될 아답터를 생성한다.
        adapter = new SearchAdapter(list1,list2, view.getContext());

        // 리스트뷰에 아답터를 연결한다.
        listView.setAdapter(adapter);*/

        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //넘기기
                Toast.makeText(getContext(),"눌렀어요",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(),DiaryDetail.class);
                intent.putExtra("name",list1.get(position));
                intent.putExtra("date",list2.get(position));
                //image를 보낸다.
//                intent.putExtra("name",list1.get(position));
                startActivity(intent);

            }
        });*/

        // Inflate the layout for this fragment
        return view;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        String postNameText=data.getStringExtra("postName");
        String download=data.getStringExtra("uri");
        String postContentText=data.getStringExtra("postContent");
        GetPost post=new GetPost(uid,pinnumber,postNameText,download,postContentText,3.14,3.14);;
        post.writeNewPost(uid,pinnumber,postNameText,download.toString(),postContentText,3.14,3.14);
    }

    private void getImage() {
        if (file != null) {
            Uri uri = Uri.parse(file);
            Log.d("newPost",uri.toString());
            Glide.with(view.getContext()).load(uri).into(imageView);
        }
    }
    public void show(ArrayList<ArrayList<String>> groupList)
    {
        ArrayList<String>postName= groupList.get(0);
        ArrayList<String>postContent= groupList.get(1);
        ArrayList<String>uri= groupList.get(2);
        ArrayList<String>latitude= groupList.get(3);
        ArrayList<String>longitude= groupList.get(4);
        for(int i=0;i<postName.size();i++)
        {
            Log.d("cccc",postName.get(i));
            Log.d("cccc",postContent.get(i));
            Log.d("cccc",uri.get(i));
            Log.d("cccc",latitude.get(i));
            Log.d("cccc",longitude.get(i));
        }
    }

}
    // 검색에 사용될 데이터를 리스트에 추가한다.


