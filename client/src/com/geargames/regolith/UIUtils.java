package com.geargames.regolith;

import com.geargames.common.String;

/**
 * User: mikhail v. kutuzov
 * Date: 18.12.12
 * Time: 13:50
 */
public class UIUtils {

    public static String getWeightRepresentation(short grams, String kg, String g) {
        int kilos = grams / 1000;
        int rest = grams % 1000;
        if (kilos != 0) {
            if (rest != 0) {
                return String.valueOfI(kilos).concat(kg).concatI(rest).concat(g);
            } else {
                return String.valueOfI(kilos).concat(kg);
            }
        } else {
            return String.valueOfI(rest).concat(g);
        }
    }

    public static String getWeightRepresentation(short grams){
        return getWeightRepresentation(grams, String.valueOfC("kg"), String.valueOfC("g"));
    }

}
