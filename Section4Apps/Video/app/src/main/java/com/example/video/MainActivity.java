package com.example.video;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        VideoView videoView = (VideoView) findViewById(R.id.videoView);
        //setting the path
        videoView.setVideoPath("android.resource://"+getPackageName()+"/"+R.raw.demovideo);
        videoView.start();

        // we have to have a media controller if we want to give users a controller
        // when we initialize a media controller, we need to define a context, which is this as usual
        MediaController mediaController = new MediaController(this);

        // here we are setting the media controller attached to the video
        mediaController.setAnchorView(videoView);

        // then here we are allowing the media controller to control the video
        videoView.setMediaController(mediaController);
    }
}
