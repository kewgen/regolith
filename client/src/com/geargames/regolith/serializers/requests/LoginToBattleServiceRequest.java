package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.Packets;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.serializers.SimpleSerializer;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleAlliance;

/**
 * User: mikhail v. kutuzov
 * Присоедениться к службе битв.
 *
 * Date: 14.08.12
 * Time: 15:35
 */
public class LoginToBattleServiceRequest extends ClientSerializedMessage {
    private Battle battle;
    private BattleAlliance alliance;

    public LoginToBattleServiceRequest(ClientConfiguration configuration, Battle battle, BattleAlliance alliance) {
        super(configuration);
        this.battle = battle;
        this.alliance = alliance;
    }

    public short getType() {
        return Packets.BATTLE_SERVICE_LOGIN;
    }

    public void serialize(MicroByteBuffer buffer) {
        Account account = getConfiguration().getAccount();
        SimpleSerializer.serializeEntityReference(battle, buffer);
        SimpleSerializer.serializeEntityReference(alliance, buffer);
        SimpleSerializer.serializeEntityReference(account, buffer);
        SimpleSerializer.serialize(account.getName(), buffer);
        SimpleSerializer.serialize(account.getPassword(), buffer);
    }
}
