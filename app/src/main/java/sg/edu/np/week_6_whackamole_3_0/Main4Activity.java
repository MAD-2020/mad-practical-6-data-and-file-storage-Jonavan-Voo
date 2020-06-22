package sg.edu.np.week_6_whackamole_3_0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class Main4Activity extends AppCompatActivity {

    /* Hint:
        1. This creates the Whack-A-Mole layout and starts a countdown to ready the user
        2. The game difficulty is based on the selected level
        3. The levels are with the following difficulties.
            a. Level 1 will have a new mole at each 10000ms.
                - i.e. level 1 - 10000ms
                       level 2 - 9000ms
                       level 3 - 8000ms
                       ...
                       level 10 - 1000ms
            b. Each level up will shorten the time to next mole by 100ms with level 10 as 1000 second per mole.
            c. For level 1 ~ 5, there is only 1 mole.
            d. For level 6 ~ 10, there are 2 moles.
            e. Each location of the mole is randomised.
        4. There is an option return to the login page.
     */
    private static final String FILENAME = "Main4Activity.java";
    private static final String TAG = "Whack-A-Mole3.0!";
    CountDownTimer readyTimer;
    CountDownTimer newMolePlaceTimer;
    private TextView currentScore;
    private Integer score, level;
    private String username;
    private Button back;
    MyDBHandler dbHandler = new MyDBHandler(this,null,null,1);
    private void readyTimer(){
        /*  HINT:
            The "Get Ready" Timer.
            Log.v(TAG, "Ready CountDown!" + millisUntilFinished/ 1000);
            Toast message -"Get Ready In X seconds"
            Log.v(TAG, "Ready CountDown Complete!");
            Toast message - "GO!"
            belongs here.
            This timer countdown from 10 seconds to 0 seconds and stops after "GO!" is shown.
         */
        readyTimer = new CountDownTimer(10000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                final Toast toast = Toast.makeText(Main4Activity.this,"Get Ready in "+(millisUntilFinished/1000+1)+" seconds",Toast.LENGTH_SHORT);
                toast.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.cancel();
                    }
                }, 1000);
                Log.v(TAG, "Ready CountDown!" + (millisUntilFinished/ 1000 + 1));
            }

            @Override
            public void onFinish() {
                Toast.makeText(Main4Activity.this,"GO!",Toast.LENGTH_SHORT).show();
                placeMoleTimer();
                Log.v(TAG, "Ready CountDown Complete!");
            }
        };
        readyTimer.start();
    }
    private void placeMoleTimer(){
        /* HINT:
           Creates new mole location each second.
           Log.v(TAG, "New Mole Location!");
           setNewMole();
           belongs here.
           This is an infinite countdown timer.
         */
        int speed = 11000 - (level * 1000);
        newMolePlaceTimer = new CountDownTimer(speed,speed) {
            @Override
            public void onTick(long millisUntilFinished) {
                setNewMole();
                Log.v(TAG, "New Mole Location!");
            }

            @Override
            public void onFinish() {
                newMolePlaceTimer.start();
            }
        };
        newMolePlaceTimer.start();
    }
    private static final int[] BUTTON_IDS = {
            /* HINT:
                Stores the 9 buttons IDs here for those who wishes to use array to create all 9 buttons.
                You may use if you wish to change or remove to suit your codes.*/
            R.id.button1,
            R.id.button2,
            R.id.button3,
            R.id.button4,
            R.id.button5,
            R.id.button6,
            R.id.button7,
            R.id.button8,
            R.id.button9
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        /*Hint:
            This starts the countdown timers one at a time and prepares the user.
            This also prepares level difficulty.
            It also prepares the button listeners to each button.
            You may wish to use the for loop to populate all 9 buttons with listeners.
            It also prepares the back button and updates the user score to the database
            if the back button is selected.
         */
        currentScore = (TextView) findViewById(R.id.score);
        back = (Button) findViewById(R.id.backButton);
        score = 0;
        Intent receivingEnd = getIntent();
        level = receivingEnd.getIntExtra("level",0);
        username = receivingEnd.getStringExtra("username");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserScore();
                Intent intent = new Intent(Main4Activity.this,Main3Activity.class);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });
        for(final int id : BUTTON_IDS){
            /*  HINT:
            This creates a for loop to populate all 9 buttons with listeners.
            You may use if you wish to remove or change to suit your codes.
            */
            final Button button = (Button) findViewById(id);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doCheck(button);
                }
            });
        }
    }
    @Override
    protected void onStart(){
        super.onStart();
        readyTimer();
    }
    private void doCheck(Button checkButton)
    {
        /* Hint:
            Checks for hit or miss
            Log.v(TAG, FILENAME + ": Hit, score added!");
            Log.v(TAG, FILENAME + ": Missed, point deducted!");
            belongs here.
        */
        if(checkButton.getText() == "*"){
            score = score + 1;
            checkButton.setText("O");
            Log.v(TAG, FILENAME + ": Hit, score added!");
        }else{
            score = score - 1;
            Log.v(TAG, FILENAME + ": Missed, point deducted!");
        }
        currentScore.setText(Integer.toString(score));
    }

    public void setNewMole()
    {
        /* Hint:
            Clears the previous mole location and gets a new random location of the next mole location.
            Sets the new location of the mole. Adds additional mole if the level difficulty is from 6 to 10.
         */
        Random ran = new Random();
        int randomLocation = ran.nextInt(9);
        for (int id : BUTTON_IDS){
            Button button = findViewById(id);
            button.setText("O");
        }
        Button mole = findViewById(BUTTON_IDS[randomLocation]);
        mole.setText("*");
        if (level > 5){
            int randomLocation2 = ran.nextInt(9);
            while (randomLocation2 == randomLocation){
                randomLocation2 = ran.nextInt(9);
            }
            Button mole2 = findViewById(BUTTON_IDS[randomLocation2]);
            mole2.setText("*");
        }
    }

    private void updateUserScore()
    {

     /* Hint:
        This updates the user score to the database if needed. Also stops the timers.
        Log.v(TAG, FILENAME + ": Update User Score...");
      */
        if (newMolePlaceTimer != null) {
            newMolePlaceTimer.cancel();
        }
        readyTimer.cancel();
        UserData userData = dbHandler.findUser(username);
        if (userData.getScores().get(level - 1) < score){
            dbHandler.deleteAccount(username);
            userData.getScores().set(level - 1,score);
            dbHandler.addUser(userData);
            Log.v(TAG, FILENAME + ": Update User Score...");
        }
    }

}
