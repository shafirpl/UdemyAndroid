package com.example.loginexercise;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void logData(View view){
        // setting up the variables to grab data
        EditText userName = (EditText) findViewById(R.id.userName);
        EditText userPassword = (EditText) findViewById(R.id.password);

        // logging the data
        Log.i("UserName","Username is: "+ userName.getText().toString());
        Log.i("Password","Password is: "+ userPassword.getText().toString());

    }
}
