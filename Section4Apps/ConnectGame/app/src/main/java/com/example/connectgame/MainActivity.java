package com.example.connectgame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.gridlayout.widget.GridLayout;

/*
* for some reason, we need to import androidx.gridlayout.widget.GridLayout; otherwise the default import of grid layout
* would crash the app.
* Also for some reason the clickCounter needs to be set to 0 if the game has been won, otherwise the drawn condition is
* incorrect
 */


public class MainActivity extends AppCompatActivity {

    // 0: yellow, 1: Red
    int activePlayer = 0;
    int [] gameState = {2,2,2,2,2,2,2,2,2};
    boolean gameActive = true;

    int clickCounter = 0;

    /*
    * define the winning postion, for example, for diagonal we would need to have 0,4,8 cells matching a player
     */
    int [] [] winningPositions = {{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8}, {0,4,8}, {2,4,6}};
    /*
    * So basically when we click on the button, it immediately puts the image view outside the screen
    * by calling setTranslationX, then it sets the image, and animate it to go down
    *
    * Gamestate keeps track of the cells, there are in total nine cells. When a player gets a cell, for example,
    * yellow gets a cell, we just put that player number on the corresponding cell index. For example: if player yellow
    * gets the top left cell, the 0th index of gameState will be 0.
     */
    public void dropIn(View view){
        Button restartButton = (Button) findViewById(R.id.restart);
        TextView winnerTextView = (TextView) findViewById(R.id.winnerText);

        ImageView counter = (ImageView) view;
        int tappedCounter = Integer.parseInt(counter.getTag().toString());


        // make sure that after a cell has been occupied, clicking on that cell doesn't let other player occupy it
        // in other words, only allow the cell to be active if the value is 2, which means it is not occupied
        if(gameState[tappedCounter] == 2 && gameActive){
            gameState[tappedCounter] = activePlayer;
            clickCounter++;
            counter.setTranslationY(-1500);
            if(activePlayer == 0){
                counter.setImageResource(R.drawable.yellow);
                activePlayer = 1;
            }
            else{
                counter.setImageResource(R.drawable.red);
                activePlayer = 0;
            }

            counter.animate().translationYBy(1500).setDuration(300);

            /* This is a for each loop, we loop through each element of
             * of the winningPosition array, since that is an array of array, or each element
             * is an array itself, we will need an array to catch those elements
             */

            for (int[] winningPosition: winningPositions){
                /*
                 * So remember, winningPosition retrieves the array element, which is an array itself.
                 * So for the first item, winningPostion = [0,1,2]. Now we can retrieve the individual item
                 * by using the index. For example, winningPosition[0] = 0. Now here for the player to win, if
                 * all the winningPostion array elements have the same value, for example,
                 * player 1 occupied cell 0,1,2 then s/he
                 * will win. So we need to check if the values at those cells are the same.
                 * Also since the initial value is 2, we need to ensure the cells are not 2, because that is not a valid player value
                 * Now checking 1 cell for not 2 would suffice.
                 */

                if(gameState[winningPosition[0]] == gameState[winningPosition[1]] && gameState[winningPosition[1]]== gameState[winningPosition[2]]
                        && gameState[winningPosition[0]]!= 2){

                    gameActive = false;
                    String winner = "";
                    if(gameState[winningPosition[0]] == 0){
                        //Toast.makeText(this,"Yellow has won", Toast.LENGTH_SHORT).show();
                        winner = "Yellow";
                    }
                    else{
                        //Toast.makeText(this,"Red has won", Toast.LENGTH_SHORT).show();
                        winner = "Red";
                    }



                    winnerTextView.setText(winner+" has won");

                    restartButton.setVisibility(view.VISIBLE);

                    winnerTextView.setVisibility(view.VISIBLE);

                    clickCounter = 0;

                }

                // draw condition
                else if(clickCounter>=9){
                    winnerTextView.setText("Game is drawn");

                    restartButton.setVisibility(view.VISIBLE);

                    winnerTextView.setVisibility(view.VISIBLE);
                }
            }
        }

    }

    public void restart(View view){

        Button restartButton = (Button) findViewById(R.id.restart);
        TextView winnerTextView = (TextView) findViewById(R.id.winnerText);

        restartButton.setVisibility(view.INVISIBLE);

        winnerTextView.setVisibility(view.INVISIBLE);

        gameActive = true;
        clickCounter = 0;
        GridLayout gridLayout = (GridLayout) findViewById(R.id.gridLayout);

        for(int i=0; i<gameState.length; i++){
            gameState[i] = 2;

            ImageView child = (ImageView) gridLayout.getChildAt(i);
            child.setImageDrawable(null);


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
