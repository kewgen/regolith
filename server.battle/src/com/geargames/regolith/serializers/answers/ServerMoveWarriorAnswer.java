package com.geargames.regolith.serializers.answers;

import com.geargames.regolith.Packets;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.serialization.SimpleSerializer;
import com.geargames.regolith.serializers.SerializeHelper;
import com.geargames.regolith.units.map.HumanElement;
import com.geargames.regolith.units.dictionaries.ServerHumanElementCollection;

/**
 * User: mikhail v. kutuzov
 * Date: 23.08.12
 * Time: 13:56
 */
public class ServerMoveWarriorAnswer extends SerializedMessage {
    private MicroByteBuffer buffer;
    private HumanElement unit;
    private ServerHumanElementCollection enemies;
    private boolean success;

    public static ServerMoveWarriorAnswer answerFailure(MicroByteBuffer buffer) {
        return new ServerMoveWarriorAnswer(buffer, null, null, false);
    }


    public static ServerMoveWarriorAnswer answerSuccess(MicroByteBuffer buffer, HumanElement unit, ServerHumanElementCollection enemies) {
        return new ServerMoveWarriorAnswer(buffer, unit, enemies, true);
    }

    private ServerMoveWarriorAnswer(MicroByteBuffer buffer, HumanElement unit, ServerHumanElementCollection enemies, boolean success) {
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
            SerializeHelper.serializeEntityReference(unit.getHuman(), buffer);
            SimpleSerializer.serialize(unit.getCellX(), buffer);
            SimpleSerializer.serialize(unit.getCellY(), buffer);
            if (enemies != null) {
                SimpleSerializer.serialize(enemies.size(), buffer);
                for (HumanElement human : enemies.getElements()) {
                    SimpleSerializer.serialize(human.getHuman().getBattleGroup().getAlliance().getNumber(), buffer);
                    SerializeHelper.serializeEntityReference(human.getHuman().getBattleGroup(), buffer);
                    SerializeHelper.serializeEntityReference(human.getHuman(), buffer);
                    SimpleSerializer.serialize(human.getCellX(), buffer);
                    SimpleSerializer.serialize(human.getCellY(), buffer);
                }
            } else {
                SimpleSerializer.serialize(0, buffer);
            }
        }
    }

}
