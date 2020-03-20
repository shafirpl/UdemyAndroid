/*
* https://www.udemy.com/course/the-complete-android-oreo-developer-course/learn/lecture/8339528#questions/9634904
* Watch from start upto 4:16
* Then we need to create the function onCreateOptionsMenu to get access to the menu and/or initialize it
* then in order to trigger an action when we select one of the items from the menu, we use onOptionsItemSelected function
 */
package com.example.menudemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        /*
        * Here we are trying to figure out which item the user selected and take necessary actions accordingly
        * Look athe the main_menu.xml file under res/menu, there I have 2 items with id settings and help
         */
        switch(item.getItemId()){
            case R.id.help:
                Log.i("Item selected","help");
                return true;
            case R.id.settings:
                Log.i("Item selected","Settings");
                return true;
            default: return false;
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
