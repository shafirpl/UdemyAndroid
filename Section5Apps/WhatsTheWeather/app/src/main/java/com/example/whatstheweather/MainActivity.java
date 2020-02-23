    /*
     * make sure the internet is on on the simulator
     * For any internet stuff to work, we need to config the mainfest file to
     * ask permission from the user to connect to the url
     * https://www.udemy.com/course/the-complete-android-oreo-developer-course/learn/lecture/8339458?start=0#questions
     * Watch from 21:24
     */
    // AsyncTask means all the tasks will be happening in background
    //

//* https://www.udemy.com/course/the-complete-android-oreo-developer-course/learn/lecture/8339458?start=0#questions

//* Watch from 4:40 to see what that string,void,string does

    package com.example.whatstheweather;

    import android.content.Context;
    import android.os.AsyncTask;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.View;
    import android.view.inputmethod.InputMethodManager;
    import android.widget.EditText;
    import android.widget.TextView;

    import org.json.JSONArray;
    import org.json.JSONObject;

    import java.io.InputStream;
    import java.io.InputStreamReader;
    import java.net.HttpURLConnection;
    import java.net.URL;

    import androidx.appcompat.app.AppCompatActivity;

    public class MainActivity extends AppCompatActivity {

        EditText editText;
        TextView resultTextView;

        public void getWeather(View view) {
            DownloadTask task = new DownloadTask();

            // sometimes if we have something like salt lake city or something where there are spaces, we might run
            // into an issue. In url, this is dealt by using %20 in between white spaces to tell the backend there is a
            // white space. For some reason our app automatically handles this, but if it didn't then we would do something like
            // this needs to be wrapped in a try catch block, so we would wrap the whole code in try block, and in the
            // catch block we would do e.printstacktree

            // String encodedCityName = URLEncoder.encode(editText.getText().toString(),"UTF-8");
            // getting the city name from the edti text
            String city = editText.getText().toString();
            task.execute("https://openweathermap.org/data/2.5/weather?q=" + city + "&appid=b6907d289e10d714a6e88b30761fae22");

            /*
             * https://www.udemy.com/course/the-complete-android-oreo-developer-course/learn/lecture/8339478?start=75#questions
             * Watch from 26:39, it talks about an issue of virtual keyboard covering up the weather data on our text view
             * so here we will make the virtual keyboard dissapear when the user hits the button
             */
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(editText.getWindowToken(), 0);

        }
        /*
         * Recall we need an async task to download something in the background/different thread
         */

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            editText = findViewById(R.id.editText);
            resultTextView = findViewById(R.id.resultTextView);


        }

        /*
         * When we pass something in excute(), it will be captured in
         * urls, so the first argument is urls[0], next is
         * urls[1] etc
         */
        public class DownloadTask extends AsyncTask<String, Void, String> {

            /*
             * doInBackground runs on a different thread which is not runnign the ui thread
             * to ensure it doesn't interfere/touch the ui
             * So in case of a failure/exception, the ui keeps running fine
             */
            @Override
            protected String doInBackground(String... urls) {
                String result = "";
                URL url;
                HttpURLConnection urlConnection;

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

            /*
             * This method however touches/interferes with the ui
             * The parameter is whatever the doInBackground returns
             * so s = result
             */

            /*
             * onPostExecute(Result), invoked on the UI thread after the background computation finishes.
             * The result of the background computation is passed to this step as a parameter.
             * collected from: https://developer.android.com/reference/android/os/AsyncTask
             * So this method will be run after doInBackground finishes, and the argument it will receive
             * is what the doInBackground returns. Since doInBackground returns result, the argument s = result
             */
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                // Log.i("JSON",s);
                /* since the result in this scenario is a json object, we could
                 * create a json object with the result.
                 * However, we need to surround this in a try catch block
                 */

                try {
                    JSONObject jsonObject = new JSONObject(s);

                    /*
                     * TO get a value, we use getString and pass in the key
                     * Copy paste the url we are using and see what keys we have available
                     * there is a key called weather inside the json object when we hit the url
                     */
                    String weatherInfo = jsonObject.getString("weather");
                    Log.i("Weather Content", weatherInfo);
                    /*
                     * Most of the time the key we will get will come as an array json object
                     * uncomment the log i to see how the array looks like
                     * So we need to use a json Array
                     * One sample is like this [{"id":721,"main":"Haze","description":"haze","icon":"50n"}]
                     */
                    JSONArray arr = new JSONArray(weatherInfo);

                    String message = "";

                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject jsonPart = arr.getJSONObject(i);

                        /*
                         * So from the sample, if we wanna grab the Haze part, we have to do getString("main") or
                         * getString(keyName), that will return the Haze part
                         * When this loop runs, in this case we only have one object in the array, so for this
                         * we will grab necessary info. But there might be more.
                         */
                        String main, description;
                        main = jsonPart.getString("main");
                        description = jsonPart.getString("description");
                        Log.i("main", jsonPart.getString("main"));
                        Log.i("Description", jsonPart.getString("description"));

                        // this is saying as long as main and description are not empty string, build the
                        // message string
                        if (!main.equals("") && !description.equals("")) {
                            // \r\n means create a new line
                            message += main + ": " + description + "\r\n";
                        }

                    }

                    if (!message.equals("")) {
                        resultTextView.setText(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }

            }
        }
    }
