package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.serialization.SimpleSerializer;
import com.geargames.regolith.Packets;
import com.geargames.regolith.serializers.SerializeHelper;
import com.geargames.regolith.units.battle.Ally;
import com.geargames.regolith.units.battle.BattleAlliance;
import com.geargames.regolith.units.battle.BattleGroup;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.dictionaries.ServerAllyCollection;

/**
 * User: gear
 * Date: 25.04.13
 * Time: 22:13
  */
public class ServerMoveAllyAnswer extends SerializedMessage {
    private MicroByteBuffer buffer;
    private Warrior warrior;
    private ServerAllyCollection enemies;

    public ServerMoveAllyAnswer(MicroByteBuffer buffer, Warrior warrior, ServerAllyCollection enemies) {
        this.buffer = buffer;
        this.warrior = warrior;
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
     * массив врагов которых он "засветил"("засвет" противника может произойти только в конце пути)
     *
     * @param buffer
     */
    @Override
    public void serialize(MicroByteBuffer buffer) {
        SerializeHelper.serializeEntityReference(warrior.getBattleGroup(),buffer);
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
