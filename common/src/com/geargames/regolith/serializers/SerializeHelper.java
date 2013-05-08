package com.geargames.regolith.serializers;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleSerializer;
import com.geargames.regolith.units.Entity;
import com.geargames.regolith.units.dictionaries.EntityCollection;

/**
 * User: mkutuzov
 * Date: 18.03.13
 */
public class SerializeHelper {
    public static final int NULL_REFERENCE = -1;
    public static final short NULL_COORDINATE = -1;

    public static final short ENEMY = 1;
    public static final short ALLY = 2;
    public static final short WARRIOR = 3;
    public static final short BARRIER = 4;
    public static final short MEDIKIT = 5;
    public static final short PROJECTILE = 6;
    public static final short HARVESTER = 7;
    public static final short ARMOR = 8;
    public static final short MAGAZINE = 9;
    public static final short WEAPON = 10;
    public static final short BOX = 11;

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

}
