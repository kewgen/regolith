package com.geargames.regolith.map;

/**
 * Users: mkutuzov, abarakov
 * Date: 15.02.12
 */
public class Pair {
    int x;
    int y;

    public Pair(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Pair() {
        this(0, 0);
    }

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

    @Override
    public String toString() {
        return "X = " + x + "; Y = " + y;
    }

}
