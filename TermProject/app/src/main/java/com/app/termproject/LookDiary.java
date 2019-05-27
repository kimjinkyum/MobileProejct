package com.app.termproject;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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
    private List<String> list1;
    private List<String> list2;// 데이터를 넣은 리스트변수
    private ListView listView;
    private SearchAdapter adapter;      // 리스트뷰에 연결할 아답터
    private ArrayList<String> arraylist1;
    private ArrayList<String> arraylist2;
    private ArrayAdapter<String>adapterPost;
    private ArrayList<String>listPost;

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
            uid=bundle.getString("uid");
            Log.d("pinnumber",pinnumber);
            Log.d("pinnumber",uid);
        }
        view=inflater.inflate(R.layout.fragment_look_diary, container, false);
        listView=view.findViewById(R.id.diaryList);
        // 리스트를 생성한다.
        list1 = new ArrayList<String>();
        list2 = new ArrayList<String>();
        listPost=new ArrayList<String>();
        adapterPost=new ArrayAdapter<>(view.getContext(),android.R.layout.simple_list_item_1);
        listView.setAdapter(adapterPost);
        postButton=view.findViewById(R.id.createPost);
        postButton.setOnClickListener(new View.OnClickListener()
        {
           public void onClick(View v)
           {
               Intent i=new Intent(view.getContext(),CreatePost.class);
               i.putExtra("pinnumber",pinnumber);
               i.putExtra("uid",uid);
               startActivity(i);
           }
        });



        // 검색에 사용할 데이터을 미리 저장한다.
        //settingList();
        // 리스트의 모든 데이터를 arraylist에 복사한다.// list 복사본을 만든다.
        /*arraylist1 = new ArrayList<String>();
        arraylist1.addAll(list1);
        arraylist2=new ArrayList<String>();
        arraylist2.addAll(list2);

        // 리스트에 연동될 아답터를 생성한다.
        adapter = new SearchAdapter(list1,list2, view.getContext());

        // 리스트뷰에 아답터를 연결한다.
        listView.setAdapter(adapter);*/

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        });

        // Inflate the layout for this fragment
        return view;
    }


    public void createDB(String postName, String postContent, String filePath)
    {
        //String pinnumber, String post_name, String uid,String uri, String contentPost,float longitude, float latitude

        GetPost post=new GetPost(pinnumber,postName,uid,filePath,postContent,3.14,3.14);
        post.writeNewPost(pinnumber,postName,uid,filePath,postContent,3.14,3.14);
    }
}
    // 검색에 사용될 데이터를 리스트에 추가한다.
    /*private void settingList()
    {
        list1.add("박지현");
        list1.add("수지");
        list1.add("남태현");
        list1.add("하성운");
        list1.add("크리스탈");
        list1.add("강승윤");
        list1.add("손나은");
        list1.add("남주혁");
        list1.add("루이");
        list1.add("진영");
        list1.add("슬기");
        list1.add("이해인");
        list1.add("고원희");
        list1.add("설리");
        list2.add("공명");
        list2.add("김예림");
        list2.add("혜리");
        list2.add("웬디");
        list2.add("박혜수");
        list2.add("카이");
        list2.add("진세연");
        list2.add("동호");
        list2.add("박세완");
        list2.add("도희");
        list2.add("창모");
        list2.add("허영지");
        list2.add("이상웅");
        list2.add("노웅기");
    }*/

