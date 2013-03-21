package com.geargames.regolith.serializers.answers;

import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.serializers.AccountDeserializer;
import com.geargames.regolith.serializers.ClientDeSerializedMessage;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleAlliance;
import com.geargames.regolith.units.battle.BattleGroup;
import com.geargames.regolith.units.dictionaries.BattleGroupCollection;

/**
 * User: mkutuzov
 * Date: 05.07.12
 * Сообщение-ответ о присоединении пользователя к альянсу. Рассылается всем слушателям битвы.
 */
public class ClientJoinBattleAnswer extends ClientDeSerializedMessage {
    private Battle battle;
    private BattleGroup battleGroup;

    public ClientJoinBattleAnswer(Battle battle) {
        this.battle = battle;
    }

    public void deSerialize(MicroByteBuffer buffer) {
        boolean success = SimpleDeserializer.deserializeBoolean(buffer);
        if (success) {
            int id = SimpleDeserializer.deserializeInt(buffer);
            Account account = AccountDeserializer.deserialize(
                    buffer, ClientConfigurationFactory.getConfiguration().getBaseConfiguration());

            BattleAlliance[] alliances = battle.getAlliances();
            for (int i = 0; i < alliances.length; i++) {
                BattleGroupCollection groups = alliances[i].getAllies();
                for (int j = 0; j < groups.size(); j++) {
                    if (groups.get(j).getId() == id) {
                        battleGroup = groups.get(j);
                        battleGroup.setAccount(account);
                        return;
                    }
                }
            }
            throw new IllegalStateException();
        }
    }

    public BattleGroup getBattleGroup() {
        return battleGroup;
    }

}
