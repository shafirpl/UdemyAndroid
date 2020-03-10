package com.example.friendlistmultiactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.listView);

        final ArrayList<String> friends = new ArrayList<String>();

        friends.add("Fido");
        friends.add("Shafi");
        friends.add("Hello");
        friends.add("Hi");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,friends);
        listView.setAdapter(arrayAdapter);

        // this will create a click listener, which will get activated when we click on the individual list item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                /*
                * Recall that in order to switch to next screen we need a intent, and if we wanna
                * pass info there, we use putExtra
                * now the argument i in onItemClick will give us the item index in the array
                * on which we clicked on, so for example, if we click on Fido, we will get i = 0
                 */
                Intent intent = new Intent(getApplicationContext(),SecondActivity.class);
                intent.putExtra("name",friends.get(i));
                startActivity(intent);
            }
        });
    }
}
