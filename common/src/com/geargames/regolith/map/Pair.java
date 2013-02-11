package com.geargames.regolith.map;

/**
 * User: mkutuzov
 * Date: 15.02.12
 * Time: 17:15
 */
public class Pair {
    int x;
    int y;

    public Pair(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Pair(){}

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
