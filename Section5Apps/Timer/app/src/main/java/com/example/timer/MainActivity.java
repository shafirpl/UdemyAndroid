package com.example.timer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        * handler is one way to handle a timer function.
        * Another way is to use countdown timer, which is the preferred way
         */

        /*
        * This is similar to js timeout function
        * https://www.w3schools.com/jsref/met_win_settimeout.asp
        * So it is kinda weird, but we need a handler, and then
        * we need runnable which will have the run function
        * in which we define what needs to be repeated
        * Here we post a message after every second
         */

//        final Handler handler = new Handler();
//
//        Runnable run = new Runnable() {
//            @Override
//            public void run() {
//                Log.i("Something","A second passed");
//                handler.postDelayed(this,1000);
//            }
//        };
//        // this actually initializes the runnable thing
//        handler.post(run);

        /*
        * Another way to handle timer is to use countdown timer
         */

        // this mean we repeat something every 1s for 10s duration
        CountDownTimer timer = new CountDownTimer(10000,1000) {
            // this function will be called every second or every countDownInterval
            // we defined in the second argument of CountDownTimer function
            @Override
            public void onTick(long l) {
                // the argument it receives tell us how long left of the duration
                Log.i("Seconds left",String.valueOf(l/1000));
            }
            // this function is triggered when the timer is finished
            @Override
            public void onFinish() {
                Log.i("Done","Done");
            }
        };

        // now we need to start the timer
        timer.start();
    };
}
