package org.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Random;

import static org.example.pacman.R.drawable.enemy;

/**
 *
 * This class should contain all your game logic
 */

public class Game {
    //context is a reference to the activity
    private Context context;
    private int points = 0; //how points do we have
    private boolean running = false;
    private int direction;
    private int enemyDirection;

    //bitmap of the pacman and coin
    private Bitmap pacBitmap;
    private Bitmap coinBitmap;
    private Bitmap enemyBitmap;
    //textview reference to points
    private TextView pointsView;
    private int pacx, pacy;
    private int enemyX, enemyY;
    //the list of goldcoins - initially empty
    private ArrayList<GoldCoin> coins = new ArrayList<>();
    private ArrayList<Enemy> enemies = new ArrayList<>();
    //a reference to the gameview
    private GameView gameView;
    private int h,w; //height and width of screen

    public Game(Context context, TextView view)
    {
        this.context = context;
        this.pointsView = view;
        pacBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.pacman);
        coinBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.coin);
        coinBitmap = Bitmap.createScaledBitmap(coinBitmap, 100, 100, true);
        enemyBitmap = BitmapFactory.decodeResource(context.getResources(), enemy);
        enemyBitmap = Bitmap.createScaledBitmap(enemyBitmap, 100, 100, true);
    }

    public void setGameView(GameView view)
    {
        this.gameView = view;
    }

    //TODO initialize goldcoins also here
    public void newGame()
    {
        pacx = 50;
        pacy = 400; //just some starting coordinates

        //reset the points
        points = 0;
        running = true;
        setDirection(0);
        //setEnemyDirection(0);

        pointsView.setText(context.getResources().getString(R.string.points)+" "+points);

        for (GoldCoin coin: getCoins()) {
            coin.setCollected(false);
        }

        for (Enemy enemy: getEnemies()) {
            enemy.setAlive(true);
        }

        gameView.invalidate(); //redraw screen
    }

    public void PauseGame()
    {
        setRunning(false);
    }

    public void ContinueGame()
    {
        for (Enemy enemy: getEnemies()){
            if (!enemy.isAlive()){
                CharSequence text = "Game ended, create a new game!";
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                gameView.invalidate();

            }
            else {
                setRunning(true);
                CharSequence text = "Game resumed!";
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }
    }

    public void setSize(int h, int w)
    {
        this.h = h;
        this.w = w;

        if (getCoins().size() == 0)
        {
            // init coins and add to the arraylist
            GoldCoin coin1 = new GoldCoin(300, 170);
            GoldCoin coin2 = new GoldCoin(500, 400);
            GoldCoin coin3 = new GoldCoin(700, 350);
            GoldCoin coin4 = new GoldCoin(150, 75);

            coins.add(coin1);
            coins.add(coin2);
            coins.add(coin3);
            coins.add(coin4);
        }

        if(getEnemies().size() == 0)
        {
            Enemy enemy = new Enemy(320, 400);

            enemies.add(enemy);
        }
    }

    public void movePacmanX(int pixels)
    {
        //still within our boundaries?
        if (pacx+pixels+pacBitmap.getWidth()<w && pacx+pixels>0) {
            pacx = pacx + pixels;
            doCollisionCheck();
            gameView.invalidate();
        }
    }

    public void movePacmanY(int pixels)
    {
        //still within our boundaries?
        if (pacy+pixels+pacBitmap.getHeight()<h && pacy+pixels>0) {
            pacy = pacy + pixels;
            doCollisionCheck();
            gameView.invalidate();
        }
    }

    public void moveEnemyX(int pixels)
    {
        //still within our boundaries?
        for (Enemy enemy: getEnemies()) {
            if (enemy.getEnemyX()+pixels+enemyBitmap.getWidth()<w && enemy.getEnemyX()+pixels>0) {
                enemy.setEnemyX(enemy.getEnemyX()+pixels);
                gameView.invalidate();
            }
        }
    }

    public void moveEnemyY(int pixels)
    {
        //still within our boundaries?
        for (Enemy enemy: getEnemies()) {
            if (enemy.getEnemyY()+pixels+enemyBitmap.getHeight()<h && enemy.getEnemyY()+pixels>0) {
                enemy.setEnemyY(enemy.getEnemyY()+pixels);
                gameView.invalidate();
            }
        }
    }

    public void checkIfAllCoinsWereCollected() {
        boolean haveAllCoinBeenTaken = true;

        for (GoldCoin coin: getCoins()) {
            if(coin.isCollected() == false) {
                haveAllCoinBeenTaken = false;
            }
        }

        if(haveAllCoinBeenTaken) {
            CharSequence text = "All coins were collected!";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            setRunning(false);
        }
    }

    //TODO check if the pacman touches a gold coin
    //and if yes, then update the neccesseary data
    //for the gold coins and the points
    //so you need to go through the arraylist of goldcoins and
    //check each of them for a collision with the pacman
    public void doCollisionCheck()
    {
        int middleOfPacmanX = pacx + pacBitmap.getWidth()/2;
        int middleOfPacmanY = pacy + pacBitmap.getHeight()/2;

        for (GoldCoin coin: getCoins())
        {
            int middleOfCoinX = coin.getCoinX() + coinBitmap.getWidth()/2;
            int middleOfCoinY = coin.getCoinY() + coinBitmap.getHeight()/2;
            if(!coin.isCollected()) {
                //canvas.drawBitmap(game.getCoinBitmap(), coin.getCoinX(), coin.getCoinY(), paint);
                double x = Math.pow(middleOfPacmanX - middleOfCoinX, 2);
                double y = Math.pow(middleOfPacmanY - middleOfCoinY, 2);
                double distance = Math.sqrt(x + y);

                if(distance < 50) {
                    coin.setCollected(true);
                    points++;
                    gameView.invalidate();
                    pointsView.setText(context.getResources().getString(R.string.points)+" "+points);
                }
            }
        }

        for (Enemy enemy: getEnemies())
        {
            int middleOfEnemyX = enemy.getEnemyX() + enemyBitmap.getWidth()/2;
            int middleOfEnemyY = enemy.getEnemyY() + enemyBitmap.getHeight()/2;
            if(enemy.isAlive()) {
                //canvas.drawBitmap(game.getCoinBitmap(), coin.getCoinX(), coin.getCoinY(), paint);
                double x = Math.pow(middleOfPacmanX - middleOfEnemyX, 2);
                double y = Math.pow(middleOfPacmanY - middleOfEnemyY, 2);
                double distance = Math.sqrt(x + y);

                if(distance < 50) {
                    enemy.setAlive(false);
                    setRunning(false);
                    CharSequence text = "Game over!";
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    gameView.invalidate();
                    pointsView.setText(context.getResources().getString(R.string.points)+" "+points);
                }
            }
        }

        checkIfAllCoinsWereCollected();
    }

    public int getPacx()
    {
        return pacx;
    }

    public int getPacy()
    {
        return pacy;
    }

    public int getPoints()
    {
        return points;
    }

    public ArrayList<GoldCoin> getCoins()
    {
        return coins;
    }

    public ArrayList<Enemy> getEnemies() { return enemies; }

    public Bitmap getPacBitmap()
    {
        return pacBitmap;
    }

    public Bitmap getCoinBitmap() { return coinBitmap; }

    public Bitmap getEnemyBitmap() { return enemyBitmap; }

    public boolean isRunning() { return running; }

    public void setRunning(boolean running) { this.running = running; }

    public int getDirection() { return direction; }

    public void setDirection(int direction) { this.direction = direction; }

    public int getEnemyDirection() { return enemyDirection; }

    public void setEnemyDirection(int enemyDirection) { this.enemyDirection = enemyDirection; }

}