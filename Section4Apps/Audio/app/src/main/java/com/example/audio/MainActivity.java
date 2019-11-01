package com.example.audio;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer;
    public void play(View view){

        this.mediaPlayer.start();
    }

    public void pause(View view){
        this.mediaPlayer.pause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // This will create the media player as soon as the app opens, so that the buttons can refer to it
        mediaPlayer = MediaPlayer.create(this,R.raw.sound);


        // start the sound effect
        //mediaPlayer.start();
    }
}
