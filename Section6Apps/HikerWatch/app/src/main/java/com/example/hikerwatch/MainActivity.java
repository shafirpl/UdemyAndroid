/*
 * Look at android manifest.xml file to see how i make it full screen app
 * https://www.udemy.com/course/the-complete-android-oreo-developer-course/learn/lecture/8339510#questions
 * Watch from 4:12 to see the layout stuff
 * Also watch from 23:42 to see how we added new line to our address stuff
 *
 * Remember: To get the location and use it, we need to:
 * 1. we need location manager to get the location, and location listener to use it
 * 2. Afterwards we need to ask for permission (around line 80) if its above android 6, as from android 6 we need permission from user to use
 * gps and other stuff, and assign a request code that we will use in step 3 to ensure that this permission was for GPS, nothing else
 * 3. Then line 122, which gets triggered as soon as user grants permission to anything,
 * 4. Then  call this.locationManager.requestLocationUpdates() at line 141 to set up the location manager to send location updates to the app, and connect
 * it to the location listener
 * 5. Then finally use the location listener in line 53, here the function at line 57 is particularly interesting, as it gets fired any time the system sends update to the app
 * 6. Use geocoder to convert a location to human readable address, line 171
 */

package com.example.hikerwatch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        this.locationListener = new LocationListener() {
            // this function is fired whenever the location change is detected, so whenever locationManager.requestLocationUpdates
            // sends an update to the app, which again is determined by minTime and minDistance, and location info is recived to the location argument
            @Override
            public void onLocationChanged(Location location) {
                updateLocationInfo(location);
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
        /*
         * here we are seeing that if we didn't get permission, we need to ask for it
         */

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            /*
             * here we are asking for permission if permission is not granted
             * 1 means we are assigning code 1 to this request, it could be any number, if permission is granted, then
             * onRequestPermissionsResult will run and receive requestCode 1, which signifies that function was run
             * as the user gave permission to access fine location
             */

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            // this else part is if the user already gave us the permission, which will happen when the app runs for first time and user gives permission

            /*
             * https://developer.android.com/reference/android/location/LocationManager#requestLocationUpdates(java.lang.String,%20long,%20float,%20android.location.LocationListener)
             * so providing 0 for min time means update all the time, if we provide let's say 100, it will only send your application an update after every
             * 100 ms
             * also the location provider will only send your application an update when the location has changed by at least minDistance meters, 0 means it will send
             * update whenever a location change is detected
             * So location manager gets the location and passes it to location listener, location listener does all other stuff like
             * Logging the info etc
             */
            this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this.locationListener);
            // we are doing it because when the app runs for very first time, the location doesn't get updated
            Location lastKnownLocation = this.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (lastKnownLocation != null) {
                updateLocationInfo(lastKnownLocation);
            }


        }
    }

    // this function finds if the user said yes or no to our permission question, and do the necessary stuff
    /*
     * recall we assigned request code 1 to our permission question,
     * so if the user allows permission to our gps question, this function will get triggered and
     * the code it will receive will be 1
     * we might have different permission such as bluetooth permission question and stuff like that, so if we assign 2
     * to bluetooth and user gives permission, this function will get triggered with code 2
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            this.startListening();
        }
    }

    public void startListening(){
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
            this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this.locationListener);
        }
    }
    /*
    * This function basically updates all the text views
     */
    public void updateLocationInfo(Location location) {
        // getting all the text views
        TextView latTextView = findViewById(R.id.latTextView);
        TextView longTextView = findViewById(R.id.longTextView);
        TextView accuracyTextView = findViewById(R.id.accuracyTextView);
        TextView addressTextView = findViewById(R.id.addressTextView);
        TextView altitudeTextView = findViewById(R.id.altTextView);

        // setting strings
        String latitude = String.format("%.3f",location.getLatitude());
        String longitude = String.format("%.3f",location.getLongitude());
        String altitude = String.format("%.3f",location.getAltitude());

        // setting all the text views
        latTextView.setText("Latitude: "+latitude);
        longTextView.setText("Longitude: "+ longitude);
        altitudeTextView.setText("Altitude: "+altitude);
        accuracyTextView.setText("Accuracy: "+location.getAccuracy());

        // geocoder string setup
        String address = "Could not find address :(";

        // geocode turns a location to a meaningful human readable address
        // Locale.getdefault() means our app will make an address that is primarily used in in the user's counyry
        // for example, if the user is in Canada, the address format will be Canadian
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            // geocoder will retrieve a list of addresses, so we need a list, and 1 means we only want 1 matching result
            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

            if(addressList != null && addressList.size()> 0){
                address = "Address:\n";

                // all other info we can fetch: https://developer.android.com/reference/android/location/Address.html
                // https://stackoverflow.com/questions/22096011/what-does-each-androids-location-address-method-return
                // ensure the list is not empty, and then do stuff

                // this gives us street number such as 2180
                // we do the null check to ensure we have something to parse
                if(addressList.get(0).getSubThoroughfare() != null){
                    address += addressList.get(0).getSubThoroughfare() + " ";
                }

                // this gives us street name such as West 38th Avenue
                // we do the null check to ensure we have something to parse
                if(addressList.get(0).getThoroughfare() != null){
                    address += addressList.get(0).getThoroughfare() + "\n";
                }

                // this gives us city name
                if(addressList.get(0).getLocality() != null){
                    address += addressList.get(0).getLocality() + ", ";
                }


                // this gives us state/province
                if(addressList.get(0).getAdminArea() != null){
                    address += addressList.get(0).getAdminArea() + ", ";
                }

                if(addressList.get(0).getCountryName() != null){
                    address += addressList.get(0).getCountryName()+ " ";
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        addressTextView.setText(address);

    }
}
