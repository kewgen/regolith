package com.geargames.regolith.units.battle;

/**
 * User: mkutuzov
 * Date: 19.02.12
 */
public class Direction {
    public static Direction UP_DOWN = new Direction(0, 1, 2);
    public static Direction UP_DOWN_LEFT = new Direction(-1, 1, 3);
    public static Direction RIGHT_LEFT = new Direction(-1, 0, 4);
    public static Direction DOWN_UP_LEFT = new Direction(-1, -1, 5);
    public static Direction DOWN_UP = new Direction(0, -1, 6);
    public static Direction DOWN_UP_RIGTH = new Direction(1, -1, 7);
    public static Direction LEFT_RIGHT = new Direction(1, 0, 0);
    public static Direction UP_DOWN_RIGHT = new Direction(1, 1, 1);
    public static Direction NONE = new Direction(0, 0, 8);

    private Direction(int x, int y, int number){
        this.x = x;
        this.y = y;
        this.number = number;
    }

    private int number;
    private int x;
    private int y;

    public int getNumber() {
        return number;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }
}
