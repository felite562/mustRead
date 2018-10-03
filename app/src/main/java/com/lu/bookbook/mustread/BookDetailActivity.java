package com.lu.bookbook.mustread;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;



public class BookDetailActivity extends AppCompatActivity {
    private String url;
    ProgressDialog pd;
    private ImageView ivBookCover;
    private TextView tvTitle;
    private TextView tvAuthor;
    private TextView tvPublisher;
    private TextView tvPageCount;
    ImageView ivTest;
    public static SQLiteHelper mSQLiteHelper;
    byte[] byteArray;
    Uri myURI;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
       // ivTest = (ImageView) findViewById(R.id.iTest);
        ivBookCover = (ImageView) findViewById(R.id.ivBookCover);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvAuthor = (TextView) findViewById(R.id.tvAuthor);
        Book book = (Book) getIntent().getSerializableExtra(KeyActivity.BOOK_DETAIL_KEY);

        //creating database
        mSQLiteHelper = new SQLiteHelper(this, "RECORDDB.sqlite", null, 1);

        loadBook(book);
    }
    // Populate data for the book
    private void loadBook(Book book) {
        //change activity title
        this.setTitle(book.getTitle());
        // Populate data
        Picasso.with(this).load(Uri.parse(book.getLargeCoverUrl())).error(R.drawable.ic_nocover).into(ivBookCover);

        url=book.getCoverUrl();

        new Downloader().execute(url);

        tvTitle.setText(book.getTitle());
        tvAuthor.setText(book.getAuthor());
        // fetch extra book data from books API


    }


    private class Downloader extends AsyncTask<String,Void,Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd=new ProgressDialog(BookDetailActivity.this);
            pd.setTitle("Image downloader");
            pd.setMessage("my mes dialog");
            pd.setIndeterminate(false);
            pd.show();
        }

        @Override
        protected Bitmap doInBackground(String... url) {

            //first loader starts
            String myUrl = url[0];
            Bitmap bm=null;
            try {
                InputStream inStream =new URL(myUrl).openStream();
                bm= BitmapFactory.decodeStream(inStream);
            }
            catch (Exception e){e.printStackTrace();}

            return bm;

            //first loader ends




        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);

            //imageView.setImageBitmap(result);

            mSQLiteHelper.insertData(
                    tvTitle.getText().toString().trim(),
                    tvAuthor.getText().toString().trim(),

                    // imageViewToByte(ivTest)
                    BitmapToByte(result)
                    //bytes


            );


            startActivity(new Intent(BookDetailActivity.this, ListActivity.class));
            pd.dismiss();
        }
    }
    public static byte[] BitmapToByte( Bitmap bitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
}








