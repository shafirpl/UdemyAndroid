package com.example.listviewdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView myListView = findViewById(R.id.myListView);

        final ArrayList<String> myFamily = new ArrayList<String>();

        myFamily.add("Altair");
        myFamily.add("Ezio");
        myFamily.add("John");
        myFamily.add("Fido");
        /*
        * So in order for something to be inside the list view, we need array adapter
        * Array adapter takes three things, the first thing is the context, which is the app itself
        * The second thing is what design/list view/layout it will use, here we are using a simple list view
        * Finally the thing which we want to show inside the listView, in this case the family tree
        * Then we have to set the adapter to the thing that we want to show
         */
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, myFamily);
        myListView.setAdapter(arrayAdapter);

        /*
        * Here we are setting click listener for the list items
        * First we need to d
         */

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // this firs argument is the entire listview, even though it is called adapter view
                // the second argument is individual item, for example: john, altair etc
                // the third argument is the index number of the list, for example: for Altair it is 0
                // we don't work with long that much
                // Log.i("Person selected",myFamily.get(i));
                Toast.makeText(getApplicationContext(), "Hello " + myFamily.get(i),Toast.LENGTH_LONG).show();
            }
        });
    }
}
