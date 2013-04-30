package com.geargames.regolith.serializers.answers;

import com.geargames.regolith.Packets;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.serialization.SimpleSerializer;
import com.geargames.regolith.serializers.SerializeHelper;
import com.geargames.regolith.units.battle.Ally;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.dictionaries.ServerAllyCollection;

/**
 * User: mikhail v. kutuzov
 * Date: 23.08.12
 * Time: 13:56
 */
public class ServerMoveWarriorAnswer extends SerializedMessage {
    private MicroByteBuffer buffer;
    private Warrior warrior;
    private ServerAllyCollection enemies;
    private boolean success;

    public static ServerMoveWarriorAnswer answerFailure(MicroByteBuffer buffer) {
        return new ServerMoveWarriorAnswer(buffer, null, null, false);
    }


    public static ServerMoveWarriorAnswer answerSuccess(MicroByteBuffer buffer, Warrior warrior, ServerAllyCollection enemies) {
        return new ServerMoveWarriorAnswer(buffer, warrior, enemies, true);
    }

    private ServerMoveWarriorAnswer(MicroByteBuffer buffer, Warrior warrior, ServerAllyCollection enemies, boolean success) {
        this.buffer = buffer;
        this.warrior = warrior;
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
     * id война
     * x и y на карте куда он пойдёт(конец пути)
     * массив врагов которых он "засветил"("засвет" противника может произойти только в конце пути)
     *
     * @param buffer
     */
    @Override
    public void serialize(MicroByteBuffer buffer) {
        SimpleSerializer.serialize(success, buffer);
        if (success) {
            SerializeHelper.serializeEntityReference(warrior, buffer);
            SimpleSerializer.serialize(warrior.getX(), buffer);
            SimpleSerializer.serialize(warrior.getY(), buffer);
            if (enemies != null) {
                SimpleSerializer.serialize(enemies.size(), buffer);
                for (Ally human : enemies.getAllies()) {
                    SimpleSerializer.serialize(human.getBattleGroup().getAlliance().getNumber(), buffer);
                    SerializeHelper.serializeEntityReference(human.getBattleGroup(), buffer);
                    SerializeHelper.serializeEntityReference(human, buffer);
                    SimpleSerializer.serialize(human.getX(), buffer);
                    SimpleSerializer.serialize(human.getY(), buffer);
                }
            } else {
                SimpleSerializer.serialize(0, buffer);
            }
        }
    }
}
