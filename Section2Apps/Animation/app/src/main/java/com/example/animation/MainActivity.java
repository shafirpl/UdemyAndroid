package com.example.animation;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

/*
* This app basically fades in either bart or homer based on
* who was in the image view at first on the click on the image
*/

public class MainActivity extends AppCompatActivity {

    public void fade(View view){
        Log.i("Info","View Clicked");

        // finding the imageviews
        ImageView bart_imageView = (ImageView) findViewById(R.id.bart_imageView);
        ImageView homer_imageView = (ImageView) findViewById(R.id.homer_imageView);

        bart_imageView.animate().alpha(0).setDuration(2000);
        homer_imageView.animate().alpha(1).setDuration(2000);

        Log.i("Alpha Value","Image alpha value is"+bart_imageView.getAlpha());

        // doing stuff
        if(bart_imageView.getAlpha() == 1){
            bart_imageView.animate().alpha(0).setDuration(2000);
            homer_imageView.animate().alpha(1).setDuration(2000);
        }

        else{
            homer_imageView.animate().alpha(0).setDuration(2000);
            bart_imageView.animate().alpha(1).setDuration(2000);
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
