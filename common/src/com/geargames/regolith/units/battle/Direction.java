package com.geargames.regolith.units.battle;

import java.io.Serializable;

/**
 * Users: mkutuzov, abarakov
 * Date: 19.02.12
 */
public class Direction implements Serializable {
    public static Direction UP_DOWN = new Direction(0, 1, 2);
    public static Direction UP_DOWN_LEFT = new Direction(-1, 1, 1);
    public static Direction RIGHT_LEFT = new Direction(-1, 0, 0);
    public static Direction DOWN_UP_LEFT = new Direction(-1, -1, 7);
    public static Direction DOWN_UP = new Direction(0, -1, 6);
    public static Direction DOWN_UP_RIGHT = new Direction(1, -1, 5);
    public static Direction LEFT_RIGHT = new Direction(1, 0, 4);
    public static Direction UP_DOWN_RIGHT = new Direction(1, 1, 3);
    public static Direction NONE = new Direction(0, 0, 8);

    private int x;
    private int y;
    private int number; //todo: number -> id?

    private Direction(int x, int y, int number) {
        this.x = x;
        this.y = y;
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public static Direction getByNumber(int number) {
        switch (number) {
            case 4:
                return LEFT_RIGHT;
            case 3:
                return UP_DOWN_RIGHT;
            case 2:
                return UP_DOWN;
            case 1:
                return UP_DOWN_LEFT;
            case 0:
                return RIGHT_LEFT;
            case 7:
                return DOWN_UP_LEFT;
            case 6:
                return DOWN_UP;
            case 5:
                return DOWN_UP_RIGHT;
            default:
                return NONE;
        }
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

}
