package com.example.switching_image;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void switchImage(View view){
        ImageView img= (ImageView) findViewById(R.id.imageView4);
        img.setImageResource(R.drawable.iphone);
    }
}
