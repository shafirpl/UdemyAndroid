package com.example.friendlistmultiactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        // this is how we retrieve the information passed from a screen and use it in this screen
        Intent intent = getIntent();
        // since we are getting a string from the previous screen, we use StringExtra
        // we also have to put a default value if for some reason the intent didn't get passed through here
        // also notice the name of the passed info has to be exact match, from previous screen
        // we were passing "name", so it has to match that
        String name = intent.getStringExtra("name");

        Toast.makeText(this,name,Toast.LENGTH_SHORT).show();
    }

    public void goBack(View view){
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);

        startActivity(intent);

        /*
         * https://www.udemy.com/course/the-complete-android-oreo-developer-course/learn/lecture/8339514?start=45#questions
         * Watch from 9:09
         */
        finish();
    }
}
