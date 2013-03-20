package com.geargames.regolith.serializers.answers;

import com.geargames.regolith.Packets;
import com.geargames.regolith.map.Pair;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.serialization.SimpleSerializer;
import com.geargames.regolith.serializers.SerializeHelper;
import com.geargames.regolith.units.battle.Warrior;

import java.util.List;

/**
 * User: mkutuzov
 * Date: 23.07.12
 */
public class ServerMoveWarriorEnemyAnswer extends SerializedMessage {
    private MicroByteBuffer buffer;
    private Warrior warrior;
    private List<Pair> pairs;

    public ServerMoveWarriorEnemyAnswer(MicroByteBuffer buffer, Warrior warrior, List<Pair> pairs) {
        this.buffer = buffer;
        this.warrior = warrior;
        this.pairs = pairs;
    }

    @Override
    public short getType() {
        return Packets.MOVE_ENEMY;
    }

    @Override
    protected MicroByteBuffer getBuffer() {
        return buffer;
    }

    @Override
    public void serialize(MicroByteBuffer buffer) {
        short invisible = 0;
        SerializeHelper.serializeEntityReference(warrior.getBattleGroup().getAlliance(), buffer);
        SerializeHelper.serializeEntityReference(warrior.getBattleGroup(), buffer);
        SerializeHelper.serializeEntityReference(warrior, buffer);
        for (Pair pair : pairs) {
            if (pair != null) {
                if (invisible != 0) {
                    SimpleSerializer.serialize(SimpleSerializer.NULL_COORDINATE, buffer);
                    SimpleSerializer.serialize(invisible, buffer);
                    invisible = 0;
                }
                SimpleSerializer.serialize(pair.getX(), buffer);
                SimpleSerializer.serialize(pair.getY(), buffer);
            } else {
                invisible++;
            }
        }
    }
}
