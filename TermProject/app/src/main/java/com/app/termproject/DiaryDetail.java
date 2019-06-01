package com.app.termproject;

import android.app.AlertDialog;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DiaryDetail extends AppCompatActivity
{

    android.support.design.widget.FloatingActionButton deleteButton;
    android.support.design.widget.FloatingActionButton editButton;
    String pinnumber;
    String key;
    String filename,originalFileName;
    String nameEdit,contentEdit,imageEdit,original,date;
    ImageView iv;
    Uri filePath;
    int flag=1;
    TextView diaryTitle,diaryContent,diaryDate;
    EditText title, content;


    float[] latlng = new float[2];


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
                    GetPost post=new GetPost(pinnumber,nameEdit,original,contentEdit,latlng[0],latlng[1],originalFileName,date);
                    post.writeNewPost(pinnumber,nameEdit,original,contentEdit,latlng[0],latlng[1],originalFileName,date);

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

            getExif(filePath);
        }

    }
    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @author paulburke
     */
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
    public void getExif(Uri uri) {

        String fileRealPath = getPath(getApplicationContext(), filePath);
        try{
            ExifInterface exif = new ExifInterface(fileRealPath);
            boolean isDone = exif.getLatLong(latlng);  // 성공적으로 읽을 시 true 리턴
            if (isDone) {
                Log.d("latlng", latlng[0] + " " + latlng[1]);
            }
            else
            {
                enteraddress();
                Log.d("latlng", "no");
            }
        }
        catch(Exception e){  }


    }





    private void uploadFile() {
        //업로드할 파일이 있으면 수행
        Log.d("Upload",filePath.toString());
        if (filePath != null) {
            final ProgressDialog progressDialog=new ProgressDialog(this, R.style.MyAlertDialogStyle);
            progressDialog.setTitle("열심히 업로드 중이에요!\n잠시만 기다려주세요");

            progressDialog.show();


            Log.d("Upload",filePath.toString());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHH_mmss");
            Date now = new Date();
            filename= formatter.format(now)+".png";
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl("gs://termproject-12d58.appspot.com/");
            final StorageReference imageRef = storageRef.child("images/"+filename);






            try {
                Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                bmp = resize(getApplicationContext(), filePath, 250);


                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                byte[] data = baos.toByteArray();
                Log.d("Upload", filename);
                UploadTask uploadTask = imageRef.putBytes(data);


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
                            imageEdit = task.getResult().toString();
                            Log.d("Upload", "ins");
                            String name = title.getText().toString();
                            if (name.length() > 0) {
                                nameEdit = name;
                            }
                            String conten = content.getText().toString();
                            if (conten.length() > 0) {
                                contentEdit = conten;
                            }
                            delete();
                            GetPost post = new GetPost(pinnumber, nameEdit, imageEdit, contentEdit, latlng[0], latlng[1], filename, date);
                            post.writeNewPost(pinnumber, nameEdit, imageEdit, contentEdit, latlng[0], latlng[1], filename, date);
                            //i.putExtra("latitude",latlng[0]);
                            //i.putExtra("longitude",latlng[1]);
                            progressDialog.dismiss();
                        } else {
                            Log.d("fileUpload", "fail");
                        }

                    }
                });
            }
            catch (Exception e) {}
        }
    }






    private Bitmap resize(Context context,Uri uri,int resize){
        Bitmap resizeBitmap=null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        try {
            BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, options); // 1번

            int width = options.outWidth;
            int height = options.outHeight;
            int samplesize = 1;

            while (true) {//2번
                if (width / 2 < resize || height / 2 < resize)
                    break;
                width /= 2;
                height /= 2;
                samplesize *= 2;
            }

            options.inSampleSize = samplesize;
            Bitmap bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, options); //3번
            resizeBitmap=bitmap;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return resizeBitmap;
    }








    public void getLat(String str)
    {
        final Geocoder geocoder = new Geocoder(this);
        List<Address> list = null;

        try {
            list = geocoder.getFromLocationName(
                    str, // 지역 이름
                    10); // 읽을 개수
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("test","입출력 오류 - 서버에서 주소변환시 에러발생");
        }

        if (list != null) {
            if (list.size() == 0) {
                Toast.makeText(this,"없음",Toast.LENGTH_LONG).show();
            }
            else
            {
                latlng[0]=(float)list.get(0).getLatitude();
                latlng[1]=(float)list.get(0).getLongitude();
                Toast.makeText(this,list.get(0).getLatitude()+" "+list.get(0).getLongitude(),Toast.LENGTH_LONG).show();
            }
        }
    }
    public void enteraddress()
    {
        final EditText editText=new EditText(this);
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("장소가 없습니다");
        builder.setMessage("장소를 입력해주세요!");
        builder.setView(editText);
        builder.setPositiveButton("입력",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (editText.getText().length() == 0) {
                            ALERT alert = new ALERT(DiaryDetail.this,"장소를 입력해주세요~");
                            alert.setDialogListener(new ALERT.ALERTListener() {
                                @Override
                                public void onButtonClicked() {
                                    enteraddress();
                                }
                            });
                            alert.show();

                        }
                        else {
                            getLat(editText.getText().toString());
                        }
                    }
                });
        builder.show();

    }







}

