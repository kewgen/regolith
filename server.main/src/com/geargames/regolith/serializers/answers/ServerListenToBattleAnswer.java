package com.geargames.regolith.serializers.answers;

import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.serializers.SerializedMessage;
import com.geargames.regolith.serializers.SimpleSerializer;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleAlliance;
import com.geargames.regolith.units.battle.BattleGroup;
import com.geargames.regolith.units.dictionaries.ServerBattleGroupCollection;

/**
 * User: mkutuzov
 * Date: 21.06.12
 */
public class ServerListenToBattleAnswer extends SerializedMessage {
    private MicroByteBuffer buffer;
    private Battle battle;
    private short type;
    private boolean success;

    public static ServerListenToBattleAnswer AnswerSuccess(MicroByteBuffer buffer, Battle battle, short type) {
        return new ServerListenToBattleAnswer(buffer, battle, type, true);
    }

    public static ServerListenToBattleAnswer AnswerFailure(MicroByteBuffer buffer, short type) {
        return new ServerListenToBattleAnswer(buffer, null, type, false);
    }


    private ServerListenToBattleAnswer(MicroByteBuffer buffer, Battle battle, short type, boolean success) {
        this.buffer = buffer;
        this.battle = battle;
        this.success = success;
        this.type = type;
    }

    @Override
    public short getType() {
        return type;
    }

    @Override
    protected MicroByteBuffer getBuffer() {
        return buffer;
    }

    @Override
    public void serialize(MicroByteBuffer buffer) {
        SimpleSerializer.serialize(success, buffer);
        if (success) {
            SimpleSerializer.serializeEntityReference(battle, buffer);
            SimpleSerializer.serialize(battle.getName(), buffer);
            SimpleSerializer.serializeEntityReference(battle.getBattleType(), buffer);
            SimpleSerializer.serialize((byte) battle.getAlliances().length, buffer);
            for (BattleAlliance alliance : battle.getAlliances()) {
                SimpleSerializer.serializeEntityReference(alliance, buffer);
                for (BattleGroup group : ((ServerBattleGroupCollection) alliance.getAllies()).getBattleGroups()) {
                    Account account = group.getAccount();
                    SimpleSerializer.serializeEntityReference(group, buffer);
                    SimpleSerializer.serializeEntityReference(account, buffer);
                    if (group.getAccount() != null) {
                        SimpleSerializer.serialize(account.getName(), buffer);
                    }
                }
            }
        }
    }
}
