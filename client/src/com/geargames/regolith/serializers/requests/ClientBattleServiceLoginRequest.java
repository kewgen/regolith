package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.Packets;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleSerializer;
import com.geargames.regolith.serializers.SerializeHelper;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleAlliance;

/**
 * User: mikhail v. kutuzov
 * Date: 15.08.12
 * Time: 14:25
 */
public class ClientBattleServiceLoginRequest extends ClientSerializedMessage {
    private Battle battle;
    private BattleAlliance alliance;
    private Account account;

    public ClientBattleServiceLoginRequest(ClientConfiguration configuration, Battle battle, BattleAlliance alliance, Account account) {
        super(configuration);
        this.battle = battle;
        this.alliance = alliance;
        this.account = account;
    }

    @Override
    public short getType() {
        return Packets.BATTLE_SERVICE_LOGIN;
    }

    @Override
    public void serialize(MicroByteBuffer buffer) {
        SerializeHelper.serializeEntityReference(battle, buffer);
        SerializeHelper.serializeEntityReference(alliance, buffer);
        SerializeHelper.serializeEntityReference(account, buffer);
        SimpleSerializer.serialize(account.getName(), buffer);
        SimpleSerializer.serialize(account.getPassword(), buffer);
    }
}
