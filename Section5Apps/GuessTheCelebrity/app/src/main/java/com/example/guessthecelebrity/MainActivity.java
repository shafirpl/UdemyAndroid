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
    /*
     * make sure the internet is on on the simulator
     * For any internet stuff to work, we need to config the mainfest file to
     * ask permission from the user to connect to the url
     * https://www.udemy.com/course/the-complete-android-oreo-developer-course/learn/lecture/8339458?start=0#questions
     * Watch from 21:24
     *
     * AsyncTask means all the tasks will be happening in background
     * Usually this asynctask is the standard way to download stuff
     * https://www.udemy.com/course/the-complete-android-oreo-developer-course/learn/lecture/8339458?start=0#questions
     * Watch from 4:40 to see what that string,void,string does
     *
     * https://www.udemy.com/course/the-complete-android-oreo-developer-course/learn/lecture/8339470?start=1020#questions
     * Watch from 26:00 that explains how the app works
     */
    package com.example.guessthecelebrity;

    import androidx.appcompat.app.AppCompatActivity;

    import android.graphics.Bitmap;
    import android.graphics.BitmapFactory;
    import android.os.AsyncTask;
    import android.os.Bundle;
    import android.os.Handler;
    import android.util.Log;
    import android.view.View;
    import android.widget.Button;
    import android.widget.ImageView;
    import android.widget.Toast;

    import java.io.InputStream;
    import java.io.InputStreamReader;
    import java.net.HttpURLConnection;
    import java.net.URL;
    import java.util.ArrayList;
    import java.util.Random;
    import java.util.regex.Matcher;
    import java.util.regex.Pattern;


    public class MainActivity extends AppCompatActivity {
        /*
         * When we pass something in excute(), it will be captured in
         * strings, so the first argument is strings[0], next is
         * strings[1] etc
         */
        ArrayList<String> celebURls = new ArrayList<String>();
        ArrayList<String> celebNames = new ArrayList<String>();
        int chosenCeleb = 0;
        ImageView imageView;
        String[] answers = new String[4];
        int locationOfCorrectAns = 0;
        Button button0;
        Button button1;
        Button button2;
        Button button3;

        // this function is triggered when any 1 of the 4 buttons are pressed
        /*
         * for this logic to work, in the activity_main.xml we had to set tag to each and every button
         * Here the view is the button that has been pressed, or the button that has been pressed will be passed as the
         * view argument here
         */
        public void celebChosen(View view) {
            Handler h = new Handler();
            /*
             * So tags are strings, even though we assigned 0,1,2,3 to them.
             * Here we are checking if the tapped/pressed button matches the set locationOfCorrectAns that
             * we set randomly
             */
            if (!view.getTag().toString().equals(Integer.toString(locationOfCorrectAns))) {
                Toast.makeText(getApplicationContext(), "Wrong! It was " + celebNames.get(chosenCeleb), Toast.LENGTH_SHORT).show();
            } else {
                /* recall toast takes three argument, the first one is the application context, where
                 * we just pass getApplicationContext() all the time, the second one is the text
                 * and finally the third one is the length, we usually use defined lenght in the Toast object
                 * Also make sure to include the show() method, otherwise it won't work
                 */
                Toast.makeText(getApplicationContext(), "Correct!", Toast.LENGTH_SHORT).show();
            }
            /*
            * I added this from stackOverflow
            * https://stackoverflow.com/questions/11548864/how-to-make-an-android-program-wait
            * This just ensures that we wait a few seconds and then display the new question
             */
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    newQuestion();
                }
            }, 2000);

        }

        public class DownloadTask extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... urls) {
                String result = "";
                URL url;
                HttpURLConnection urlConnection = null;

                /*
                 * URL needs to be surrounded by try catch, because if it cannot
                 * convert a string to url, it will throw an exception
                 */
                try {
                    // converting string to url
                    url = new URL(urls[0]);
                    // establishing connection to the url
                    urlConnection = (HttpURLConnection) url.openConnection();
                    // downloading the required data
                    InputStream in = urlConnection.getInputStream();
                    // reading the downloaded data
                    InputStreamReader reader = new InputStreamReader(in);

                    // it is like file input reader in c/python, it reads one character at a time
                    // and when it doesn't find any more data, it returns -1
                    // reader.read() gives int so we need to typecast it to read/get
                    // the actual data, then add it to result to get the full result

                    int data = reader.read();

                    while (data != -1) {
                        char current = (char) data;
                        result += current;
                        data = reader.read();
                    }
                    return result;

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }
        }

        public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
            @Override
            protected Bitmap doInBackground(String... urls) {
                try {
                    URL url = new URL(urls[0]);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                    InputStream inputStream = connection.getInputStream();
                    // this is the part where we decode the image and put it on the app
                    Bitmap myBitMap = BitmapFactory.decodeStream(inputStream);
                    return myBitMap;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }


        // this function creates the question and sets the answer
        public void newQuestion() {

            try {
                Random rand = new Random();
                chosenCeleb = rand.nextInt(celebURls.size());

                /*
                 * So Remember, when we do async task stuff, we need to first create an object of that class
                 * and then pass execute function with the stuff we need
                 */
                ImageDownloader imageTask = new ImageDownloader();
                Bitmap celebImage = imageTask.execute(celebURls.get(chosenCeleb)).get();

                /*
                 * We randomly chose a celebrity and set the image to be that celebrity, similarly we randomly chose a correct
                 * answer slot to make the player not predict the answers.
                 */

                imageView.setImageBitmap(celebImage);

                locationOfCorrectAns = rand.nextInt(4);

                int incorrectAnsLocation;

                /*
                 * This loop readies the answer array which we will use to set up the buttons
                 */

                for (int i = 0; i < 4; i++) {
                    if (i == locationOfCorrectAns) {
                        answers[i] = celebNames.get(chosenCeleb);
                    } else {
                        incorrectAnsLocation = rand.nextInt(celebURls.size());

                        /*
                         * The while loop ensures that the incorrect answer doesn't match the correct answer
                         * Let's say there are 20 celebrities, chosenCeleb is 3, then the incorrect answer should not be
                         * 3.
                         * This incorrectAns is a misnomer, what it is doing is that it is choosing
                         * a random location of size celebrity size that is not the correct answer, and then using that
                         * we are just pulling some random celebrity names
                         */
                        while (incorrectAnsLocation == chosenCeleb) {
                            incorrectAnsLocation = rand.nextInt(celebURls.size());
                        }

                        answers[i] = celebNames.get(incorrectAnsLocation);
                    }

                }
                // setting the buttons to answers
                button0.setText(answers[0]);
                button1.setText(answers[1]);
                button2.setText(answers[2]);
                button3.setText(answers[3]);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            // grabbing the imageview from the layout and setting it to imageView variable
            imageView = findViewById(R.id.imageView);

            // grabbing buttons from layout and setting it to appropriate variables
            button0 = findViewById(R.id.button0);
            button1 = findViewById(R.id.button1);
            button2 = findViewById(R.id.button2);
            button3 = findViewById(R.id.button3);

            DownloadTask task = new DownloadTask();
            String result = null;
            // we need to add get() in order to get the return
            // stuff, which in our case is string as we specified
            // in AsyncTask<String,Void,String> where third argument specifies
            // what we will be getting

            try {
                result = task.execute("http://www.posh24.se/kandisar").get();
                String[] splitResult = result.split("<div class=\"listedArticles\">");
                // go through the google doc on regex section to see what
                // this part is doing
                Pattern p = Pattern.compile("img src=\"(.*?)\"");
                Matcher m = p.matcher(splitResult[0]);

                while (m.find()) {
                    celebURls.add(m.group(1));
                }

                p = Pattern.compile("alt=\"(.*?)\"");
                m = p.matcher(splitResult[0]);

                while (m.find()) {
                    celebNames.add(m.group(1));
                }

                newQuestion();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
