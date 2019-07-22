package com.example.cameraproject;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class MainActivity extends AppCompatActivity {
    private ImageView mCameraImage;
    private Button btCapture;
    private File createPhotoFile;
    private Uri mPhotoUri;
    private static final int TAKE_PICK_REQUEST = 100;
    private String pathToFile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCameraImage=( ImageView) findViewById(R.id._iv_camera_image_id);
        btCapture= (Button)findViewById(R.id.bt_cameraimage_id);

        btCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selfie();
            }
        });
        if (Build.VERSION.SDK_INT > 22){
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},2);
        }


    }

    private void selfie() {
        //Setting an Action for the Camera
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
       if (captureIntent.resolveActivity(getPackageManager())!= null){
           File capturePhotoFile = null;
           //saving photo to the photo file variable
           capturePhotoFile = createPhotoFile();
           if (capturePhotoFile!= null);{
           String pathToFile = capturePhotoFile.getAbsolutePath();
           mPhotoUri = FileProvider.getUriForFile(MainActivity.this,"com.cameraapp.fileprovider",capturePhotoFile);
           captureIntent.putExtra(MediaStore.EXTRA_OUTPUT,mPhotoUri);}
           startActivityForResult(captureIntent,TAKE_PICK_REQUEST);


       }


       else{
           Toast.makeText(this, " no app available to open camera", Toast.LENGTH_SHORT).show();

        }
    }

    private File createPhotoFile() {
        //creating a name for the file where a photo will be stored
        String fileName = new SimpleDateFormat("yyyyMMMDD_HHmmss").format(new Date());
        //setting a directory to allow other apps to access your photos
        File storageDirectory = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        //creating the actual file
        File imageFile = null;
        try {imageFile = File.createTempFile(fileName,"jpg",storageDirectory);



        }catch (Exception e){Log.d("myLog","Except:"+e.toString());



        }
        return imageFile;


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PICK_REQUEST && requestCode == RESULT_OK){

            Bitmap bitmap = BitmapFactory.decodeFile(pathToFile);
            mCameraImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
            mCameraImage.setImageBitmap(bitmap);



        }
        else if(resultCode == RESULT_CANCELED){
            Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
        }
    }
}
