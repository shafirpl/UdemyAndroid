package com.example.locationdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

// make sure to add <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"></uses-permission> in
// the manifest.xml file

// udemy.com/course/the-complete-android-oreo-developer-course/learn/lecture/8339498?start=285#overview
/*
* We alsoneed to set our emulator to the appropriate location
* 16:09 from that video shows how to do that
 */
public class MainActivity extends AppCompatActivity {
    LocationManager locationManager;
    LocationListener locationListener;


    // this function finds if the user said yes or no to our permission question, and do the necessary stuff
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // here we provided fine location as this is what we asked for in manifest.xml file

        if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            /*
             * here we are seeing that we have the permission, and then do all the stuff
             */
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION )== PackageManager.PERMISSION_GRANTED) {
                /*
                 * https://developer.android.com/reference/android/location/LocationManager#requestLocationUpdates(java.lang.String,%20long,%20float,%20android.location.LocationListener)
                 * so providing 0 for min time means update all the time, if we provide let's say 100, it will only send your application an update after every
                 * 100 ms
                 * also the location provider will only send your application an update when the location has changed by at least minDistance meters, 0 means it will send
                 * update whenever a location change is detected
                 * So location manager gets the location and passes it to location listener, location listener does all other stuff like
                 * Logging the info etc
                 */
                this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this.locationListener);
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        this.locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("Location",location.toString());
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

        // we need to actually write code to get permission apart from manifest.xml file
        // here we provided fine location as this is what we asked for in manifest.xml file
        /*
        * here we are seeing that if we didn't get permission, we need to ask for it
         */
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION )!= PackageManager.PERMISSION_GRANTED){
            // here we are asking for permission if permission is not granted
            // 1 means we are asking for one permission only
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        // this else part is if the user already gave us the permission, which will happen when the app runs for first time and user gives permission
        else {
            this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this.locationListener);
        }
    }
}
