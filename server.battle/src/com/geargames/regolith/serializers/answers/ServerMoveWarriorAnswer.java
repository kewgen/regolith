package com.geargames.regolith.serializers.answers;

import com.geargames.regolith.Packets;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.serialization.SimpleSerializer;
import com.geargames.regolith.serializers.SerializeHelper;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.dictionaries.ServerWarriorCollection;

/**
 * Users: mikhail v. kutuzov, abarakov
 * Date: 23.08.12
 * Time: 13:56
 */
public class ServerMoveWarriorAnswer extends SerializedMessage {
    private MicroByteBuffer buffer;
    private Warrior unit;
    private ServerWarriorCollection enemies;
    private boolean success;

    public static ServerMoveWarriorAnswer answerFailure(MicroByteBuffer buffer) {
        return new ServerMoveWarriorAnswer(buffer, null, null, false);
    }


    public static ServerMoveWarriorAnswer answerSuccess(MicroByteBuffer buffer, Warrior unit, ServerWarriorCollection enemies) {
        return new ServerMoveWarriorAnswer(buffer, unit, enemies, true);
    }

    private ServerMoveWarriorAnswer(MicroByteBuffer buffer, Warrior unit, ServerWarriorCollection enemies, boolean success) {
        this.buffer = buffer;
        this.unit = unit;
        this.enemies = enemies;
        this.success = success;
    }

    @Override
    public short getType() {
        return Packets.MOVE_WARRIOR;
    }

    @Override
    protected MicroByteBuffer getBuffer() {
        return buffer;
    }

    /**
     * id союза
     * id группы
     * id воина
     * x и y на карте куда он пойдёт (конец пути)
     * массив врагов которых он "засветил" ("засвет" противника может произойти только в конце пути)
     *
     * @param buffer
     */
    @Override
    public void serialize(MicroByteBuffer buffer) {
        SimpleSerializer.serialize(success, buffer);
        if (success) {
            SerializeHelper.serializeEntityReference(unit, buffer);
            SimpleSerializer.serialize(unit.getCellX(), buffer);
            SimpleSerializer.serialize(unit.getCellY(), buffer);
            if (enemies != null) {
                SimpleSerializer.serialize((byte) enemies.size(), buffer);
                for (Warrior warrior : enemies.getWarriors()) {
                    SerializeHelper.serializeEntityReference(warrior, buffer);
                    SimpleSerializer.serialize(warrior.getCellX(), buffer);
                    SimpleSerializer.serialize(warrior.getCellY(), buffer);
                }
            } else {
                SimpleSerializer.serialize((byte) 0, buffer);
            }
        }
    }

}
