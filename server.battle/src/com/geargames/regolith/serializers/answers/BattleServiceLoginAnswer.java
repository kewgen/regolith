package com.geargames.regolith.serializers.answers;

import com.geargames.regolith.Packets;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.serialization.SimpleSerializer;
import com.geargames.regolith.serializers.SerializeHelper;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleGroup;

import java.util.Collection;

/**
 * User: mikhail v. kutuzov
 * Date: 15.08.12
 * Time: 11:08
 */
public class BattleServiceLoginAnswer extends SerializedMessage {
    private MicroByteBuffer buffer;
    private Battle battle;
    private Collection<BattleGroup> groups;
    private boolean success;


    public static BattleServiceLoginAnswer answerFailure(MicroByteBuffer buffer, Battle battle) {
        return new BattleServiceLoginAnswer(buffer, battle, null, false);
    }

    public static BattleServiceLoginAnswer answerSuccess(MicroByteBuffer buffer, Battle battle, Collection<BattleGroup> groups){
        return new BattleServiceLoginAnswer(buffer, battle, groups, true);
    }

    private BattleServiceLoginAnswer(MicroByteBuffer buffer, Battle battle, Collection<BattleGroup> groups, boolean success) {
        this.buffer = buffer;
        this.battle = battle;
        this.groups = groups;
        this.success = success;
    }

    @Override
    public short getType() {
        return Packets.BATTLE_SERVICE_LOGIN;
    }

    @Override
    protected MicroByteBuffer getBuffer() {
        return buffer;
    }

    @Override
    public void serialize(MicroByteBuffer buffer) {
        SerializeHelper.serializeEntityReference(battle, buffer);
        SimpleSerializer.serialize(success, buffer);
        if (success) {
            SimpleSerializer.serialize(groups.size(), buffer);
            for (BattleGroup group : groups) {
                SerializeHelper.serializeEntityReference(group, buffer);
            }
        }
    }
}
