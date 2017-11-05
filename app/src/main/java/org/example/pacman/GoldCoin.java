package org.example.pacman;

import java.util.ArrayList;

/**
 * This class should contain information about a single GoldCoin.
 * such as x and y coordinates (int) and whether or not the goldcoin
 * has been taken (boolean)
 */

public class GoldCoin {
    public boolean collected = false;
    private int coinX, coinY;

    public GoldCoin(int coinX, int coinY) {
        this.coinX = coinX;
        this.coinY = coinY;
    }

    public int getCoinX() { return coinX; }

    public int getCoinY() { return coinY; }

    public boolean isCollected() { return collected; }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }
}