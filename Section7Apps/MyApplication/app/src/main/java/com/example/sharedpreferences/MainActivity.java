/*
* sharedpreference is a way to store values in the app memory, however it has lot of limitation
* one limitation is that it cannot take array, so we need to use an seriliazer to convert array to string
* and then store it, and then when we retrieve it, we need to use deserilizer to convert back to array from string
* Now how the ObjectSerilizer works wasn't explained, so assume it is a library (we copy pasted the code)
 */
package com.example.sharedpreferences;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        * when we are initializing it, we need two parameters
        * Sharedpreference allow us to save app data permanently
        * the first parameter is the name of the app, which we copy from the top of the file, where it says package app_name
        * the second parameter is the mode we wanna save
         */
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.sharedpreferences", Context.MODE_PRIVATE);

        /*
        * Here we are saving a string
        * first we need to give the string a name
        * Then we have to provide the actual thing we are storing
         */
        sharedPreferences.edit().putString("username","Hello").apply();

        /*
        * now we are fetching the username
        * The first argument is the name of the string we are looking for
        * second is the default value in case we cannot find the string
         */
        String userName = sharedPreferences.getString("username", "");

        Log.i("Username",userName);

        ArrayList<String> friends = new ArrayList<>();

        friends.add("Fido");
        friends.add("Jones");
        /*
        * Since we cannot store an array in shared preference, we are using seriliazation to convert it to string
        * and later when we retrieve it, we need to convert it back to an array
        * The objectSerializer class was copy pasted and for the time being assume it is for granted/library function
        * that someone wrote it for us
         */
        try {
            sharedPreferences.edit().putString("friends",ObjectSerializer.serialize(friends)).apply();
            Log.i("friends",ObjectSerializer.serialize(friends));
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*
        * here we are deserlizing the string we would get back from the sharedpreference
        * In case we cannot find the sharedpreference, the deserialize is expecting a string to convert it to an array, and that string has particular structure
        * so passing an empty string will break it if we cannot find the sharedpreference,
        * that is why we are passing another object serilizer to convert an empty arraylist to a sutiable string that wouldn't break the deserializer
         */
        ArrayList<String> newFriends = new ArrayList<>();
        try {
            newFriends = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("friends",ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.i("new friends", newFriends.toString());

        //sharedPreferences.edit().put
    }
}
