package org.example.pacman;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

import static android.R.attr.button;

public class MainActivity extends Activity {
    GameView gameView;
    Game game;
    private Timer myTimer;
    private Timer enemyTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //saying we want the game to run in one mode only
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        gameView = findViewById(R.id.gameView);
        TextView textView = findViewById(R.id.points);


        game = new Game(this,textView);
        game.setGameView(gameView);
        gameView.setGame(game);

        game.newGame();

        //make a new timer
        myTimer = new Timer();
        enemyTimer = new Timer();

        //We will call the timer 5 times each second
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                TimerMethod();
            }

        }, 0, 10); //0 indicates we start now, 200
        //is the number of miliseconds between each call

        enemyTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                EnemyTimerMethod();
            }

        }, 0, 10); //0 indicates we start now, 200
        //is the number of miliseconds between each call

        gameView.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeTop() {
                game.setDirection(4);
            }
            public void onSwipeRight() {
                game.setDirection(1);
            }
            public void onSwipeLeft() {
                game.setDirection(3);
            }
            public void onSwipeBottom() {
                game.setDirection(2);
            }

        });
    }

    private void TimerMethod()
    {
        //This method is called directly by the timer
        //and runs in the same thread as the timer.

        //We call the method that will work with the UI
        //through the runOnUiThread method.
        this.runOnUiThread(Timer_Tick);
    }

    private void EnemyTimerMethod()
    {
        //This method is called directly by the timer
        //and runs in the same thread as the timer.

        //We call the method that will work with the UI
        //through the runOnUiThread method.
        this.runOnUiThread(Enemy_Timer_Tick);
    }

    private Runnable Timer_Tick = new Runnable() {
        public void run() {
            //This method runs in the same thread as the UI.
            // so we can draw
            if (game.isRunning())
            {
                //game.moveEnemyX(1);
                if(game.getDirection() == 1) {
                    game.movePacmanX(2);
                }
                else if(game.getDirection() == 2) {
                    game.movePacmanY(2);
                }
                else if(game.getDirection() == 3) {
                    game.movePacmanX(-2);
                }
                else if(game.getDirection() == 4) {
                    game.movePacmanY(-2);
                }

                //update the counter - notice this is NOT seconds in this example
                //you need TWO counters - one for the time and one for the pacman
                //gameView.move(20); //move the pacman - you
                //should call a method on your game class to move
                //the pacman instead of this
            }

        }
    };

    private Runnable Enemy_Timer_Tick = new Runnable() {
        public void run() {
            //This method runs in the same thread as the UI.
            // so we can draw
            if (game.isRunning())
            {
                //game.moveEnemyX(1);
                if(game.getDirection() == 1) {
                    game.moveEnemyX(-2);
                }
                else if(game.getDirection() == 2) {
                    game.moveEnemyY(2);
                }
                else if(game.getDirection() == 3) {
                    game.moveEnemyX(2);
                }
                else if(game.getDirection() == 4) {
                    game.moveEnemyY(-2);
                }

                //update the counter - notice this is NOT seconds in this example
                //you need TWO counters - one for the time and one for the pacman
                //gameView.move(20); //move the pacman - you
                //should call a method on your game class to move
                //the pacman instead of this
            }

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Context context = getApplicationContext();
            CharSequence text = "Setting was clicked!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            return true;
        }

        if (id == R.id.action_newGame) {
            game.newGame();
            Context context = getApplicationContext();
            CharSequence text = "Game restarted!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            return true;
        }

        if (id == R.id.action_pauseGame) {
            game.PauseGame();
            Context context = getApplicationContext();
            CharSequence text = "Game paused!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            return true;
        }

        if (id == R.id.action_ContinueGame) {
            game.ContinueGame();
            Context context = getApplicationContext();
            CharSequence text = "Game resumed!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
