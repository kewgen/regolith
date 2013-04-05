package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.application.ObjectManager;
import com.geargames.regolith.helpers.BaseConfigurationHelper;
import com.geargames.regolith.serializers.AccountDeserializer;
import com.geargames.regolith.serializers.SerializeHelper;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleAlliance;
import com.geargames.regolith.units.battle.BattleGroup;
import com.geargames.regolith.units.battle.BattleType;

/**
 * User: m.v.kutuzov, abarakov
 * Date: 28.03.13
 */
public class ClientListenToUpdatedBattles extends ClientDeSerializedMessage {

    public void deSerialize(MicroByteBuffer buffer) throws Exception {
        boolean success = SimpleDeserializer.deserializeBoolean(buffer); //todo: это лишне
        if (success) {
            int battleId = SimpleDeserializer.deserializeInt(buffer);
            Battle battle = ObjectManager.getInstance().findBattleById(battleId);
            if (battle != null) {
//                battle.setId(battleId);
                battle.setName(SimpleDeserializer.deserializeString(buffer));
                int battleTypeId = SimpleDeserializer.deserializeInt(buffer);
                BattleType battleType = BaseConfigurationHelper.findBattleTypeById(battleTypeId, ClientConfigurationFactory.getConfiguration().getBaseConfiguration());
                battle.setBattleType(battleType);
                Account battleAuthor = AccountDeserializer.deserializeAnother(buffer);
                battle.setAuthor(battleAuthor);
                int allianceAmount = buffer.get();
                for (int i = 0; i < allianceAmount; i++) {
                    BattleAlliance alliance = battle.getAlliances()[i];
                    int allianceId = SimpleDeserializer.deserializeInt(buffer);
                    alliance.setId(allianceId);
                    for (int j = 0; j < battle.getBattleType().getAllianceSize(); j++) {
                        BattleGroup group = alliance.getAllies().get(j);
                        int battleGroupId = SimpleDeserializer.deserializeInt(buffer);
                        group.setId(battleGroupId);
                        int accountId = SimpleDeserializer.deserializeInt(buffer);
                        if (accountId != SerializeHelper.NULL_REFERENCE) {
                            Account account = group.getAccount();
                            if (account == null) {
                                account = new Account();
                                group.setAccount(account);
                            }
                            account.setId(accountId);
                            account.setName(SimpleDeserializer.deserializeString(buffer));
                            account.setFrameId(SimpleDeserializer.deserializeInt(buffer));
                        } else {
                            group.setAccount(null);
                        }
                        group.setWarriors(null);
//                        group.setAlliance(alliance);
//                        alliance.getAllies().add(group);
                    }
//                    alliance.setBattle(battle);
//                    battle.getAlliances()[i] = alliance;
                }
            }
        }
    }

}
