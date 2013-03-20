package com.geargames.regolith.serializers.answers;

import com.geargames.regolith.RegolithException;
import com.geargames.regolith.serializers.ClientDeSerializedMessage;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.units.ClientBattleHelper;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleAlliance;

/**
 * User: mikhail v. kutuzov
 * Date: 27.08.12
 * Time: 13:41
 */
public class ClientChangeActiveAllianceAnswer extends ClientDeSerializedMessage {
    private Battle battle;
    private BattleAlliance alliance;

    public ClientChangeActiveAllianceAnswer(){}

    public ClientChangeActiveAllianceAnswer(Battle battle) {
        this.battle = battle;
    }

    public BattleAlliance getAlliance() {
        return alliance;
    }

    public void setBattle(Battle battle) {
        this.battle = battle;
    }

    public void deSerialize(MicroByteBuffer buffer) {
        try {
            alliance = ClientBattleHelper.findAllianceById(battle, SimpleDeserializer.deserializeInt(buffer));
        } catch (RegolithException e) {
        }
    }
}
