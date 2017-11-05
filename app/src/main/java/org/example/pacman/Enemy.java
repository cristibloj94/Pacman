package org.example.pacman;

/**
 * Created by Cristi on 11/3/2017.
 */

public class Enemy {
    public boolean isAlive = true;
    private int enemyX, enemyY;

    public Enemy(int enemyX, int enemyY) {
        this.enemyX = enemyX;
        this.enemyY = enemyY;
    }

    public int getEnemyX() { return enemyX; }

    public void setEnemyX(int enemyX) {
        this.enemyX = enemyX;
    }

    public int getEnemyY() { return enemyY; }

    public void setEnemyY(int enemyY) {
        this.enemyY = enemyY;
    }

    public boolean isAlive() { return isAlive; }

    public void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }
}
