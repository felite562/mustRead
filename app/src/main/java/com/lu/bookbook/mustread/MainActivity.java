package com.lu.bookbook.mustread;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;



public class MainActivity extends AppCompatActivity {
    EditText medtName, mEdtAuthor;
    Button btnSave, btnList;
    ImageView mIView;
    final int REQUEST_CODE_GALLERY=999;
    public static SQLiteHelper mSQLiteHelper;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actoinBar =getSupportActionBar();
        actoinBar.setTitle("New book");
        medtName=findViewById(R.id.edtName);
        mEdtAuthor=findViewById(R.id.edtAutor);
        btnSave=findViewById(R.id.btnSave);
        btnList=findViewById(R.id.btnList);
        mIView =findViewById(R.id.iView);
        final ProgressBar pBar = (ProgressBar) findViewById(R.id.pBar);
        pBar.setVisibility(ProgressBar.INVISIBLE);
        //creating database
        mSQLiteHelper = new SQLiteHelper(this, "RECORDDB.sqlite", null, 1);


        // mSQLiteHelper.queryData("CREATE TABLE IF NOT EXISTS RECORD(id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, author VARCHAR, image BLOB)");




        mIView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //read external storage permission to select image from gallery
                //runtime permission for devices android 6.0 and above
                ActivityCompat.requestPermissions(
                        MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {pBar.setVisibility(ProgressBar.VISIBLE);
                    mSQLiteHelper.insertData(

                            medtName.getText().toString().trim(),
                            mEdtAuthor.getText().toString().trim(),

                            imageViewToByte(mIView)
                    );
                    Toast.makeText(MainActivity.this, "Added successfully", Toast.LENGTH_SHORT).show();
                    //reset views


                    mIView.setImageResource(R.drawable.addpic);
                    mSQLiteHelper.close();
                    pBar.setVisibility(ProgressBar.INVISIBLE);
                    medtName.setText("");
                    mEdtAuthor.setText("");
                }
                catch (Exception e){
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });



        //show record list
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start recordlist activity
                startActivity(new Intent(MainActivity.this, ListActivity.class));
                finish();
            }
        });

    }



    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_GALLERY){
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //gallery intent
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, REQUEST_CODE_GALLERY);
            }
            else {
                Toast.makeText(this, "Don't have permission to access file location", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON) //enable image guidlines
                    .setAspectRatio(1,1)// image will be square
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result =CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){
                Uri resultUri = result.getUri();
                //set image choosed from gallery to image view
                mIView.setImageURI(resultUri);
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}