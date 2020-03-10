/*
 * make sure to add <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"></uses-permission> in
 * the manifest.xml file
 * To get the location and use it,
 * 1. we need location manager to get the location, and location listener to use it
 * 2. Afterwards we need to ask for permission (around line 206-207) if its above android 6, as from android 6 we need permission from user to use
 * gps and other stuff, and assign a request code that we will use in step 3 to ensure that this permission was for GPS, nothing else
 * 3. Then line 55, which gets triggered as soon as user grants permission to anything,
 * 4. Then  call this.locationManager.requestLocationUpdates() inside step 3 function to set up the location manager to send location updates to the app, and connect
 * it to the location listener
 * 5. Then finally use the location listener in line 107, here the function at line 111 is particularly interesting, as it gets fired any time the system sends update to the app
 * 6. To put a marker look at line 124
 * 7. If we wanna use geocoder to convert a location to human readable address, line 132
 *
 *
 */

package com.example.mapandlocationdemo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    LocationManager locationManager;
    LocationListener locationListener;

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
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                }
            }
        }
    }

    private GoogleMap mMap;

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

        // ideally we wanna fetch and update location after the map is ready, so it should come after the initialization of map
        this.locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        this.locationListener = new LocationListener() {
            // this function is fired whenever the location change is detected, so whenever locationManager.requestLocationUpdates
            // sends an update to the app, which again is determined by minTime and minDistance, and location info is recived to the location argument
            @Override
            public void onLocationChanged(Location location) {
                // Toast.makeText(MapsActivity.this, location.toString(), Toast.LENGTH_SHORT).show();

                // if we don't clear the map, every time the location changes we get a new marker, so there will be markers all over the place
                mMap.clear();

                // getting the coordinates
                // location here is the argument
                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                // adding marker to our location using the cordinates
                mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location"));
                // focusing the app to that marker and zooming in
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
                // geocoder turns a location to a meaningful human readable address
                // Locale.getdefault() means our app will make an address that is primarily used in in the user's counyry
                // for example, if the user is in Canada, the address format will be Canadian
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    // geocoder will retrieve a list of addresses, so we need a list, and 1 means we only want 1 matching result
                    List<Address> listAddresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                    // all other info we can fetch: https://developer.android.com/reference/android/location/Address.html
                    // https://stackoverflow.com/questions/22096011/what-does-each-androids-location-address-method-return
                    // ensure the list is not empty, and then do stuff
                    if (listAddresses != null && listAddresses.size() > 0) {
                        // use this logcat to see what key value pairs we have, for example
                        // for my home address, I get this
                        // admin=British Columbia,sub-admin=Greater Vancouver,locality=Vancouver,thoroughfare=West 38th Avenue,postalCode=V6M,countryCode=CA,countryName=Canada,
                        // so we can use, like getLocality, to get the city name and stuff like that
                        // Log.i("Place info:",listAddresses.get(0).toString());
                        String address = "";

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

                        // this gives us city name
                        if (listAddresses.get(0).getLocality() != null) {
                            address += listAddresses.get(0).getLocality() + " ";
                        }

                        // this gives us zip code
                        if (listAddresses.get(0).getPostalCode() != null) {
                            address += listAddresses.get(0).getPostalCode() + " ";
                        }

                        // this gives us state/province
                        if (listAddresses.get(0).getAdminArea() != null) {
                            address += listAddresses.get(0).getAdminArea() + " ";
                        }

                        Toast.makeText(MapsActivity.this, address, Toast.LENGTH_SHORT).show();


                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
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
            this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this.locationListener);
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
                this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this.locationListener);

                // we are doing it because when the app runs for very first time, the marker doesn't show up
                Location lastknownlocation = this.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                // if we don't clear the map, every time the location changes we get a new marker, so there will be markers all over the place
                mMap.clear();

                // getting the coordinates
                // location here is the argument
                LatLng userLocation = new LatLng(lastknownlocation.getLatitude(), lastknownlocation.getLongitude());
                // adding marker to our location using the cordinates
                mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location"));
                // focusing the app to that marker and zooming in
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
            }
        }


    }
}
