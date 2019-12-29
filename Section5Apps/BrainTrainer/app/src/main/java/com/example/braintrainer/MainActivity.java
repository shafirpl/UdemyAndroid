/*
* https://www.udemy.com/course/the-complete-android-oreo-developer-course/learn/lecture/8339450?start=15#questions
* Watch the first 20 mins to see tha layout of the app
* Then
* Watch from 57:50 to see how we aligned the go button
* https://www.udemy.com/course/the-complete-android-oreo-developer-course/learn/lecture/8339450?start=15#overview
*
* I might have a question that how come we have one constraint layer inside another and how it hides
* the go button, usually if we don't set the go button invisibile, it appears on top of the second layer, recall that
* the go button and the second constraint layer are all inside the first layer, so in the first layer, both the go button and
* the second constraint layer exists. I can comment out line 114 to see the effect.
*
* The gameLayout starts as invisible, and then when we click the go button, the go button gets invisible and that layout
* becomes visible
*
* The game starts when we click on the go button, which calls the start method
 */
package com.example.braintrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import androidx.constraintlayout.widget.ConstraintLayout;

public class MainActivity extends AppCompatActivity {
    ConstraintLayout gameLayout;
    Button goButton;
    TextView sumTextView;
    int locationOfCorrectAnswer;
    TextView timerTextView;
    ArrayList<Integer> answers = new ArrayList<Integer>();
    TextView resultTextView;
    int score = 0;
    int numberOfQuestions = 0;
    TextView scoreTextView;

    Button button0;
    Button button1;
    Button button2;
    Button button3;

    long gameLength = 30100;
    long interval = 1000;

    Button playAgainButton;

    /*
    * This function either enables or disables the clicking
     */

    public void setClickable(boolean isClickable){
        if(!isClickable){
            this.button0.setClickable(false);
            this.button1.setClickable(false);
            this.button2.setClickable(false);
            this.button3.setClickable(false);
        }
        else{
            this.button0.setClickable(true);
            this.button1.setClickable(true);
            this.button2.setClickable(true);
            this.button3.setClickable(true);
        }
    }

    /*
    * This function restarts the game and is assigned to the play again button
     */

    public void playAgain(View view){

        this.playAgainButton.setVisibility(View.INVISIBLE);
        this.score = 0;
        this.numberOfQuestions = 0;
        this.timerTextView.setText("30s");

        // usually in android, we add 100milisecond extra, so for 30 seconds, it is 30000+ 100
        scoreTextView.setText(Integer.toString(score)+"/"+Integer.toString(numberOfQuestions));
        newQuestion();
        /*
        * I had a bug if i call setClickable before calling new question, the app would crash, which makes sense because
        * at this stage the buttons are still not initialized. The buttons get initialized when I call the newQuestion() method.
         */
        setClickable(true);


        resultTextView.setText("");
        CountDownTimer timer =  new CountDownTimer(this.gameLength, this.interval) {
            @Override
            public void onTick(long l) {
                long timeRemaining = l/1000;
                timerTextView.setText(Long.toString(timeRemaining)+"s");
            }

            @Override
            public void onFinish() {
                resultTextView.setText("Done!");
                playAgainButton.setVisibility(View.VISIBLE);
                setClickable(false);
            }
        }.start();


    }

    /*
    * This function starts the game and assigned to the big go button that appears when
    * we open up the app for very first time
     */
    public void start(View view){
        goButton.setVisibility(View.INVISIBLE);
        this.gameLayout.setVisibility(View.VISIBLE);
        // here it doesn't really matter what view we are passing in as we don't use that view
        // so we just pass a dummy random view, we need to do it as we defined the function like that
        // so it expects a view
        playAgain(findViewById(R.id.timerTextView));
    }

    /*
    * This method is triggered when we click one of the 4 answer buttons and contains the logic to see
    * whether the button pressed had correct answer or not, and then increments the score. It is assigned to the 4 number boxes in
    * the app
     */
    public void chooseAnswer(View view){

        // recall we assign tags to button, and then use getTag to get which button was pressed if
        // we have multiple buttons have the same function.
        Log.i("Button Pressed",view.getTag().toString());


        if(Integer.toString(locationOfCorrectAnswer).equals(view.getTag().toString())){
            resultTextView.setText("Correct!");
            score++;
        } else{
            resultTextView.setText("Wrong :(");
        }
        numberOfQuestions++;
        scoreTextView.setText(Integer.toString(score)+"/"+Integer.toString(numberOfQuestions));
        newQuestion();


    }

    /*
    * This creates a new question
    *
     */

    public void newQuestion(){
        Random rand = new Random();

        // this means a number between 0 and 20
        int a = rand.nextInt(21);

        int b = rand.nextInt(21);

        this.locationOfCorrectAnswer = rand.nextInt(4);

        this.sumTextView.setText(Integer.toString(a)+ " + "+ Integer.toString(b));

        // initializing the answer buttons

        this.button0 = findViewById(R.id.button0);
        this.button1 = findViewById(R.id.button1);
        this.button2 = findViewById(R.id.button2);
        this.button3 = findViewById(R.id.button3);

        /*
         * This loop sets the texts on the buttons
         */

        // we need to clear the array before running the loop, otherwise we would have residuals
        // from previous calling to this function

        answers.clear();

        for(int i=0; i<4; i++){
            if (i == locationOfCorrectAnswer) {
                answers.add(a+b);
            } else{
                /* the while loop ensures that the wrong answer, due to randomness, is never picked the right answer
                 * since we are dealing with random numbers, there might be case that the wrong answer and the correct answer
                 * maybe the same, we don't want that
                 */
                int wrongAnswer = rand.nextInt(41);
                while(wrongAnswer == a+b){
                    wrongAnswer = rand.nextInt(41);
                }
                answers.add(wrongAnswer);
            }

        }

        button0.setText(Integer.toString(answers.get(0)));
        button1.setText(Integer.toString(answers.get(1)));
        button2.setText(Integer.toString(answers.get(2)));
        button3.setText(Integer.toString(answers.get(3)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.gameLayout = findViewById(R.id.gameLayout);

        this.playAgainButton = findViewById(R.id.playAgainButton);

        this.goButton = findViewById(R.id.goButton);

        this.sumTextView = findViewById(R.id.sumTextView);

        this.resultTextView = findViewById(R.id.resultTextView);

        this.scoreTextView = findViewById(R.id.scoreTextView);

        this.timerTextView = findViewById(R.id.timerTextView);

        this.goButton.setVisibility(View.VISIBLE);

        this.gameLayout.setVisibility(View.INVISIBLE);



    }
}
