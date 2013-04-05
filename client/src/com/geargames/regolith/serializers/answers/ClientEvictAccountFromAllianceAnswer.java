package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.helpers.ClientBattleHelper;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleAlliance;
import com.geargames.regolith.units.battle.BattleGroup;
import com.geargames.regolith.units.dictionaries.BattleGroupCollection;

/**
 * Users: mkutuzov, abarakov
 * Date: 05.07.12
 * Сообщение-ответ об исключении/выходе пользователя из битвы/альянса. Рассылается всем слушателям битвы.
 */
public class ClientEvictAccountFromAllianceAnswer extends ClientDeSerializedMessage {
//    private Battle battle;
    private boolean isSuccess;
    private BattleAlliance alliance;
    private Account account;

//    public void setBattle(Battle battle) {
//        this.battle = battle;
//    }

    public void deSerialize(MicroByteBuffer buffer) throws Exception {
        isSuccess = SimpleDeserializer.deserializeBoolean(buffer);
        alliance = null;
        account = null;
        if (isSuccess) {
            Battle battle = ClientConfigurationFactory.getConfiguration().getBattle();
            alliance = ClientBattleHelper.findAllianceById(battle, SimpleDeserializer.deserializeInt(buffer));

            int id = SimpleDeserializer.deserializeInt(buffer);
            BattleGroupCollection battleGroups = alliance.getAllies();
            for (int i = 0; i < battleGroups.size(); i++) {
                BattleGroup battleGroup = battleGroups.get(i);
                Account account = battleGroup.getAccount();
                if (account.getId() == id) {
                    this.account = account;
                    battleGroup.setAccount(null);
                    battleGroup.setWarriors(null);
                    return;
                }
            }
            throw new IllegalStateException();
        }
    }

    /**
     * Вернет true, если ответ пришел об успешности запроса.
     */
    public boolean isSuccess() {
        return isSuccess;
    }

    /**
     * Военный союз из которого выкинули пользователя.
     */
    public BattleAlliance getAlliance() {
        return alliance;
    }

    /**
     * Пользователь, которого выкинули из битвы, или который вышел сам.
     */
    public Account getAccount() {
        return account;
    }

}
