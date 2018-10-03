package com.lu.bookbook.mustread;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class UpdateActivity extends AppCompatActivity {
    ListView mListView;
    ArrayList<Model> mList;
    ListAdapter mAdapter = null;
    ImageView imageViewIcon;
    public SQLiteHelper mSQLiteHelper;
    //creating database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        mSQLiteHelper = new SQLiteHelper(this, "RECORDDB.sqlite", null, 1);

        imageViewIcon = findViewById(R.id.iViewBook);
        final EditText edtName = findViewById(R.id.tViewName);
        final EditText edtAuthor = findViewById(R.id.tViewAuthor);

        Button btnUpdate = findViewById(R.id.btnUpdate);



        //changed from cursor
        Cursor cursor = mSQLiteHelper.getData("SELECT * FROM RECORD");


        while (cursor.moveToNext()) {

            int id = cursor.getInt(0);

            String name = cursor.getString(1);
            edtName.setText(name);
            String author = cursor.getString(2);

            edtAuthor.setText(author);
            byte[] image = cursor.getBlob(3);
            imageViewIcon.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));
            //add to list
            mList.add(new Model(id, name, author, image));

            // edtName.setText("");
            // edtAuthor.setText("");


        }
        cursor.close();





}

}
