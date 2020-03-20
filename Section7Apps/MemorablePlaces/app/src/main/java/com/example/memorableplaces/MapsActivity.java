/*
 * make sure to add <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"></uses-permission> in
 * the manifest.xml file
 * To get the location and use it,
 * 1. we need location manager to get the location, and location listener to use it
 * 2. Afterwards we need to ask for permission (around line 201, search for the line if (Build.VERSION.SDK_INT < 23))
 * if its above android 6, as from android 6 we need permission from user to use
 * gps and other stuff, and assign a request code that we will use in step 3 to ensure that this permission was for GPS, nothing else
 * 3. Then line 94 (search for onRequestPermissionsResult function), which gets triggered as soon as user grants permission to anything,
 * 4. Then  call this.locationManager.requestLocationUpdates() inside step 3 function to set up the location manager to send location updates to the app, and connect
 * it to the location listener
 * 5. Then finally use the location listener in line 172, here the function at line 174 (onLocationChanged function) is particularly interesting, as it gets
 * fired any time the system sends update to the app
 * Now the way our app works, we get the index of the array that gets passed to this activity, if its 0, that means the user wants to add
 * a place, (line 168 or search if (placeNumber == 0)) we initiate the location listener and call centerMapOnLocation function inside onLocationChanged
 * function, which puts a marker on the user's current location, moves and zooms the camera there. We also allow the user to add a new place by allowing
 * them to long press a location on the map, which happnes in line 158: mMap.setOnMapLongClickListener(this); where we set up the long press
 * stuff, and then write appropriate code inside public void onMapLongClick(LatLng latLng), which gets activated when user presses for long
 * Inside that function, we use GeoCode to turn locations into human redable address, use that to build up the address string, store that address string
 * inside MainActivity.places (the places arraylist coming from MainActivity class) as well as the actual lattitude/longitude inside
 * MainActivity.locations (the locations arraylist coming from MainActivity class), also we need to update the array adapter associated with
 * places arraylist.
 * Now if the user presses any other button, we do the else part (around line 241 or search for int indexOfPlace = intent.getIntExtra("placeNumber",0);), where
 * we fetch the necessary address and location and set the marker there
 *
 *
 *
 * I have a weird issue, if i change the map, like dragging inside the map to some place, it
 * would center on current location, watch the video inside videos folder in section6app
 * The way I fixed this was setting the minDistance to 5 meters in locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 5, locationListener);
 * So that the system notifies the app about the new location if it detects the user has moved 5 meters
 * The reason for that issue was that with 0 minDistance and minTime, it would continiously call onLocationChanged, which in turn will call centerMapOnLocation
 * which means the app will continioulsy zoom on the user location, no matter what we do, so by dragging to other places, we would face
 * the same exact issue.
 *
 * sharedpreference is a way to store values in the app memory, however it has lot of limitation
 * one limitation is that it cannot take array, so we need to use an seriliazer to convert array to string
 * and then store it, and then when we retrieve it, we need to use deserilizer to convert back to array from string
 * Now how the ObjectSerilizer works wasn't explained, so assume it is a library (we copy pasted the code)
 *
 * Serialize convert arraylist to string so that we can store them in the sharedPreference, and then deserialize convert them back to arraylist of string from
 * string from the sharedPreference
 *
 * Now since our objectserlizer can only handle strings and arrays, we need to convert latLang objects to string arraylist, store them, and then in MainActivity.java we need to
 * grab those string arraylist and then make new latlang object in order to display them on the map, as our map expects latLang object in order to properly work
 *
 * Every application has only one sharedPreference which is shared among classes, that is why we don't need to get ourselves worry too much
 * about passing. As long as we initialize the sharedPreference in our class with the application name, we should have access to the sharedPreference
 * of our application.
 */


package com.example.memorableplaces;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {
    LocationManager locationManager;
    LocationListener locationListener;
    private GoogleMap mMap;
    private Location previousLocation;

    /*
     * This function basically centers/zooms in a given location
     */
    public void centerMapOnLocation(Location location, String title) {

        /*
         * getting the coordinates
         * location here is the argument
         */

        if (location != null){
            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
            /* if we don't clear the map, every time the location changes we get a new marker, so there will be markers all over the place */
            mMap.clear();
            /* adding marker to our location using the coordinates */
            mMap.addMarker(new MarkerOptions().position(userLocation).title(title));
            /* focusing the app to that marker and zooming in */
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 12));
        }

    }

    // this function finds if the user said yes or no to our permission question, and do the necessary stuff
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // recall we assigned request code 1 to our permission question,
        // so if the user allows permission to our gps question, this function will get triggered and
        // the code it will receive will be 1
        // we might have different permission such as bluetooth permission question and stuff like that, so if we assign 2
        // to bluetooth and user gives permission, this function will get triggered with code 2

        // checking if the user granted permission
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    /*
                     * https://developer.android.com/reference/android/location/LocationManager#requestLocationUpdates(java.lang.String,%20long,%20float,%20android.location.LocationListener)
                     * so providing 0 for min time means update all the time, if we provide let's say 100, it will only send your application an update after every
                     * 100 ms
                     * also the location provider will only send your application an update when the location has changed by at least minDistance meters, 0 means it will send
                     * update whenever a location change is detected
                     * So location manager gets the location and passes it to location listener, location listener does all other stuff like
                     * Logging the info etc
                     */
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 5, locationListener);
                    // we are doing it because when the app runs for very first time, we wanna show the marker to the last known location
                    // this is just standard practice
                    Location lastknownlocation = this.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    this.centerMapOnLocation(lastknownlocation, "Your Location");
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        /*
        * We want to allow users to set a location and save it to the array list in the main activity, and we want that
        * to get activated when the user presses a long press. This code handles that
        * This will in fact trigger the onMapLongClick function
        * Ensure that the class implements GoogleMap.OnMapLongClickListener in order for this to work
        * https://www.udemy.com/course/the-complete-android-oreo-developer-course/learn/lecture/8339518#questions/6804706
        * Watch from 35:41
         */
        mMap.setOnMapLongClickListener(this);

        // this is how we retrieve the information passed from a screen and use it in this screen
        Intent intent = getIntent();
        // since we are getting an int from the previous screen, we use Int Extra
        // we also have to put a default value if for some reason the intent didn't get passed through here
        // also notice the name of the passed info has to be exact match, from previous screen
        // we were passing "placeNumber", so it has to match that
        int placeNumber = intent.getIntExtra("placeNumber", 0);

        // Toast.makeText(getApplicationContext(),Integer.toString(placeNumber), Toast.LENGTH_SHORT).show();

        if (placeNumber == 0) {
            // that means user clicked add a new place as this will be the first item (0th index) in the
            // array adapter
            // we will just zoom in to the user location
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                   // Log.i("Location changed called",location.toString());
                    centerMapOnLocation(location, "Your Location");
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            };

            // if the user is not using Android 6 Marshmallow, we don't need to ask for permission as the permission stuff got changed after version 23
            // so we just go ahead and use the location manager

            // this checks the user's android version
            if (Build.VERSION.SDK_INT < 23) {
                /*
                 * https://developer.android.com/reference/android/location/LocationManager#requestLocationUpdates(java.lang.String,%20long,%20float,%20android.location.LocationListener)
                 * so providing 0 for min time means update all the time, if we provide let's say 100, it will only send your application an update after every
                 * 100 ms
                 * also the location provider will only send your application an update when the location has changed by at least minDistance meters, 0 means it will send
                 * update whenever a location change is detected
                 * So location manager gets the location and passes it to location listener, location listener does all other stuff like
                 * Logging the info etc
                 */
                this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 5, this.locationListener);
            } else {
                /*
                 * here we are seeing that if we didn't get permission, we need to ask for it
                 */
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    /*
                     * here we are asking for permission if permission is not granted
                     * 1 means we are assigning code 1 to this request, it could be any number, if permission is granted, then
                     * onRequestPermissionsResult will run and receive requestCode 1, which signifies this function was run
                     * as the user gave permission to access fine location
                     */
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                } else {
                    // this else part is if the user already gave us the permission, which will happen when the app runs for first time and user gives permission
                    this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 5, this.locationListener);

                    // we are doing it because when the app runs for very first time, we wanna show the marker to the last known location
                    // this is just standard practice
                    Location lastknownlocation = this.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    this.centerMapOnLocation(lastknownlocation, "Your Location");

                }
            }
        } else {
            /*
            * If the user taps on a saved location, we fetch the appropriate location and address and put a marker there
             */
            Location placeLocation = new Location(LocationManager.GPS_PROVIDER);
            /*
            * Recall, when the user adds a location, we save the human readable address string inside places and the actual
            * longitude/latitude inside locations. Also we do that at the same time, so any saved address will have same index
            * in both the variables.
             */
            // grabbing the appropriate index
            int indexOfPlace = intent.getIntExtra("placeNumber",0);
            // setting lattitude and longitude
            placeLocation.setLatitude(MainActivity.locations.get(indexOfPlace).latitude);
            placeLocation.setLongitude(MainActivity.locations.get(indexOfPlace).longitude);
            // centering the map on the location
            centerMapOnLocation(placeLocation, MainActivity.places.get(indexOfPlace));
        }


    }

    /*
    * this function gets triggered whenever user does a long press
     */
    @Override
    public void onMapLongClick(LatLng latLng) {

        /*
        * geocoder turns a location to a meaningful human readable address
        * Locale.getdefault() means our app will make an address that is primarily used in in the user's counyry
        * for example, if the user is in Canada, the address format will be Canadian
         */
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        String address = "";
        try{
            // geocoder will retrieve a list of addresses, so we need a list, and 1 means we only want 1 matching result
            List<Address> listAddresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            // all other info we can fetch: https://developer.android.com/reference/android/location/Address.html
            // https://stackoverflow.com/questions/22096011/what-does-each-androids-location-address-method-return
            // ensure the list is not empty, and then do stuff
            if (listAddresses != null && listAddresses.size() > 0) {
                // use this logcat to see what key value pairs we have, for example
                // for my home address, I get this
                // admin=British Columbia,sub-admin=Greater Vancouver,locality=Vancouver,thoroughfare=West 38th Avenue,postalCode=V6M,countryCode=CA,countryName=Canada,
                // so we can use, like getLocality, to get the city name and stuff like that
                // Log.i("Place info:",listAddresses.get(0).toString());

                // this gives us street number such as 2180
                // we do the null check to ensure we have something to parse
                if (listAddresses.get(0).getSubThoroughfare() != null) {
                    address += listAddresses.get(0).getSubThoroughfare() + " ";
                }
                // this gives us street name such as West 38th Avenue
                // we do the null check to ensure we have something to parse
                if (listAddresses.get(0).getThoroughfare() != null) {
                    address += listAddresses.get(0).getThoroughfare() + " ";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // if the address field is empty, show a date as title:
        if(address.equals("")){
            /*
            * http://tutorials.jenkov.com/java-internationalization/simpledateformat.html
            * https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html
             */
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm yyyy-MM-dd");
            // new Date will fetch the current time from the device
            address += simpleDateFormat.format(new Date());
        }
        mMap.addMarker(new MarkerOptions().position(latLng).title(address));

        // accessing the variables from main activity class
        MainActivity.places.add(address);
        MainActivity.locations.add(latLng);
        // after this we need to update the array adapter
        MainActivity.arrayAdapter.notifyDataSetChanged();

        /*
        * Storing the places and locations to sharedpreference for permanent storage
         */

        /*
         * when we are initializing it, we need two parameters
         * Sharedpreference allow us to save app data permanently
         * the first parameter is the name of the app, which we copy from the top of the file, where it says package app_name
         * the second parameter is the mode we wanna save
         */
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.memorableplaces",Context.MODE_PRIVATE);
        try {
            /*
            * The problem is our serilizer takes array and convert it to string or vice versa, it is not designed to handle latlang objects, so we
            * need to do some stuff to store lat lang objects
             */
            ArrayList<String> latitudes = new ArrayList<>();
            ArrayList<String> longitudes = new ArrayList<>();

            for (LatLng cord:MainActivity.locations){
                latitudes.add(Double.toString(cord.latitude));
                longitudes.add(Double.toString(cord.longitude));

            }
            /*
             * Here we are saving a string
             * first we need to give the string a name
             * Then we have to provide the actual thing we are storing
             * Since we cannot store an array in shared preference, we are using seriliazation to convert it to string
             * and later when we retrieve it, we need to convert it back to an array
             * The objectSerializer class was copy pasted and for the time being assume it is for granted/library function
             * that someone wrote it for us
             */
            sharedPreferences.edit().putString("places",ObjectSerializer.serialize(MainActivity.places)).apply();

            /*
            * Recall we needed to convert latLang objects to string in order to store them in sharedpreferences as our shared preferences cannot
            * convert latlang to suitable string
             */
            sharedPreferences.edit().putString("lats",ObjectSerializer.serialize(latitudes));
            sharedPreferences.edit().putString("lons", ObjectSerializer.serialize(longitudes));
        }catch (Exception e){
            e.printStackTrace();
        }
        Toast.makeText(getApplicationContext(),"Location Saved",Toast.LENGTH_SHORT).show();
    }
}
