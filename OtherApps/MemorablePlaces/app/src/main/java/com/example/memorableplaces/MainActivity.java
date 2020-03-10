/*
 * So we need to do several things
 * We need to get permission from the user to use fine location stuff in order to get access to the map (look at the
 * MapsActivity.java comment section)
 * We need to do stuff (look at the MapsActivity.java comment section) to get correct location
 * And then we have to create an intent to jump from this page to the map page
 *
 * We will be sharing 2 variables and 1 array adapter betweeen this class and the MapsActivity class, this is why
 * we have line 49-51 where we declared them static and declared them at the top of the class
 *
 * To navigate to other page (creating an intent) , we have to do these steps
 * 1. Create an intent
 * 2. Pass the intent to startActivity function
 * 3. Also https://www.udemy.com/course/the-complete-android-oreo-developer-course/learn/lecture/8339514?start=45#questions: watch
 * from 9:09
 * 4. If we wanna pass information from one screen to another, we do intent.putExtra()
 *
 * The way the app works is that:
 * https://www.udemy.com/course/the-complete-android-oreo-developer-course/learn/lecture/8339518#questions
 * Watch from 27:36
 * so at first it opens up to this page/activity, where we have a list view with only one item, Add Place...
 * All the items have click listener, so when we click that, it goes to the maps page and passes the index
 * based on the index of the array item, we do different stuff.
 */
package com.example.memorableplaces;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;



public class MainActivity extends AppCompatActivity {
    /*
     * Recall we use an array list and then an array adapter to create a list inside the list view
     * Go through the list view section on google docs to refresh my memory
     */

    /*
    * Putting static and at the top of the class will allow us to get access
    * to this variables in other classes as well, including maps_activity
     */

    static ArrayList<String> places = new ArrayList<String>();
    static ArrayList<LatLng> locations = new ArrayList<LatLng>();
    static ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.listView);


        places.add("Add a new place ...");
        locations.add(new LatLng(0,0));

        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,places);

        listView.setAdapter(arrayAdapter);

        /*
        * This will add a click listener to individual item, so when we click any individual item on the list, the click listener gets
        * activated and does stuff
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            // here the argument i refers to the index of the item from the array was clicked on
            // if lets say the arraylist is a,b,c,d, and we click on lets say b, then i = 1 as
            // index of b is 1
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                /*
                 * This function will create an intent,
                 * it takes the application context, the activity we wanna move to
                 * so for which we have to copy paste the class name of that activity
                 *
                 */
                Intent intent = new Intent(getApplicationContext(),MapsActivity.class);

                // this is how we pass information to the other screen from here
                intent.putExtra("placeNumber",i);
                // Toast.makeText(getApplicationContext(),Integer.toString(i),Toast.LENGTH_SHORT).show();

                startActivity(intent);
            }
        });
    }
}
