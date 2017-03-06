package com.example.crookdebo.guessinggame;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private int gameRange, interval;
    public static final String prefsName = "Prefs_file";

    private int pageNum;
    private boolean onTempPage = false;
    private int numAns, playerNum;
    private int colAnsRed, colAnsGreen, colAnsBlue;
    private int playerRed, playerGreen, playerBlue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final SharedPreferences settings = getSharedPreferences(prefsName,0);
        pageNum = settings.getInt("pageNum",0);
        colAnsRed = settings.getInt("colAnsRed",-1);
        colAnsBlue = settings.getInt("colAnsBlue",-1);
        colAnsGreen = settings.getInt("colAnsGreen",-1);

        playerRed = settings.getInt("playerRed",0);
        playerBlue = settings.getInt("playerBlue",0);
        playerGreen = settings.getInt("playerGreen",0);

        numAns = settings.getInt("numAns",-1);
        playerNum = settings.getInt("playerNum",-1);

        interval = settings.getInt("interval",60);
        gameRange = settings.getInt("gameRange",100);

        Button playNumber = (Button)findViewById(R.id.playNumber);
        Button playColour = (Button)findViewById(R.id.playColour);

        playNumber.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                startNumberGame();
            }
        });

        playColour.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                startColourGame();
            }
        });

        if(pageNum == 1) startNumberGame();
        else if (pageNum == 2) startColourGame();
    }

    protected void startSplashPage(){
        setContentView(R.layout.activity_main);
        pageNum = 0;

        Button playNumber = (Button) findViewById(R.id.playNumber);
        Button playColour = (Button) findViewById(R.id.playColour);

        playNumber.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startNumberGame();
            }
        });

        playColour.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startColourGame();
            }
        });
    }

    protected void startColourGame( ){
        setContentView(R.layout.colourgame);
        pageNum = 2;

        if(colAnsRed == -1) colAnsRed =round((int)(Math.random()*256));
        if(colAnsGreen == -1) colAnsGreen =round((int)(Math.random()*256));
        if(colAnsBlue == -1) colAnsBlue =round((int)(Math.random()*256));

        int correctColour = Color.rgb(colAnsRed, colAnsGreen, colAnsBlue);
        final View ansBox = findViewById(R.id.colourWinBox);
        ansBox.setBackgroundColor(correctColour);

        final SeekBar redBar = (SeekBar)findViewById(R.id.redBar);
        final SeekBar greenBar = (SeekBar)findViewById(R.id.greenBar);
        final SeekBar blueBar = (SeekBar)findViewById(R.id.blueBar);

        final EditText redTextBox = (EditText)findViewById(R.id.redNum);
        final EditText greenTextBox = (EditText)findViewById(R.id.greenNum);
        final EditText blueTextBox = (EditText)findViewById(R.id.blueNum);

        Button resetBtn = (Button)findViewById(R.id.reset2);
        Button goBackBtn = (Button)findViewById(R.id.goBackColour);

        final View guessBox = findViewById(R.id.guessColour);

        playerRed = round(playerRed);
        playerGreen = round(playerGreen);
        playerBlue = round(playerBlue);
        redTextBox.setText(String.valueOf(playerRed));
        greenTextBox.setText(String.valueOf(playerGreen));
        blueTextBox.setText(String.valueOf(playerBlue));
        redBar.setProgress(playerRed);
        greenBar.setProgress(playerGreen);
        blueBar.setProgress(playerBlue);

        guessBox.setBackgroundColor(Color.rgb(playerRed,playerGreen,playerBlue));

        //this part handles the slider bars which the player can use to adjust their guessed colour
        redBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress != round(progress)){
                    progress = round(progress);
                    redBar.setProgress(progress);
                }
                redTextBox.setText(String.valueOf(progress));
                checkColour(redTextBox,greenTextBox,blueTextBox,guessBox);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        greenBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress != round(progress)){
                    progress = round(progress);
                    greenBar.setProgress(progress);
                }
                greenTextBox.setText(String.valueOf(progress));
                checkColour(redTextBox,greenTextBox,blueTextBox,guessBox);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        blueBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress != round(progress)){
                    progress = round(progress);
                    blueBar.setProgress(progress);
                }
                blueTextBox.setText(String.valueOf(progress));
                checkColour(redTextBox,greenTextBox,blueTextBox,guessBox);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        //the reset button starts a new game with a random colour
        resetBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                colAnsRed = round((int)(Math.random()*256));
                colAnsGreen = round((int)(Math.random()*256));
                colAnsBlue = round((int)(Math.random()*256));

                int correctColour = Color.rgb(colAnsRed, colAnsGreen, colAnsBlue);
                ansBox.setBackgroundColor(correctColour);

                redBar.setProgress(0);
                greenBar.setProgress(0);
                blueBar.setProgress(0);

                redTextBox.setText("0");
                greenTextBox.setText("0");
                blueTextBox.setText("0");

                guessBox.setBackgroundColor(Color.rgb(0,0,0));
            }
        });

        //the return button goes back to the splash page, saving the current data
        goBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(redTextBox.getText().toString().equals("")) playerRed = 0;
                else playerRed = Integer.parseInt(redTextBox.getText().toString());

                if(greenTextBox.getText().toString().equals("")) playerGreen = 0;
                else playerGreen = Integer.parseInt(greenTextBox.getText().toString());

                if(blueTextBox.getText().toString().equals("")) playerBlue = 0;
                else playerBlue = Integer.parseInt(blueTextBox.getText().toString());

                startSplashPage();
            }
        });

        //this part handles the up/down buttons which adjust the values by +/-interval
        Button redDown = (Button)findViewById(R.id.redDown);
        Button greenDown = (Button)findViewById(R.id.greenDown);
        Button blueDown = (Button)findViewById(R.id.blueDown);

        Button redUp = (Button)findViewById(R.id.redUp);
        Button greenUp = (Button)findViewById(R.id.greenUp);
        Button blueUp = (Button)findViewById(R.id.blueUp);

        redDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentProgress = redBar.getProgress();
                if(currentProgress >= interval) {
                    redBar.setProgress(currentProgress - interval);
                }
            }
        });

        greenDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentProgress = greenBar.getProgress();
                if(currentProgress >= interval) {
                    greenBar.setProgress(currentProgress - interval);
                }
            }
        });

        blueDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentProgress = blueBar.getProgress();
                if(currentProgress >= interval) {
                    blueBar.setProgress(currentProgress - interval);
                }
            }
        });

        redUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentProgress = redBar.getProgress();
                int newProgress = currentProgress + interval;
                if(newProgress > 255) newProgress = 255;
                redBar.setProgress(newProgress);
            }
        });

        greenUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentProgress = greenBar.getProgress();
                int newProgress = currentProgress + interval;
                if(newProgress > 255) newProgress = 255;
                greenBar.setProgress(newProgress);
            }
        });

        blueUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentProgress = blueBar.getProgress();
                int newProgress = currentProgress + interval;
                if(newProgress > 255) newProgress = 255;
                blueBar.setProgress(newProgress);
            }
        });

        //this part handles the player entering numbers directly into the edittext boxes
        redTextBox.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
                boolean handled = false;
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    String redText = redTextBox.getText().toString();
                    int redNum;
                    if (!redText.equals("")) redNum = Integer.parseInt(redTextBox.getText().toString());
                    else{
                        redNum = 0;
                        redTextBox.setText("0");
                    }
                    if(redNum > 255) redNum = 255;

                    redBar.setProgress(round(redNum));
                    redTextBox.setText(String.valueOf(round(redNum)));
                    handled = true;
                }
                return handled;
            }
        });

        greenTextBox.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
                boolean handled = false;
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    String greenText = greenTextBox.getText().toString();
                    int greenNum;
                    if (!greenText.equals("")) greenNum = Integer.parseInt(greenTextBox.getText().toString());
                    else{
                        greenNum = 0;
                        greenTextBox.setText("0");
                    }
                    if(greenNum > 255) greenNum = 255;

                    greenBar.setProgress(round(greenNum));
                    greenTextBox.setText(String.valueOf(round(greenNum)));
                    handled = true;
                }
                return handled;
            }
        });

        blueTextBox.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
                boolean handled = false;
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    String blueText = blueTextBox.getText().toString();
                    int blueNum;
                    if (!blueText.equals("")) blueNum = Integer.parseInt(blueTextBox.getText().toString());
                    else{
                        blueNum = 0;
                        blueTextBox.setText("0");
                    }
                    if(blueNum > 255) blueNum = 255;

                    blueBar.setProgress(round(blueNum));
                    blueTextBox.setText(String.valueOf(round(blueNum)));
                    handled = true;
                }
                return handled;
            }
        });

    }

    /*this method updates the guessed colour box and checks if the player has won*/
    private void checkColour(EditText redTextBox, EditText greenTextBox, EditText blueTextBox, View guessBox){
        int redNum, greenNum, blueNum;

        if(redTextBox.getText().toString().equals("")) {
            redNum = 0;
            redTextBox.setText("0");
        }
        else redNum = Integer.parseInt(redTextBox.getText().toString());

        if(greenTextBox.getText().toString().equals("")) {
            greenNum = 0;
            greenTextBox.setText("0");
        }
        else greenNum = Integer.parseInt(greenTextBox.getText().toString());

        if(blueTextBox.getText().toString().equals("")) {
            blueNum = 0;
            blueTextBox.setText("0");
        }
        else blueNum = Integer.parseInt(blueTextBox.getText().toString());

        guessBox.setBackgroundColor(Color.rgb(redNum,greenNum,blueNum));

        if(redNum == colAnsRed && greenNum == colAnsGreen && blueNum == colAnsBlue){
            setContentView(R.layout.game_over);

            View colourWinBox = findViewById(R.id.colourWinBox);
            colourWinBox.setVisibility(View.VISIBLE);
            colourWinBox.setBackgroundColor(Color.rgb(colAnsRed,colAnsGreen,colAnsBlue));

            TextView colourWinText = (TextView)findViewById(R.id.colourWinText);
            colourWinText.setVisibility(View.VISIBLE);

            TextView colourWinRed = (TextView)findViewById(R.id.colourWinRed);
            colourWinRed.setText("Red: " + colAnsRed);
            colourWinRed.setVisibility(View.VISIBLE);

            TextView colourWinGreen = (TextView)findViewById(R.id.colourWinGreen);
            colourWinGreen.setText("Green: " + colAnsGreen);
            colourWinGreen.setVisibility(View.VISIBLE);

            TextView colourWinBlue = (TextView)findViewById(R.id.colourWinBlue);
            colourWinBlue.setText("Blue: " + colAnsBlue);
            colourWinBlue.setVisibility(View.VISIBLE);

            colAnsRed = -1;
            colAnsGreen = -1;
            colAnsBlue = -1;

            playerRed = 0;
            playerGreen = 0;
            playerBlue = 0;

            onTempPage = true;

            Button newGame = (Button)findViewById(R.id.newGame);

            newGame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startColourGame();
                }
            });
        }
    }

    /*Runs the number-guessing game*/
    protected void startNumberGame( ){
        setContentView(R.layout.numbergame);
        pageNum = 1;
        if(numAns == -1) numAns = (int)(Math.random()*(gameRange) + 1);

        TextView instructions = (TextView)findViewById(R.id.instruction);
        instructions.setText("Guess the random number between 1 and " + gameRange + ": ");

        Button guessBtn = (Button)findViewById(R.id.guessButton);
        Button resetBtn = (Button)findViewById(R.id.reset1);
        Button goBackBtn = (Button)findViewById(R.id.goBackNum);

        final EditText playerGuessTextBox = (EditText)findViewById(R.id.playerGuess);
        final TextView resultTextBox = (TextView)findViewById(R.id.answer);
        final TextView prevGuessTextBox = (TextView)findViewById(R.id.prevGuess);

        if(playerNum != -1) checkNum(playerNum,playerGuessTextBox,prevGuessTextBox,resultTextBox);

        guessBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                String guessText = playerGuessTextBox.getText().toString();
                if(guessText.equals("")){
                    prevGuessTextBox.setText("You guessed ' '");
                    resultTextBox.setText("That's not a number!");
                }
                else {
                    int guessNum = Integer.parseInt(playerGuessTextBox.getText().toString());
                    checkNum(guessNum, playerGuessTextBox, prevGuessTextBox, resultTextBox);
                }
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                numAns = (int)(Math.random()*(gameRange) + 1);
                playerNum = -1;

                playerGuessTextBox.setText("");
                prevGuessTextBox.setText("");
                resultTextBox.setText("");
            }
        });

        goBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSplashPage();
            }
        });

        playerGuessTextBox.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
                boolean handled = false;
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    String guessText = playerGuessTextBox.getText().toString();
                    if(guessText.equals("")){
                        prevGuessTextBox.setText("You guessed ' '");
                        resultTextBox.setText("That's not a number!");
                    }
                    else {
                        int guessNum = Integer.parseInt(playerGuessTextBox.getText().toString());
                        checkNum(guessNum, playerGuessTextBox, prevGuessTextBox, resultTextBox);
                    }
                    handled = true;
                }
                return handled;
            }
        });
    }

    /*Checks the current input from the player to see if it is correct
    * and updates the display on the page to show Too High/Low if not*/
    private void checkNum(int guessNum, EditText playerGuessText, TextView prevGuessText, TextView resultText){
        playerNum = guessNum;

        if(guessNum < 1 || guessNum > gameRange) {
            resultText.setText("Out of range!");
            playerGuessText.setText("");
            prevGuessText.setText("You guessed " + guessNum);
        }
        else if(guessNum < numAns){
            resultText.setText("Too low!");
            playerGuessText.setText("");
            prevGuessText.setText("You guessed " + guessNum);
        }

        else if (guessNum > numAns){
            resultText.setText("Too high!");
            playerGuessText.setText("");
            prevGuessText.setText("You guessed " + guessNum);
        }

        else{
            setContentView(R.layout.game_over);

            TextView numWin = (TextView)findViewById(R.id.numWin);
            numWin.setText("The answer was "  + numAns);
            numWin.setVisibility(View.VISIBLE);

            numAns = -1;
            playerNum = -1;
            onTempPage = true;

            Button newGame = (Button)findViewById(R.id.newGame);

            newGame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startNumberGame();
                }
            });
        }
    }

    //rounds a number to the closest multiple of (interval)
    //if the number is within (interval) distance of 255, it should round up to 255 if that is closer
    private int round(int x){
        if(x >= 255 - interval){
            if(255 - x <= x - (255/interval)*interval) x = 255;
            else x = (255/interval) * interval;
            return x;
        }
        //this bit was taken from the web:
        int answer = (((x + (interval/2))/interval)*interval);

        if (answer > 255) answer = 255;
        return answer;
    }

    //this method's code was copied from the web:
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_menu, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.settings){
            //store data for the guess-colour game
            if(pageNum == 2 && !onTempPage){
                EditText redText = (EditText)findViewById(R.id.redNum);
                EditText greenText = (EditText)findViewById(R.id.greenNum);
                EditText blueText = (EditText)findViewById(R.id.blueNum);

                if(redText.getText().toString().equals("")) playerRed = 0;
                else playerRed = Integer.parseInt(redText.getText().toString());

                if(greenText.getText().toString().equals("")) playerGreen = 0;
                else playerGreen = Integer.parseInt(greenText.getText().toString());

                if(blueText.getText().toString().equals("")) playerBlue = 0;
                else playerBlue = Integer.parseInt(blueText.getText().toString());
            }
            showSettingsPage();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void showSettingsPage(){
        onTempPage = true;
        setContentView(R.layout.settings_page);

        //settings for number game: can change upper bound on range of game
        //this resets the current number game
        final TextView currentRangeText = (TextView)findViewById(R.id.currentRangeText);
        currentRangeText.setText("Current range: 1 to " + gameRange);

        Button button100 = (Button)findViewById(R.id.button100);
        Button button500 = (Button)findViewById(R.id.button500);
        Button button1000 = (Button)findViewById(R.id.button1000);
        Button custom = (Button)findViewById(R.id.custom);
        Button goBack = (Button)findViewById(R.id.goBack);

        button100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameRange = 100;
                numAns = -1;
                playerNum = -1;
                currentRangeText.setText("Current range: 1 to " + gameRange);
            }
        });

        button500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameRange = 500;
                numAns = -1;
                playerNum = -1;
                currentRangeText.setText("Current range: 1 to " + gameRange);
            }
        });

        button1000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameRange = 1000;
                numAns = -1;
                playerNum = -1;
                currentRangeText.setText("Current range: 1 to " + gameRange);
            }
        });

        custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText rangeInput = (EditText)findViewById(R.id.rangeInput);
                String rangeText = rangeInput.getText().toString();

                if(rangeText.equals("")) gameRange = 100;
                else gameRange = Integer.parseInt(rangeText);

                if(gameRange == 0) gameRange = 100;

                numAns = -1;
                playerNum = -1;

                currentRangeText.setText("Current range: 1 to " + gameRange);
                rangeInput.setText("");
            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTempPage = false;
                switch(pageNum){
                    case 0:
                        startSplashPage();
                        break;
                    case 1:
                        startNumberGame();
                        break;
                    case 2:
                        startColourGame();
                        break;
                }
            }
        });

        //settings for colour-guessing game: can change difficulty
        //(i.e. size of interval)
        //Easy = 60; Medium = 40; Hard = 20; Hardest = 10;

        RadioGroup difficulties = (RadioGroup)findViewById(R.id.difficulties);

        switch(interval){
            case 60:
                difficulties.check(R.id.radioEasy);
                break;
            case 40:
                difficulties.check(R.id.radioMedium);
                break;
            case 20:
                difficulties.check(R.id.radioHard);
                break;
            case 10:
                difficulties.check(R.id.radioHardest);
                break;
        }

        difficulties.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.radioEasy:
                        interval = 60;
                        break;
                    case R.id.radioMedium:
                        interval = 40;
                        break;
                    case R.id.radioHard:
                        interval = 20;
                        break;
                    case R.id.radioHardest:
                        interval = 10;
                        break;
                }

                //reset current game
                colAnsRed = -1;
                colAnsGreen = -1;
                colAnsBlue = -1;

                playerRed = 0;
                playerGreen = 0;
                playerBlue = 0;
            }
        });


    }

    @Override
    public void onBackPressed(){
        if(onTempPage){
            switch(pageNum){
                case 0:
                    startSplashPage();
                    break;
                case 1:
                    startNumberGame();
                    break;
                case 2:
                    startColourGame();
                    break;
            }
            onTempPage = false;
            return;
        }
        if(pageNum != 0) {
            if(pageNum == 2){
                //need to store current data for the guess-colour game only
                //because the other game stores it at every step
                EditText redText = (EditText)findViewById(R.id.redNum);
                EditText greenText = (EditText)findViewById(R.id.greenNum);
                EditText blueText = (EditText)findViewById(R.id.blueNum);

                if(redText.getText().toString().equals("")) playerRed = 0;
                else playerRed = Integer.parseInt(redText.getText().toString());

                if(greenText.getText().toString().equals("")) playerGreen = 0;
                else playerGreen = Integer.parseInt(greenText.getText().toString());

                if(blueText.getText().toString().equals("")) playerBlue = 0;
                else playerBlue = Integer.parseInt(blueText.getText().toString());
            }

            startSplashPage();
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        SharedPreferences settings = getSharedPreferences(prefsName,0);
        SharedPreferences.Editor editor = settings.edit();

        editor.putInt("pageNum",pageNum);
        editor.putInt("interval",interval);
        editor.putInt("gameRange",gameRange);

        editor.putInt("numAns",numAns);
        editor.putInt("playerNum",playerNum);

        editor.putInt("colAnsRed",colAnsRed);
        editor.putInt("colAnsGreen",colAnsGreen);
        editor.putInt("colAnsBlue",colAnsBlue);

        //if we're currently on the colour-guessing page
        //then get the current values of the player's guess
        //otherwise get the stored ones

        if(pageNum == 2 && !onTempPage){
            EditText redText = (EditText)findViewById(R.id.redNum);
            EditText greenText = (EditText)findViewById(R.id.greenNum);
            EditText blueText = (EditText)findViewById(R.id.blueNum);

            int redNum, greenNum, blueNum;
            if(redText.getText().toString().equals("")) redNum = 0;
            else redNum = Integer.parseInt(redText.getText().toString());

            if(greenText.getText().toString().equals("")) greenNum = 0;
            else greenNum = Integer.parseInt(greenText.getText().toString());

            if(blueText.getText().toString().equals("")) blueNum = 0;
            else blueNum = Integer.parseInt(blueText.getText().toString());

            editor.putInt("playerRed",redNum);
            editor.putInt("playerGreen",greenNum);
            editor.putInt("playerBlue",blueNum);
        }
        else{
            editor.putInt("playerRed",playerRed);
            editor.putInt("playerGreen",playerGreen);
            editor.putInt("playerBlue",playerBlue);
        }

        editor.commit();
    }
}