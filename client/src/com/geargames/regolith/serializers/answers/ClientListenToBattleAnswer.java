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
import com.geargames.regolith.units.dictionaries.ClientBattleGroupCollection;

import java.util.Vector;

/**
 * User: mkutuzov, abarakov
 * Date: 06.07.12
 */
public class ClientListenToBattleAnswer extends ClientDeSerializedMessage {
    private Battle battle;
    private boolean success;

    public ClientListenToBattleAnswer() {
    }

    public Battle getBattle() {
        return battle;
    }

    public void deSerialize(MicroByteBuffer buffer) throws Exception {
        success = SimpleDeserializer.deserializeBoolean(buffer);
        if (success) {
            int battleId = SimpleDeserializer.deserializeInt(buffer);
            //todo: Пока сервер не заработает более надежным образом будем искать уже созданную битву в ObjectManager.battleCollection
            // В некоторых случаях сервер высылает по несколько раз одинаковое сообщение ListenToCreatedBattle, поэтому
            // в списке ObjectManager.battleCollection оказываются разные объекты битвы, но реализующие одну и ту же битву.
            battle = ObjectManager.getInstance().findBattleById(battleId);
            if (battle == null) {
                battle = new Battle();
                battle.setId(battleId);
                ObjectManager.getInstance().getBattleCollection().add(battle);
            }
            battle.setName(SimpleDeserializer.deserializeString(buffer));
            int battleTypeId = SimpleDeserializer.deserializeInt(buffer);
            BattleType battleType = BaseConfigurationHelper.findBattleTypeById(battleTypeId, ClientConfigurationFactory.getConfiguration().getBaseConfiguration());
            battle.setBattleType(battleType);
            Account battleAuthor = AccountDeserializer.deserializeAnother(buffer);
            battle.setAuthor(battleAuthor);
            int allianceAmount = buffer.get();
            battle.setAlliances(new BattleAlliance[allianceAmount]);
            for (int i = 0; i < allianceAmount; i++) {
                BattleAlliance alliance = new BattleAlliance();
                int allianceId = SimpleDeserializer.deserializeInt(buffer);
                alliance.setId(allianceId);
                alliance.setAllies(new ClientBattleGroupCollection(new Vector()));
                alliance.setNumber((byte) i);
                for (int j = 0; j < battle.getBattleType().getAllianceSize(); j++) {
                    BattleGroup group = new BattleGroup();
                    int battleGroupId = SimpleDeserializer.deserializeInt(buffer);
                    group.setId(battleGroupId);
                    int accountId = SimpleDeserializer.deserializeInt(buffer);
                    if (accountId != SerializeHelper.NULL_REFERENCE) {
                        Account account = new Account();
                        group.setAccount(account);
                        account.setId(accountId);
                        account.setName(SimpleDeserializer.deserializeString(buffer));
                        account.setFrameId(SimpleDeserializer.deserializeInt(buffer));
                    }
                    group.setAlliance(alliance);
                    alliance.getAllies().add(group);
                }
                alliance.setBattle(battle);
                battle.getAlliances()[i] = alliance;
            }
        } else {
            battle = null;
        }
    }

    public boolean isSuccess() {
        return success;
    }

}
