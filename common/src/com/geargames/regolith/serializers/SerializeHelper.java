package com.geargames.regolith.serializers;

import com.geargames.regolith.units.battle.Ally;
import com.geargames.regolith.units.battle.Border;
import com.geargames.regolith.units.battle.Box;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.tackle.*;

/**
 * User: mkutuzov
 * Date: 18.03.13
 */
public class SerializeHelper {
    public static final String[] CLASSES = new String[]{
            Warrior.class.getSimpleName(),
            Box.class.getSimpleName(),
            Magazine.class.getSimpleName(),
            Border.class.getSimpleName(),
            Armor.class.getSimpleName(),
            Weapon.class.getSimpleName(),
            Medikit.class.getSimpleName(),
            Ally.class.getSimpleName(),
            Projectile.class.getSimpleName()
    };

    public static short findTypeId(String clazz) {
        for (short i = 0; i < CLASSES.length; i++) {
            if (CLASSES[i].equals(clazz)) {
                return i;
            }
        }
        return -1;
    }

}
