package com.geargames.regolith.serializers.answers;

import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.serializers.AccountDeserializer;
import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.helpers.ClientBattleHelper;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleGroup;

/**
 * Users: mkutuzov, abarakov
 * Date: 05.07.12
 * Сообщение-ответ о присоединении пользователя к военному союзу (альянсу). Рассылается всем слушателям битвы.
 */
public class ClientJoinToBattleAllianceAnswer extends ClientDeSerializedMessage {
//    private Battle battle;
    private BattleGroup battleGroup;
    private boolean isSuccess;

//    public void setBattle(Battle battle) {
//        this.battle = battle;
//    }

    public void deSerialize(MicroByteBuffer buffer) throws Exception {
        battleGroup = null;
        isSuccess = SimpleDeserializer.deserializeBoolean(buffer);
        if (isSuccess) {
            int id = SimpleDeserializer.deserializeInt(buffer);
            Account account = AccountDeserializer.deserialize(
                    buffer, ClientConfigurationFactory.getConfiguration().getBaseConfiguration());
            Battle battle = ClientConfigurationFactory.getConfiguration().getBattle();
            battleGroup = ClientBattleHelper.findBattleGroupById(battle, id);
            battleGroup.setAccount(account);
            battleGroup.setWarriors(null);
        }
    }

    public BattleGroup getBattleGroup() {
        return battleGroup;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

}
