package com.geargames.regolith.helpers;

import com.geargames.regolith.units.battle.Direction;

/**
 * User: mikhail v. kutuzov
 * Date: 18.12.12
 * Time: 13:50
 */
public class ClientGUIHelper {

    public static String getWeightRepresentation(short grams, String kg, String g) {
        int kilos = grams / 1000;
        int rest = grams % 1000;
        if (kilos != 0) {
            if (rest != 0) {
                return kilos + kg + " " + rest + g;
            } else {
                return kilos + kg;
            }
        } else {
            return rest + g;
        }
    }

    public static String getWeightRepresentation(short grams) {
        return getWeightRepresentation(grams, "kg", "g");
    }


    /**
     * Класс работает с набором направлений от 0 до 7; с отсутствием направления класс работает не корректно.
     *
     * @param direction
     * @return
     * @see Direction
     */
    public static int convertPackerScriptsDirection(Direction direction) {
        return (direction.getNumber() + 2) % Direction.NONE.getNumber();
    }

}
