package com.geargames.regolith.serializers;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleSerializer;
import com.geargames.regolith.units.Entity;
import com.geargames.regolith.units.battle.Ally;
import com.geargames.regolith.units.battle.Barrier;
import com.geargames.regolith.units.battle.Box;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.dictionaries.EntityCollection;
import com.geargames.regolith.units.tackle.*;

/**
 * User: mkutuzov
 * Date: 18.03.13
 */
public class SerializeHelper {
    public static final int NULL_REFERENCE = -1;

    public static void serializeEntityReference(Entity entity, MicroByteBuffer buffer) {
        if (entity == null) {
            SimpleSerializer.serialize(NULL_REFERENCE, buffer);
        } else {
            SimpleSerializer.serialize(entity.getId(), buffer);
        }
    }

    public static void serializeEntityReferences(EntityCollection entities, MicroByteBuffer buffer) {
        byte length = (byte) entities.size();
        if (length > 127) {
            throw new IllegalArgumentException();
        }
        buffer.put(length);
        for (int i = 0; i < length; i++) {
            serializeEntityReference(entities.get(i), buffer);
        }
    }

    public static final String[] CLASSES = new String[] {
            Warrior.class.getSimpleName(),
            Box.class.getSimpleName(),
            Magazine.class.getSimpleName(),
            Barrier.class.getSimpleName(),
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
