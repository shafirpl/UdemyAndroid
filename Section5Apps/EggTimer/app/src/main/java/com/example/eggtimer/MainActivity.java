package com.example.eggtimer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView timerTextView;
    SeekBar timerSeekBar;
    Boolean counterIsActive = false;
    Button goButton;
    CountDownTimer countDownTimer;

    public void resetTimer(){
        timerTextView.setText("0:30");
        timerSeekBar.setProgress(30);
        timerSeekBar.setEnabled(true);
        countDownTimer.cancel();
        goButton.setText("GO!");
        counterIsActive = false;
    }

    public void buttonClicked(View view){

        if(counterIsActive){
            /*
            * if someone wants to stop the timer, this code will run
             */
            resetTimer();
        } else{
            /*
             * We don't want users to change the seekbar when the counter is going on
             * This code will freeze/disable the seekbar when we start the counter
             * As well as change the button text
             */
            counterIsActive = true;
            timerSeekBar.setEnabled(false);
            goButton.setText("STOP!");

            /*
             * https://www.udemy.com/course/the-complete-android-oreo-developer-course/learn/lecture/8339442#overview
             * Watch from 27:30 to see why we add 100
             */
                countDownTimer = new CountDownTimer(timerSeekBar.getProgress() * 1000 + 100,1000) {
                @Override
                // doing the countdown
                public void onTick(long l) {
                    int secondsLeft = (int) (l/1000);
                    updateTimer(secondsLeft);
                }

                @Override
                // playing the sound when the timer is finished
                public void onFinish() {
                    MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.sound);
                    mediaPlayer.start();
                    resetTimer();
                }
            };
            countDownTimer.start();

        }

    }

    public void updateTimer(int secondsLeft){
        int minutes = secondsLeft/60;
        int seconds = secondsLeft- (minutes*60);

        // this is to add an extra 0 in front of single digit number so that
        // they look like 08, 07 etc
        String secondString = Integer.toString(seconds);
        if (seconds <= 9){
            secondString = "0"+ secondString;
        }
        timerTextView.setText(Integer.toString(minutes)+ ":" + secondString);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerSeekBar = findViewById(R.id.timerSeekBar);
        timerTextView = findViewById(R.id.countdownTextView);
        goButton = findViewById(R.id.goButton);

        // setting the max value of timer seekbar to 10 minutes
        timerSeekBar.setMax(600);
        // setting up the initial timer to be 30 seconds
        timerSeekBar.setProgress(30);

        timerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                updateTimer(i);
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
