package com.geargames.regolith.serializers.answers;

import com.geargames.regolith.BaseConfigurationHelper;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleAlliance;
import com.geargames.regolith.units.battle.BattleGroup;
import com.geargames.regolith.units.battle.BattleType;
import com.geargames.regolith.units.dictionaries.ClientBattleGroupCollection;

import java.util.Vector;

/**
 * User: mkutuzov
 * Date: 06.07.12
 */
// ClientCreateBattleAnswer
public class ClientListenToBattleAnswer extends ClientDeSerializedMessage {
    private Battle battle;

    public ClientListenToBattleAnswer() {
    }

    public void setBattle(Battle battle) {
        this.battle = battle;
    }

    public Battle getBattle() {
        return battle;
    }

    public void deSerialize(MicroByteBuffer buffer) throws Exception {
        boolean success = SimpleDeserializer.deserializeBoolean(buffer);
        if (success) {
            if (battle == null) {
                battle = new Battle();
            }
            battle.setId(SimpleDeserializer.deserializeInt(buffer));
            battle.setName(SimpleDeserializer.deserializeString(buffer));
            BattleType battleType = BaseConfigurationHelper.findBattleTypeById(SimpleDeserializer.deserializeInt(buffer), ClientConfigurationFactory.getConfiguration().getBaseConfiguration());
            battle.setBattleType(battleType);
            // Очищаем, на случай, если battle создавался в другом месте и map может содержать значение.
            battle.setMap(null);
//          battle.setAuthor(null); //todo: Заполнить Author
            int allianceAmount = buffer.get();
            battle.setAlliances(new BattleAlliance[allianceAmount]);
            for (int i = 0; i < allianceAmount; i++) {
                BattleAlliance alliance = new BattleAlliance();
                alliance.setId(SimpleDeserializer.deserializeInt(buffer));
                alliance.setAllies(new ClientBattleGroupCollection(new Vector()));
                for (int j = 0; j < battle.getBattleType().getAllianceSize(); j++) {
                    BattleGroup group = new BattleGroup();
                    group.setId(SimpleDeserializer.deserializeInt(buffer));
                    int id = SimpleDeserializer.deserializeInt(buffer);
                    if (id != -1) {
                        Account account = new Account();
                        account.setId(id);
                        group.setAccount(account);
                        account.setName(SimpleDeserializer.deserializeString(buffer));
                    }
                    group.setAlliance(alliance);
                    alliance.getAllies().add(group);
                }
                alliance.setBattle(battle);
                battle.getAlliances()[i] = alliance;
            }
        }
    }

}
