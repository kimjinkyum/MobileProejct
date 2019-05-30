package com.app.termproject;



import android.app.LauncherActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    ArrayList<ArrayList<String>> groupPostList;
    ArrayList<ArrayList<String>> arraylist;
    private PostAdapter adapter=new PostAdapter();
    String file;
    RecyclerView recyclerView;
    PostListItem item;
    List<PostListItem> items;
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
        //adapter.setItems(postitem);
        //이름하고 uri
        //adapter = new SearchAdapter(groupPostList.get(0),groupPostList.get(2), view.getContext());
        /*listView=view.findViewById(R.id.diaryList);
        // 리스트를 생성한다.
        list1 = new ArrayList<>();
        list2 = new ArrayList<>();
        listPost=new ArrayList<>();

        adapterPost=new ArrayAdapter<>(view.getContext(),android.R.layout.simple_list_item_1);
        listView.setAdapter(adapterPost);

        */
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
        // 검색에 사용할 데이터을 미리 저장한다.
        //settingList();
        //getImage();
        //getImage();
        // 리스트의 모든 데이터를 arraylist에 복사한다.// list 복사본을 만든다.
        /*arraylist1 = new ArrayListString>();
        arraylist1.addAll(list1);<
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
        //float latitude=data.getFloatExtra("latitude",0);
        //float longitude=data.getFloatExtra("longitude",0);
        GetPost post=new GetPost(uid,pinnumber,postNameText,download,postContentText,3,3);
        post.writeNewPost(uid,pinnumber,postNameText,download,postContentText,3,3);
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
        if(groupList.size()>0) {
            items.clear();
            ArrayList<String> postName = groupList.get(0);
            ArrayList<String> postContent = groupList.get(1);
            ArrayList<String> uri = groupList.get(2);
            ArrayList<String> latitude = groupList.get(3);
            ArrayList<String> longitude = groupList.get(4);
            groupPostList.add(postName);
            groupPostList.add(postContent);
            groupPostList.add(uri);
            groupPostList.add(latitude);
            groupPostList.add(longitude);
            for (int i = 0; i < postName.size(); i++) {
                PostListItem a=new PostListItem(uri.get(i),postName.get(i));
                items.add(a);
            }
            groupList.clear();
            recyclerView.setAdapter(new PostAdapter(view.getContext(),items,R.layout.fragment_look_diary));

        }
    }

}
    // 검색에 사용될 데이터를 리스트에 추가한다.


