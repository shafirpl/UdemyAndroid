package com.example.alertdemo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // creating the alertbox, rux this app to see how it will look like
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Are You Sure!")
                .setMessage("Do you definitely want to do this")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    // this function will run if the user clicks the yes or positive button
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(),"It is done", Toast.LENGTH_SHORT).show();
                    }
                })
                // null means we won't run any code if the user clicks the no/negative button
                .setNegativeButton("No",null)
                .show();
    }
}
