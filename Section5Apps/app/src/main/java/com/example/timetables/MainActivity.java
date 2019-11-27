package com.example.timetables;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView timesTablesListView;

    public void generateTimesTables(int timesTablesNumber){
        ArrayList<String> timesTableContent = new ArrayList<String>();

        for (int j = 1; j<= 100; j++){
            timesTableContent.add(Integer.toString(j * timesTablesNumber));
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,timesTableContent);

        timesTablesListView.setAdapter(arrayAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        * when we open the app, it doesn't show anything until we start moving the slider
        * to fix this, we need to call the generateTimesTables first when the app is
        * created, which is this function
         */



        final SeekBar timesTablesSeekBar = findViewById(R.id.timesTablesSeekBar);
        timesTablesListView = findViewById(R.id.timesTablesListView);

        int max = 20;

        int startingPosition = 10;


        timesTablesSeekBar.setMax(max);
        generateTimesTables(startingPosition);
        // this means the seek bar when the app is loaded, will start
        // at 10
        timesTablesSeekBar.setProgress(10);

        timesTablesSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int min = 1;
                // i define the current position of the seekbar
                // since we set the max value to 20, i can go from 0 to 20

                int timesTablesNumber;

                /*
                * There is no point to make a time table for 0, so if i is 0, we change the time table value to be 1
                 */
                if (i<min){
                    timesTablesNumber = min;
                    // this is to not allow the user to place the seekbar
                    // at the very left of the screen, the '
                    // instructor did that but i kinda felt it is
                    // unnecessary
                    timesTablesSeekBar.setProgress(min);
                } else{
                    timesTablesNumber = i;
                }

                Log.i("Seekbar Value",Integer.toString(timesTablesNumber));

                generateTimesTables(timesTablesNumber);

                // this part deals with generating the time table and put it into
                // the list view

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }



}
