package com.app.termproject;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

public class CreatePost extends AppCompatActivity {

    ImageView image;
    EditText postName;
    EditText postContent;
    ImageButton postConfirmButton;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_create_post);

        postContent=findViewById(R.id.postEditText);
        postName=findViewById(R.id.postNameEditText);
        postConfirmButton=findViewById(R.id.postConfirmButton);
        image=findViewById(R.id.postImage);
        image.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 0);

            }

        });
        postConfirmButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                String postNameText=postName.getText().toString();
                String postContentText=postContent.getText().toString();
                /*추후에 postName, postContentText 안썼을때 그 팝업창 띄우는거 추가 예정
                * */
                Intent passIntent= getIntent();
                String uid=passIntent.getStringExtra("uid");
                String pinnumber=passIntent.getStringExtra("pinnumber");
                Log.d("confirm",uid);
                createPost(postNameText,postContentText,pinnumber,uid);
                finish();

            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //request코드가 0이고 OK를 선택했고 data에 뭔가가 들어 있다면
        if(requestCode == 0 && resultCode == RESULT_OK){
            filePath = data.getData();
            try
            {
                //Uri 파일을 Bitmap으로 만들어서 ImageView에 집어 넣는다.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                int height=bitmap.getHeight();
                int width=bitmap.getWidth();
                //bitmap=Bitmap.createScaledBitmap(bitmap,160,height/(width/160),true);
                image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    protected void createPost(String postNameText,String postContentText,String pinnumber,String uid)
    {
        //String uid,String pinnumber, String postName,String uri, String contentPost,double longitude,double latitude
        Log.d("confirm",uid);

        GetPost post=new GetPost(uid,pinnumber,postNameText,filePath.toString(),postContentText,3.14,3.14);
        post.writeNewPost(uid,pinnumber,postNameText,filePath.toString(),postContentText,3.14,3.14);

    }
}
