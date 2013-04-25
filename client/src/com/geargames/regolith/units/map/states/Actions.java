package com.geargames.regolith.units.map.states;

/**
 * User: m.v.kutuzov
 * Date: 03.04.13
 */
public class Actions {

    public static final byte STAND = 0;            // Стоит
    public static final byte RUN = 8;              // Движется
    public static final byte STAND_AND_SHOOT = 16; // Стреляет стоя
    public static final byte SIT_DOWN = 24;        // Садится
    public static final byte SIT = 32;             // Сидит
    public static final byte SIT_AND_SHOOT = 40;   // Стреляет сидя
    public static final byte SIT_AND_HIT = 48;     // Получает урон сидя
    public static final byte STAND_UP = 56;        // Встает
    public static final byte STAND_AND_HIT = 64;   // Получает урон стоя
    public static final byte STAND_AND_DIE = 72;   // Умирает стоя
    public static final byte DIE = 80;             // Лежит мертвый
    public static final byte SIT_AND_DIE = 88;     // Умирает сидя

}
