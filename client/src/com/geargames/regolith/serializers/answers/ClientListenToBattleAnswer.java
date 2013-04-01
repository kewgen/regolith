package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.helpers.BaseConfigurationHelper;
import com.geargames.regolith.serializers.AccountDeserializer;
import com.geargames.regolith.serializers.BattleDeserializer;
import com.geargames.regolith.serializers.SerializeHelper;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleAlliance;
import com.geargames.regolith.units.battle.BattleGroup;
import com.geargames.regolith.units.battle.BattleType;
import com.geargames.regolith.units.dictionaries.ClientBattleGroupCollection;

import java.util.Vector;

/**
 * User: mkutuzov, abarakov
 * Date: 06.07.12
 */
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
            Account battleAuthor = AccountDeserializer.deserializeAnother(buffer);
            battle.setAuthor(battleAuthor);
            int allianceAmount = buffer.get();
            battle.setAlliances(new BattleAlliance[allianceAmount]);
            for (int i = 0; i < allianceAmount; i++) {
                BattleAlliance alliance = new BattleAlliance();
                alliance.setId(SimpleDeserializer.deserializeInt(buffer));
                alliance.setAllies(new ClientBattleGroupCollection(new Vector()));
                for (int j = 0; j < battle.getBattleType().getAllianceSize(); j++) {
                    BattleGroup group = new BattleGroup();
                    int id = SimpleDeserializer.deserializeInt(buffer);
                    group.setId(id);
                    id = SimpleDeserializer.deserializeInt(buffer);
                    if (id != SerializeHelper.NULL_REFERENCE) {
                        Account account = new Account();
                        account.setId(id);
                        group.setAccount(account);
                        account.setName(SimpleDeserializer.deserializeString(buffer));
                        account.setFrameId(SimpleDeserializer.deserializeInt(buffer));
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
