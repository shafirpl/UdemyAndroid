package com.example.basicphrases;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.gridlayout.widget.GridLayout;

public class MainActivity extends AppCompatActivity {

    public void playPhrase(View view){
        /*
        * So the way it works is that, we created the buttons and gave them a tag matching the appropriate filename that
        * should be played. For example: the hello button has the text hello, and the tag hello, and it should play the
        * hello.m4a file. So we wanted to only have one method to play all the sounds. The way we do that is we grab which
        * button was pressed first, get the tag name from the button, create a media player to play the sound using the file
        * that we grab using the tag name (which is a bit more complex as described below), and then using mediaPlayer.start
        * play the audio.
         */
        Button buttonPressed = (Button) view;
        Log.i("Button Pressed:",buttonPressed.getTag().toString());
        // this line is kinda weird, as we cannot just use R.raw.buttonPressed.getTag() or something like that
        // it wants a constant name. SO in order to get around, we found a stack overflow post that suggested something
        // weird like this to use the variable name, as recall we mapped our buttonPressed tags with the file name
        // so when we click the button, it will fetch the sound filename matching its tag
        MediaPlayer mediaPlayer = MediaPlayer.create(this,getResources().getIdentifier(buttonPressed.getTag().toString(),"raw",getPackageName()));

        mediaPlayer.start();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
