package com.example.downloadimages;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
    }

    public void downloadImage(View view){
        ImageDownloader task = new ImageDownloader();
        Bitmap myImage;

        try{
            myImage = task.execute("https://vignette.wikia.nocookie.net/simpsons/images/6/65/Bart_Simpson.png/revision/latest/top-crop/width/360/height/360?cb=20190409004756").get();
            imageView.setImageBitmap(myImage);
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    // here we will take url as string and return a image which is bitmap
    public class ImageDownloader extends AsyncTask<String,Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... strings) {
            try{
                URL url = new URL(strings[0]);
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.connect();
                InputStream in = connection.getInputStream();
                // this is the part where we decode the image and put it on the app
                Bitmap mybitmap = BitmapFactory.decodeStream(in);
                return mybitmap;
            } catch (Exception e){
                e.printStackTrace();
                return null;
            }

        }
    }
}
