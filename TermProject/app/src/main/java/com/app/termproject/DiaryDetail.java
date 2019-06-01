package com.app.termproject;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.termproject.DB.GetPost;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DiaryDetail extends AppCompatActivity
{

    Button deleteButton;
    Button editButton;
    String pinnumber;
    String key;
    String filename,originalFileName;
    String nameEdit,contentEdit,imageEdit,original,date;
    ImageView iv;
    Uri filePath;
    int flag=1;
    TextView diaryTitle,diaryContent,diaryDate;
    EditText title, content;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_detail);
        diaryTitle= (TextView)findViewById(R.id.textView1);
        diaryContent = (TextView)findViewById(R.id.textView2);
        title=findViewById(R.id.editText1);
        iv = (ImageView)findViewById(R.id.imageView1);
        diaryDate=(TextView)findViewById(R.id.textView3);
        content=findViewById(R.id.editText2);
        deleteButton=findViewById(R.id.postDeleteButton);
        editButton=findViewById(R.id.postEditButton);

        final Intent intent = getIntent(); // 보내온 Intent를 얻는다

        nameEdit=intent.getStringExtra("name");
        contentEdit=intent.getStringExtra("content") ;
        key=intent.getStringExtra("key");
        original=intent.getStringExtra("uri");
        originalFileName=intent.getStringExtra("fileName");
        pinnumber=intent.getStringExtra("pinnumber");
        date=intent.getStringExtra("date");

        diaryTitle.setText(nameEdit);
        diaryContent.setText(contentEdit);
        diaryDate.setText(date);


         Glide.with(getApplicationContext()).load(original).into(iv);
         diaryTitle.setOnLongClickListener(new View.OnLongClickListener() {
             @Override
             public boolean onLongClick(View v)
             {editButton.setVisibility(View.VISIBLE);
                 diaryTitle.setVisibility(View.INVISIBLE);
                 title.setVisibility(View.VISIBLE);
                 Log.d("editDairy",nameEdit);
                 return true;
             }
         });
         diaryContent.setOnLongClickListener(new View.OnLongClickListener()
         {
             public boolean onLongClick(View v)
             {
                 editButton.setVisibility(View.VISIBLE);
                 diaryContent.setVisibility(View.INVISIBLE);
                 content.setVisibility(View.VISIBLE);

                 Log.d("editDairy",contentEdit);
                 return true;
             }
         });

         iv.setOnLongClickListener(new View.OnLongClickListener()
         {
             @Override
             public boolean onLongClick(View v) {
                 editButton.setVisibility(View.VISIBLE);
                 Intent intent1 = new Intent();
                 intent1.setType("image/*");
                 intent1.setAction(Intent.ACTION_GET_CONTENT);
                 startActivityForResult(Intent.createChooser(intent1, "이미지를 선택하세요."), 0);
                 return true;
             }
         });
        deleteButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                delete();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v){

                if (flag==1)
                {

                    String name=title.getText().toString();
                    if (name.length()>0)
                    {
                        nameEdit=name;
                    }
                    String conten=content.getText().toString();
                    if(conten.length()>0)
                    {
                        contentEdit=conten;
                    }
                    delete();
                    GetPost post=new GetPost(pinnumber,nameEdit,original,contentEdit,3,3,originalFileName,date);
                    post.writeNewPost(pinnumber,nameEdit,original,contentEdit,3,3,originalFileName,date);

                }
                else
                    {

                        uploadFile();

                    }

            }
        });
    }
    public void delete()
    {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://termproject-12d58.appspot.com/");
        if(!originalFileName.equals("basic image"))
        {
            final StorageReference imageRef = storageRef.child("images/" + originalFileName);
            imageRef.delete();
        }
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference;
        databaseReference = firebaseDatabase.getReference("diary").child(pinnumber).child(key);
        databaseReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                finish();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
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
                iv.setImageBitmap(bitmap);

                flag=0;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadFile() {
        //업로드할 파일이 있으면 수행
        Log.d("Upload",filePath.toString());
        if (filePath != null) {
            Log.d("Upload",filePath.toString());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHH_mmss");
            Date now = new Date();
            filename= formatter.format(now)+".png";
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl("gs://termproject-12d58.appspot.com/");
            final StorageReference imageRef = storageRef.child("images/"+filename);
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
                    {
                        imageEdit=task.getResult().toString();
                        Log.d("Upload", "ins");
                        String name=title.getText().toString();
                        if (name.length()>0)
                        {
                            nameEdit=name;
                        }
                        String conten=content.getText().toString();
                        if(conten.length()>0)
                        {
                            contentEdit=conten;
                        }
                        delete();
                        GetPost post=new GetPost(pinnumber,nameEdit,imageEdit,contentEdit,3,3,filename,date);
                        post.writeNewPost(pinnumber,nameEdit,imageEdit,contentEdit,3,3,filename,date);
                        //i.putExtra("latitude",latlng[0]);
                        //i.putExtra("longitude",latlng[1]);
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

