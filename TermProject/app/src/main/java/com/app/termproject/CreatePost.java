package com.app.termproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreatePost extends AppCompatActivity {
    String pinnumber;
    ImageView image;
    EditText postName;
    EditText postContent;
    String postContentText;
    ImageButton postConfirmButton;
    private Uri filePath;
    String postNameText;
    String file;
    String filename;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_create_post);
        Intent passIntent= getIntent();
        pinnumber=passIntent.getStringExtra("pinnumber");
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
               postNameText=postName.getText().toString();
               postContentText=postContent.getText().toString();
                /*추후에 postName, postContentText 안썼을때 그 팝업창 띄우는거 추가 예정
                * */

                //Intent passIntent= getIntent();
                //String uid=passIntent.getStringExtra("uid");
                //Log.d("confirm",uid);
                uploadFile();
                //createPost(postNameText,postContentText,pinnumber,uid);
               //finish();
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

    private void uploadFile() {
        //업로드할 파일이 있으면 수행
        if (filePath != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHH_mmss");
            Date now = new Date();
            filename= pinnumber+formatter.format(now)+".png";
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl("gs://termproject-12d58.appspot.com/");
            final StorageReference imageRef = storageRef.child("images/"+filename);
            Log.d("Upload",pinnumber);
            Log.d("Upload",filename);
            UploadTask uploadTask = imageRef.putFile(filePath);
            Task<Uri> uriTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    return imageRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful())
                    {   Log.d("Upload", "ins");
                        Intent i=new Intent();
                        i.putExtra("uri",task.getResult().toString());
                        i.putExtra("postName",postNameText);
                        i.putExtra("postContent",postContentText);
                        i.putExtra("latitude",3.14);
                        i.putExtra("longitude",3.14);
                        setResult(11,i);
                        finish();
                    /*Intent i= getIntent();
                        String uid=i.getStringExtra("uid");
                        Uri download=task.getResult();
                        Log.d("Upload", "ins");

                        GetPost post=new GetPost(uid,pinnumber,postNameText,download.toString(),postContentText,3.14,3.14);
                        Log.d("Upload","post");
                        post.writeNewPost(uid,pinnumber,postNameText,download.toString(),postContentText,3.14,3.14);
                        setResult(11);
*/
                    }
                    else
                        {
                            Log.d("fileUpload","fail");
                        }
                }
            });



        }
    }
}
