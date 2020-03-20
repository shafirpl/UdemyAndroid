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
 *
 * We have some extra stuff to do, recall in MapsActivity.java we were converting latLang object to string arraylist, as our
 * object serilizer is not designed to handle latLang object. However, we need to store latLang objects in order for our map to
 * properly display the locations. So here we will be grabbing the strings after converting them from the arraylist stored in our
 * sharedPreference, and then we need to create latLang objects from those string and then store them
 *
 * Serialize convert arraylist to string so that we can store them in the sharedPreference, and then deserialize convert them back to arraylist of string from
 * string from the sharedPreference
 *
 * Every application has only one sharedPreference which is shared among classes, that is why we don't need to get ourselves worry too much
 * about passing. As long as we initialize the sharedPreference in our class with the application name, we should have access to the sharedPreference
 * of our application.
 */
package com.example.memorableplaces;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.memorableplaces", Context.MODE_PRIVATE);

        ArrayList<String> latitudes = new ArrayList<>();
        ArrayList<String> longitudes = new ArrayList<>();

        /*
        * We need to clear out our arraylists before storing new arraylist to ensure no duplication
         */
        this.places.clear();
        latitudes.clear();
        longitudes.clear();
        locations.clear();

        /*
         * here we are deserlizing the string we would get back from the sharedpreference
         * In case we cannot find the sharedpreference, the deserialize is expecting a string to convert it to an array, and that string has particular structure
         * so passing an empty string will break it if we cannot find the sharedpreference,
         * that is why we are passing another object serilizer to convert an empty arraylist to a sutiable string that wouldn't break the deserializer
         *
         * Recall we are storing the places in MapsActivity.java class
         */
        try {
            this.places = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("places",ObjectSerializer.serialize(new ArrayList<String>())));
            latitudes = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("lats", ObjectSerializer.serialize(new ArrayList<String>())));
            longitudes = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("lons", ObjectSerializer.serialize(new ArrayList<String>())));
        }catch (Exception e){
            e.printStackTrace();
        }

        /*
        * Recall if everything works out, the places arraylist, latitude and longitude arraylist will have something
        * Also remember, for every address, we store their string address in places arraylist, and then their latLang object location
        * in another arraylist, which we broke down into 2 other string arraylists, lattidue and longitdue as our objectSeriliazer cannot handle latLang
        * object and we were storing those latLang by breaking them into lattiude and longitude string arraylists (as sharedpreference can only
        * store string so we need to convert them/the arraylists to string in order to store them in the sharedPreference)
        * Before coming here, we were populating/retreiving those string arraylist into lattitude and longitude from sharedPreference. So for every address, we will have similar number of
        * items in these three arrays. For example, for 2180 West 38th Avenue and 2181 west 38th avenue, we will have 2 string addresses, 2 lattitude and 2 longitude
        * (one address, lattitude and longitude from 2180 west 38th and another from 2181 west 38th Avenue)
        * Now since they are string, and our latLang object requires double to work, we need to parse it into double to create the new latLang object
         */
        if(places.size() > 0 && latitudes.size()>0 && longitudes.size()>0) {
            if (places.size() == latitudes.size() && places.size() == longitudes.size()){
                for(int i = 0; i<latitudes.size(); i++){
                    LatLng latLng = new LatLng(Double.parseDouble(latitudes.get(i)), Double.parseDouble(longitudes.get(i)));
                    locations.add(latLng);
                }
            }
        } else{
            /*
            * This else part means we don't have anything, which is when we open the app for the very first time
             */
            places.add("Add a new place ...");
            locations.add(new LatLng(0,0));

        }


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
