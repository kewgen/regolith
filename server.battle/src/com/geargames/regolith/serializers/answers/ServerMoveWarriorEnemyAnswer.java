package com.geargames.regolith.serializers.answers;

import com.geargames.regolith.Packets;
import com.geargames.regolith.map.Pair;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.serialization.SimpleSerializer;
import com.geargames.regolith.serializers.SerializeHelper;
import com.geargames.regolith.units.Human;
import com.geargames.regolith.units.map.HumanElement;

import java.util.List;

/**
 * User: mkutuzov
 * Date: 23.07.12
 */
public class ServerMoveWarriorEnemyAnswer extends SerializedMessage {
    private MicroByteBuffer buffer;
    private HumanElement unit;
    private List<Pair> pairs;

    public ServerMoveWarriorEnemyAnswer(MicroByteBuffer buffer, HumanElement unit, List<Pair> pairs) {
        this.buffer = buffer;
        this.unit = unit;
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
        Human human = unit.getHuman();
        SerializeHelper.serializeEntityReference(human.getBattleGroup().getAlliance(), buffer);
        SerializeHelper.serializeEntityReference(human.getBattleGroup(), buffer);
        SerializeHelper.serializeEntityReference(human, buffer);
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
