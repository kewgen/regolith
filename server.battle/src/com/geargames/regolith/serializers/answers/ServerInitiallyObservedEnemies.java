package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.serialization.SimpleSerializer;
import com.geargames.regolith.Packets;
import com.geargames.regolith.serializers.SerializeHelper;
import com.geargames.regolith.units.battle.Warrior;

import java.util.Set;

/**
 * User: mvkutuzov
 * Date: 26.04.13
 * Time: 12:48
 */
public class ServerInitiallyObservedEnemies extends SerializedMessage {
    private MicroByteBuffer buffer;
    private Set<? extends Warrior> enemies;

    public ServerInitiallyObservedEnemies(MicroByteBuffer buffer, Set<? extends Warrior> enemies) {
        this.buffer = buffer;
        this.enemies = enemies;
    }

    @Override
    public short getType() {
        return Packets.INITIALLY_OBSERVED_ENEMIES;
    }

    @Override
    protected MicroByteBuffer getBuffer() {
        return buffer;
    }

    @Override
    public void serialize(MicroByteBuffer buffer) {
        SimpleSerializer.serialize((byte) enemies.size(), buffer);
        for (Warrior unit : enemies) {
            SerializeHelper.serializeEntityReference(unit, buffer);
            SimpleSerializer.serialize(unit.getCellX(), buffer);
            SimpleSerializer.serialize(unit.getCellY(), buffer);
        }
    }

}
