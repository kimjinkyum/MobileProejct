package com.app.termproject;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.view.MotionEvent;
import android.widget.DatePicker;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ExifInterface;

import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.termproject.DB.GetPost;
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
import java.util.Calendar;
import java.util.Date;

public class CreatePost extends AppCompatActivity {

    ImageView image;
    EditText postName;
    EditText postContent;
    EditText viewDate;
    String postContentText;
    ImageButton postConfirmButton;
    Button postDate;
    private Uri filePath;
    String postNameText;
    String filename;

    int cYear;
    int cMonth;
    int cDay;

    static final int DATE_DIALOG_ID = 0;


    float[] latlng = new float[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_create_post);
        postContent = findViewById(R.id.postEditText);
        postName = findViewById(R.id.postNameEditText);
        postConfirmButton = findViewById(R.id.postConfirmButton);
        viewDate = findViewById(R.id.viewDate);
        postDate = findViewById(R.id.postDate);


        Calendar c = Calendar.getInstance();
        cYear = c.get(Calendar.YEAR);
        cMonth = c.get(Calendar.MONTH);
        cDay = c.get(Calendar.DAY_OF_MONTH);



        image = findViewById(R.id.postImage);
        image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 0);

            }

        });

        postDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener dateSetListener =
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                cYear = year;
                                cMonth = month;
                                cDay = dayOfMonth;
                                updateDisplay();
                            }
                        };
                DatePickerDialog alert=new DatePickerDialog(CreatePost.this, dateSetListener,cYear,cMonth,cDay);
                alert.show();
            }
        });


        postConfirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                postNameText = postName.getText().toString();
                postContentText = postContent.getText().toString();
                uploadFile();

            }
        });

    }

    private void updateDisplay() {
        viewDate.setText(new StringBuilder().append(cYear).append("년 ").append(cMonth + 1).append("월 ").append(cDay).append("일"));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //request코드가 0이고 OK를 선택했고 data에 뭔가가 들어 있다면
        if (requestCode == 0 && resultCode == RESULT_OK) {
            filePath = data.getData();

            try {

                //Uri 파일을 Bitmap으로 만들어서 ImageView에 집어 넣는다.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                int height = bitmap.getHeight();
                int width = bitmap.getWidth();
                //bitmap=Bitmap.createScaledBitmap(bitmap,160,height/(width/160),true);
                image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //getExif(filePath);
    }

    public void getExif(Uri uri) {

        boolean isDone;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
            cursor.moveToNext();
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
            cursor.close();
            ExifInterface exif = new ExifInterface(path);

            isDone = exif.getLatLong(latlng);  // 성공적으로 읽을 시 true 리턴
            if (isDone) {
                Log.d("latlng", latlng[0] + " " + latlng[1]);
            } else {
                Log.d("latlng", "no");
            }
            // latlng[0] : 위도
            // latlng[1] : 경도
            // mView.setText(latlng[0] + " " + latlng[1]);
            // showExif(exif);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error!", Toast.LENGTH_LONG).show();
        }

    }


    private void uploadFile() {
        //업로드할 파일이 있으면 수행
        if (filePath != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHH_mmss");
            Date now = new Date();
            filename = formatter.format(now) + ".png";
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl("gs://termproject-12d58.appspot.com/");
            final StorageReference imageRef = storageRef.child("images/" + filename);
            Log.d("Upload", filename);
            UploadTask uploadTask = imageRef.putFile(filePath);
            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return imageRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Log.d("Upload", "ins");
                        Intent i = new Intent();
                        i.putExtra("uri", task.getResult().toString());
                        i.putExtra("postName", postNameText);
                        i.putExtra("postContent", postContentText);
                        //i.putExtra("latitude",latlng[0]);
                        //i.putExtra("longitude",latlng[1]);
                        setResult(11, i);
                        finish();
                    } else {
                        Log.d("fileUpload", "fail");
                    }
                }
            });
        }
    }

}
