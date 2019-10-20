package com.example.otheranimations;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    /*
    * For this, we couldn't use constrains on the activity.main.xml as it wouldn't take
    * negative values. So we had to use a function call to start him hidden on the left side
    * of device screen
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView bartImageView = (ImageView) findViewById(R.id.bartImageView);
        bartImageView.setX(-2000f);

        // this 3600 will rotate him 10 times, 360 will rotate once, and then multiplied
        // by 10 will rotate him 10 times
        bartImageView.animate().translationXBy(2000f).rotation(3600).setDuration(2000);
    }

    public void fade(View view){
        ImageView bartImageView = (ImageView) findViewById(R.id.bartImageView);
        // move mr bart to the left
        bartImageView.animate().translationXBy(-2000).setDuration(2000);

    }
}
