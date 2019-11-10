package com.example.audio;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    // we need to initiate these before using them, otherwise we will get the app crashed.
    MediaPlayer mediaPlayer;
    AudioManager audioManager;
    public void play(View view){
        // this method got triggered when we click the play button, check the onClick method of the play button
        this.mediaPlayer.start();
    }

    public void pause(View view){
        // this method got triggered when we click the pause button, check the onClick method of the pause button
        this.mediaPlayer.pause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // for every seekbar, we need to set the max values. This is important

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        // min and max volume are a bit tricky as they vary from device to device, that is why we need
        // to set it first and then pass it
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        // the problem is, when the app starts, the slider is not set to the current volume, it is set to 0, we
        // don't want that, so we need to grab the current volume and then set the slider to that volume
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        // This will create the media player as soon as the app opens, so that the buttons can refer to it
        // the second argument is where we pass what sound it should be playing
        mediaPlayer = MediaPlayer.create(this,R.raw.sound);

        // this is the seekbar initialization
        SeekBar volumeControl = (SeekBar) findViewById(R.id.volumeSeekBar);

        volumeControl.setMax(maxVolume);

        // this will set the seekbar to current volume when we open our app instead of starting at 0
        volumeControl.setProgress(currentVolume);

        //setting up a listener on the seekbar so that we can change the sound
        volumeControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            // this will fire whenever we change the seekbar
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                // Log.i("Seekbar changed",Integer.toString(i));
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,i,0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }


        });
        // start the sound effect
        //mediaPlayer.start();

        // this seek bar is for seeking the playback
        final SeekBar scrubSeekBar = (SeekBar) findViewById(R.id.scrubSeekBar);
        scrubSeekBar.setMax(mediaPlayer.getDuration());

        scrubSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
                Log.i("ScrubBar changed",Integer.toString(i));
                // this will fast forward to the position the user is listening
                // The problem is, if we directly wrote seekTo without the if method, since the seekbar is being
                // changed by the timer as well, it will result in a jumpy/horrible playback. So we need to ensure
                // that we do this only when the user changes it, not when the system or the timer changes it
                if (fromUser){
                    mediaPlayer.seekTo(i);
                }
                //mediaPlayer.seekTo(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // we need to move the seekbar/progress bar as the sound plays through,
        // so we needed the timer, and update the seekbar every second, so this
        // timer will set the progress of seekbar every second
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                scrubSeekBar.setProgress(mediaPlayer.getCurrentPosition());
            }
        },0,300);
    }
}
