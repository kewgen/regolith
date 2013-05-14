package com.geargames.regolith.units.map.unit;

/**
 * Users: m.v.kutuzov, abarakov
 * Date: 03.04.13
 */
public class Actions {

    public static final byte HUMAN_STAND = 0;            // Стоит
    public static final byte HUMAN_RUN = 8;              // Движется
    public static final byte HUMAN_STAND_AND_SHOOT = 16; // Стреляет стоя
    public static final byte HUMAN_SIT_DOWN = 24;        // Садится
    public static final byte HUMAN_SIT = 32;             // Сидит
    public static final byte HUMAN_SIT_AND_SHOOT = 40;   // Стреляет сидя
    public static final byte HUMAN_SIT_AND_HIT = 48;     // Получает урон сидя
    public static final byte HUMAN_STAND_UP = 56;        // Встает
    public static final byte HUMAN_STAND_AND_HIT = 64;   // Получает урон стоя
    public static final byte HUMAN_STAND_AND_DIE = 72;   // Умирает стоя
    public static final byte HUMAN_DIE = 80;             // Лежит мертвый
    public static final byte HUMAN_SIT_AND_DIE = 88;     // Умирает сидя

    public static final byte DOOR_CLOSED = 0;  // закрытая дверь
    public static final byte DOOR_OPENS = 2;   // дверь открывается
    public static final byte DOOR_OPENED = 4;  // открытая дверь
    public static final byte DOOR_CLOSING = 6; // дверь закрывается

}
