package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.application.ObjectManager;
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
 * User: m.v.kutuzov
 * Date: 28.03.13
 */
public class ClientListenToUpdatedBattles extends ClientDeSerializedMessage {

    public void deSerialize(MicroByteBuffer buffer) throws Exception {
        if (SimpleDeserializer.deserializeBoolean(buffer)) {
            Battle battle = ObjectManager.getInstance().findBattleById(SimpleDeserializer.deserializeInt(buffer));
            if (battle != null) {
                buffer.setPosition(buffer.getPosition() - 4);
                battle.setId(SimpleDeserializer.deserializeInt(buffer));
                battle.setName(SimpleDeserializer.deserializeString(buffer));
                BattleType battleType = BaseConfigurationHelper.findBattleTypeById(SimpleDeserializer.deserializeInt(buffer), ClientConfigurationFactory.getConfiguration().getBaseConfiguration());
                battle.setBattleType(battleType);
                Account battleAuthor = AccountDeserializer.deserializeAnother(buffer);
                battle.setAuthor(battleAuthor);
                int allianceAmount = buffer.get();
                for (int i = 0; i < allianceAmount; i++) {
                    BattleAlliance alliance = battle.getAlliances()[i];
                    alliance.setId(SimpleDeserializer.deserializeInt(buffer));
                    for (int j = 0; j < battle.getBattleType().getAllianceSize(); j++) {
                        BattleGroup group = alliance.getAllies().get(j);
                        int id = SimpleDeserializer.deserializeInt(buffer);
                        group.setId(id);
                        id = SimpleDeserializer.deserializeInt(buffer);
                        if (id != SerializeHelper.NULL_REFERENCE) {
                            Account account = group.getAccount();
                            if (account == null) {
                                account = new Account();
                                group.setAccount(account);
                            }
                            account.setId(id);
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
}
