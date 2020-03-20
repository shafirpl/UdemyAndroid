/*
* For seeing how I added side menu,
* https://www.udemy.com/course/the-complete-android-oreo-developer-course/learn/lecture/8339532#questions/9634904
* Watch from 20:45
 */
package com.example.languagepreferences;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView languageTextView;
    SharedPreferences sharedPreferences;

    public void setLanguage (String language){


        // storing the language preference
        /*
        * This takes two string as well, the first one is the name, and then the actual string we wanna save
         */
        this.sharedPreferences.edit().putString("language", language).apply();

        this.languageTextView.setText(language);

    }
/*
* This function creates and initliazes the side menu
 */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /*
    * This function gets triggered whenever someone chooses an option from the menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        /*
        * Remember we set english and spanish as our items id, so if the user selects english from the
        * side menu, the case R.id.english will run and vice versa
         */
        switch (item.getItemId()){
            case R.id.english:
                this.setLanguage("English");
                return true;
            case R.id.spanish:
                this.setLanguage("Spanish");
                return true;
            default:return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
         * Recall we use sharedPreference to store data permanently
         * When we are initializing it, we need to pass the application name which we grab from the very top of this file
         * and then the mode
         */
        this.sharedPreferences = this.getSharedPreferences("com.example.languagepreferences", Context.MODE_PRIVATE);
        this.languageTextView = findViewById(R.id.languageTextView);
        /*
        * Retreiving the saved shared preference
        * recall it takes 2 arguments, the first one is the name of the string we are trying to retrieve
        * the second one is if we cannot retrieve the string, use a default placeholde
         */
        String language = this.sharedPreferences.getString("language","Error");

        if(language.equals("Error")){
            // creating the alertbox, rux this app to see how it will look like
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_btn_speak_now)
                    .setTitle("Choose A Language")
                    .setMessage("Which language do you like to use")
                    .setPositiveButton("English", new DialogInterface.OnClickListener() {
                        // this function will run if the user clicks the English or positive button
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(getApplicationContext(),"You chose English", Toast.LENGTH_SHORT).show();
                            setLanguage("English");
                        }
                    })
                    // this function will run if we click the spanish/negative button
                    .setNegativeButton("Spanish", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(getApplicationContext(),"You chose Spanish", Toast.LENGTH_SHORT).show();
                            // set spanish
                            setLanguage("Spanish");
                        }
                    })
                    .show();
        } else{
            this.languageTextView.setText(language);
        }


    }
}
