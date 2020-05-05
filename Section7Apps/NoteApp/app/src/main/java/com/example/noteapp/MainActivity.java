package com.example.noteapp;

/*
* Recall, to show a list of items like <ul> in html, we use list view, and in that we use array adapter.
* And in the array adapter, we use an arraylist, so arraylist contains the list of items we want to show, then array adapter
* to use that inside list view
 */

/*
 * To navigate to other page, we have to do these steps
 * 1. Create an intent
 * 2. Pass the intent to startActivity function
 * 3. Also https://www.udemy.com/course/the-complete-android-oreo-developer-course/learn/lecture/8339514?start=45#questions: watch
 * from 9:09
 * 4. If we wanna pass information from one screen to another, we do intent.putExtra()
 *
 */

/*
* https://www.udemy.com/course/the-complete-android-oreo-developer-course/learn/lecture/8339536#overview
* Watch from 9:39
* Instead of top, click clip_horizontal
* Check google doc's notes app section under Section 7
* Also make sure to make all the constraints match_constraint to make it responsive
* Check the notes app section for screenshots as well
* Also watch from 19:00 to see how to add menu (the three vertical dots on titlebar)
 */
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // Recall we need to make it static so that we can access this from other activities/screens
    static ArrayList <String> notes = new ArrayList<String>();
    static ArrayAdapter arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.listView);

        notes.add("Example Note");

        /*
         * So in order for something to be inside the list view, we need array adapter
         * Array adapter takes three things, the first thing is the context, which is the app itself
         * The second thing is what design/list view/layout it will use, here we are using a simple list view
         * Finally the thing which we want to show inside the listView, in this case the notes
         * Then we have to set the adapter to the thing that we want to show
         */
        this.arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,this.notes);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // this firs argument is the entire listview, even though it is called adapter view
                // the second argument is individual item in the array list
                // the third argument is the index number of that item
                // we don't work with long that much
                // Log.i("Person selected",myFamily.get(i));

                /*
                 * This function will create an intent,
                 * it takes the application context, the activity we wanna move to
                 * so for which we have to copy paste the class name of that activity
                 * Intent allows us to move from one screen to another screen
                 *
                 */
                Intent intent = new Intent(getApplicationContext(),NoteEditorActivity.class);

                // this is how we pass information to the other screen from here
                intent.putExtra("noteId",i);

                /*
                 * This function will actually do the navigation stuff
                 * it takes in the intent we created earlier
                 */
                startActivity(intent);
            }
        });

        // this function will run when we do a long press on the items
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to delete this note")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                return true;
            }
        });

    }

    // Menu code

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    // this function runs when we click any item on the menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.add_note){
            Intent intent = new Intent(getApplicationContext(), NoteEditorActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }
}
