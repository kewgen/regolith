package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.serialization.SimpleSerializer;
import com.geargames.regolith.Packets;
import com.geargames.regolith.serializers.SerializeHelper;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.dictionaries.ServerWarriorCollection;

/**
 * Users: mvkutuzov, abarakov
 * Date: 25.04.13
 * Time: 22:13
 */
public class ServerMoveAllyAnswer extends SerializedMessage {
    private MicroByteBuffer buffer;
    private Warrior unit;
    private ServerWarriorCollection enemies;

    public ServerMoveAllyAnswer(MicroByteBuffer buffer, Warrior unit, ServerWarriorCollection enemies) {
        this.buffer = buffer;
        this.unit = unit;
        this.enemies = enemies;
    }

    @Override
    public short getType() {
        return Packets.MOVE_ALLY;
    }

    @Override
    protected MicroByteBuffer getBuffer() {
        return buffer;
    }

    /**
     * id война
     * x и y на карте куда он пойдёт(конец пути)
     * массив врагов которых он "засветил" ("засвет" противника может произойти только в конце пути)
     *
     * @param buffer
     */
    @Override
    public void serialize(MicroByteBuffer buffer) {
        SerializeHelper.serializeEntityReference(unit, buffer);
        SimpleSerializer.serialize(unit.getCellX(), buffer);
        SimpleSerializer.serialize(unit.getCellY(), buffer);
        if (enemies != null) {
            SimpleSerializer.serialize((byte) enemies.size(), buffer);
            for (Warrior human : enemies.getWarriors()) {
                SerializeHelper.serializeEntityReference(human, buffer);
                SimpleSerializer.serialize(human.getCellX(), buffer);
                SimpleSerializer.serialize(human.getCellY(), buffer);
            }
        } else {
            SimpleSerializer.serialize(0, buffer);
        }
    }

}
