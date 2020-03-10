/*
* To navigate to other page, we have to do these steps
* 1. Create an intent
* 2. Pass the intent to startActivity function
* 3. Also https://www.udemy.com/course/the-complete-android-oreo-developer-course/learn/lecture/8339514?start=45#questions: watch
* from 9:09
* 4. If we wanna pass information from one screen to another, we do intent.putExtra()
*
 */

package com.example.multiactivitydemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /*
    * This function will get triggered when we click the go to next page button, and
    * will take us to the second activity page
    *
     */
    public void goToNext(View view){
        /*
        * This function will create an intent,
        * it takes the application context, the activity we wanna move to
        * so for which we have to copy paste the class name of that activity
        *
         */
        Intent intent = new Intent(getApplicationContext(),SecondActivity.class);

        // this is how we pass information to the other screen from here
        intent.putExtra("age",24);

        /*
        * This function will actually do the navigation stuff
        * it takes in the intent we created earlier
         */
        startActivity(intent);

        /*
        * https://www.udemy.com/course/the-complete-android-oreo-developer-course/learn/lecture/8339514?start=45#questions
        * Watch from 9:09
         */
    }
}
