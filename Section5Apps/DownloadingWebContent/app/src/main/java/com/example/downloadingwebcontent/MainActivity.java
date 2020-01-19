package com.example.downloadingwebcontent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    /*
    * make sure the internet is on on the simulator
    * For any internet stuff to work, we need to config the mainfest file to
    * ask permission from the user to connect to the url
    * https://www.udemy.com/course/the-complete-android-oreo-developer-course/learn/lecture/8339458?start=0#questions
    * Watch from 21:24
     */
    // AsyncTask means all the tasks will be happening in background
    /*
    * https://www.udemy.com/course/the-complete-android-oreo-developer-course/learn/lecture/8339458?start=0#questions
    * Watch from 4:40 to see what that string,void,string does
     */
    public class DownloadTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            /*
            * When we pass something in excute(), it will be captured in
            * strings, so the first argument is strings[0], next is
            * strings[1] etc
             */
            // Log.i("URL:",strings[0]);
            String result = "";
            HttpsURLConnection urlConnection = null;


            /*
            * URL needs to be surrounded by try catch, because if it cannot
            * convert a string to url, it will throw an exception
             */
            try{
                // converting string to url
                URL url = new URL(strings[0]);
                // establishing connection to the url
                urlConnection = (HttpsURLConnection) url.openConnection();
                // downloading the required data
                InputStream in = urlConnection.getInputStream();
                // reading the downloaded data
                InputStreamReader reader = new InputStreamReader(in);
                // it is like file input reader in c/python, it reads one character at a time
                // and when it doesn't find any more data, it returns -1
                // reader.read() gives int so we need to typecast it to read/get
                // the actual data, then add it to result to get the full result
                int data = reader.read();

                while(data != -1){
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return  result;
            } catch(Exception e){
                e.printStackTrace();
                return "Failed";
            }


            //return "Done";
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DownloadTask task = new DownloadTask();
        String result = null;

        try {
            // we need to add get() in order to get the return
            // stuff, which in our case is string as we specified
            // in AsyncTask<String,Void,String> where thrid arugment specifies
            // what we will be getting
            result = task.execute("https://www.shafirpl.com/").get();
        } catch (Exception e){
            e.printStackTrace();
        }

        Log.i("Result",result);


    }
}
