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
public class ServerMoveEnemyAnswer extends SerializedMessage {
    private MicroByteBuffer buffer;
    private Warrior warrior;
    private List<Pair> pairs;

    public ServerMoveEnemyAnswer(MicroByteBuffer buffer, Warrior warrior, List<Pair> pairs) {
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
        buffer.mark();
        buffer.setPosition(buffer.getPosition() + 4);
        int i = 0;
        for (Pair pair : pairs) {
            if (pair != null) {
                if (invisible != 0) {
                    SimpleSerializer.serialize(SimpleSerializer.NULL_COORDINATE, buffer);
                    SimpleSerializer.serialize(invisible, buffer);
                    invisible = 0;
                }
                SimpleSerializer.serialize(pair.getX(), buffer);
                SimpleSerializer.serialize(pair.getY(), buffer);
                i++;
            } else {
                invisible++;
            }
        }
        int position = buffer.getPosition();
        buffer.reset();
        SimpleSerializer.serialize(i, buffer);
        buffer.setPosition(position);
    }
}
