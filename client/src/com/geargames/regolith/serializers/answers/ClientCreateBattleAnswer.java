package com.geargames.regolith.serializers.answers;

import com.geargames.regolith.BaseConfigurationHelper;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.serializers.ClientDeSerializedMessage;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.serializers.SimpleDeserializer;
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
public class ClientCreateBattleAnswer extends ClientDeSerializedMessage {
    private Battle battle;

    public Battle getBattle() {
        return battle;
    }

    public ClientCreateBattleAnswer() {
        battle = null;
    }

    protected void deSerialize(MicroByteBuffer buffer) {
        if (SimpleDeserializer.deserializeBoolean(buffer)) {
            Battle battle = new Battle();
            battle.setId(SimpleDeserializer.deserializeInt(buffer));
            battle.setName(SimpleDeserializer.deserializeString(buffer));
            BattleType battleType = BaseConfigurationHelper.findBattleTypeById(SimpleDeserializer.deserializeInt(buffer), ClientConfigurationFactory.getConfiguration().getBaseConfiguration());
            battle.setBattleType(battleType);
            int allianceAmount =  buffer.get();
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
            this.battle = battle;
        }
    }
}
