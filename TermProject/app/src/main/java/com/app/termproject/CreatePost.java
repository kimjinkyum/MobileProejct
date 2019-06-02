package com.app.termproject;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
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
import android.widget.TextView;
import android.widget.Toast;

import com.app.termproject.DB.GetDiary;
import com.app.termproject.DB.GetPost;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CreatePost extends AppCompatActivity {

    ImageView image,weather;
    EditText postName;
    EditText postContent;
    TextView viewDate;
    String postContentText;
    String weatherString="해";
    ImageButton postConfirmButton;
    ImageButton postDate;
    private Uri filePath;
    String postNameText;
    String filename;

    int cYear;
    int cMonth;
    int cDay;
    String date;
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
        weather=findViewById(R.id.weather);

        registerForContextMenu(weather);

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
                if (postName.getText().length() == 0 || postContent.getText().length() == 0) {
                    ALERT alert = new ALERT(CreatePost.this,"제목과 내용을 입력해주세요~");
                    alert.setDialogListener(new ALERT.ALERTListener() {
                        @Override
                        public void onButtonClicked() {
                        }
                    });
                    alert.show();
                }
                else {
                    postNameText = postName.getText().toString();
                    postContentText = postContent.getText().toString();
                    uploadFile();
                }


            }
        });

    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu,v,menuInfo);

        menu.setHeaderTitle("날씨를 골라주세요~");
        menu.add(0,1,100,"해");
        menu.add(0,2,100,"눈");
        menu.add(0,3,100,"구름");
        menu.add(0,4,100,"비");
        menu.add(0,5,100,"구름 조금");
    }

    public boolean onContextItemSelected(MenuItem item){
        switch (item.getItemId()){
            case 1:
                weatherString="해";
                weather.setImageResource(R.drawable.ic_sunny);
                return true;
            case 2:
                weatherString="눈";
                weather.setImageResource(R.drawable.ic_snow);
                return true;
            case 3:
                weatherString="구름";
                weather.setImageResource(R.drawable.ic_cloud);
                return true;
            case 4:
                weatherString="비";
                weather.setImageResource(R.drawable.ic_rain);
                return true;
            case 5:
                weatherString="구름조금";
                weather.setImageResource(R.drawable.ic_littlecloud);
                return true;
        }
        return super.onContextItemSelected(item);
    }
    private void updateDisplay()
    {

        viewDate.setText(new StringBuilder().append(cYear).append("년 ").append(cMonth + 1).append("월 ").append(cDay).append("일"));
        String month=Integer.toString(cMonth+1);
        String day=Integer.toString(cDay);
        if(month.length()==1)
            month="0"+month;
        if(day.length()==1)
            day="0"+day;
        date=Integer.toString(cYear)+month+day;
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
        if (filePath != null)
        {
            final ProgressDialog progressDialog=new ProgressDialog(this, R.style.MyAlertDialogStyle);
            progressDialog.setTitle("열심히 업로드 중이에요!\n잠시만 기다려주세요");


            progressDialog.show();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHH_mmss");
            Date now = new Date();
            filename = formatter.format(now) + ".png";
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl("gs://termproject-12d58.appspot.com/");
            final StorageReference imageRef = storageRef.child("images/" + filename);

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

                        if(task.isSuccessful())
                        {   Log.d("Upload", "ins");
                            progressDialog.dismiss();
                            Intent i=new Intent();
                            i.putExtra("uri",task.getResult().toString());
                            i.putExtra("postName",postNameText);
                            i.putExtra("postContent",postContentText);
                            i.putExtra("fileName",filename);
                            i.putExtra("date",date);
                            i.putExtra("latitude",latlng[0]);
                            i.putExtra("longitude",latlng[1]);
                            i.putExtra("weather",weatherString);
                            setResult(11, i);
                            finish();
                        }else
                        {
                            Intent i=new Intent();
                            i.putExtra("uri","https://firebasestorage.googleapis.com/v0/b/termproject-12d58.appspot.com/o/images%2Fbasic%20image.png?alt=media&token=d594ba53-6d78-46f4-a8e1-023a886abb0b");
                            i.putExtra("postName",postNameText);
                            i.putExtra("postContent",postContentText);
                            i.putExtra("fileName","basic image");
                            i.putExtra("date",date);
                            i.putExtra("latitude",latlng[0]);
                            i.putExtra("longitude",latlng[1]);
                            i.putExtra("weather",weatherString);
                            setResult(11, i);
                            finish();
                        }
                    }
                });
            }
            catch (Exception e) {}
            //uploading the image
            //UploadTask uploadTask2 = childRef2.putBytes(data);
            /*
            uploadTask2.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(Profilepic.this, "Upload successful", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Profilepic.this, "Upload Failed -> " + e, Toast.LENGTH_LONG).show();
                }
            });
            */











        }
        else
            {
                Intent i=new Intent();
                i.putExtra("uri","https://firebasestorage.googleapis.com/v0/b/termproject-12d58.appspot.com/o/images%2Fbasic%20image.png?alt=media&token=d594ba53-6d78-46f4-a8e1-023a886abb0b");
                i.putExtra("postName",postNameText);
                i.putExtra("postContent",postContentText);
                i.putExtra("fileName","basic image");
                i.putExtra("date",date);
                i.putExtra("latitude",latlng[0]);
                i.putExtra("longitude",latlng[1]);
                setResult(11, i);
                finish();
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
            if (list.size() == 0)
            {
                enteraddress();
            }
            else
                {
                    latlng[0]=(float)list.get(0).getLatitude();
                    latlng[1]=(float)list.get(0).getLongitude();

            }
        }
    }
    public void enteraddress()
    {
        final SearchingPIN searchingPIN = new SearchingPIN(this,"address");
        searchingPIN.setDialogListener(new SearchingPIN.SearchingPINListener() {
            @Override
            public void onPositiveClicked(String pin) {
                                   }

            @Override
            public void onNegativeClicked(String address)
            {

                if(address.length()==0)
                    address="temp";
                getLat(address);

            }
        });
        searchingPIN.show();
    }

}
