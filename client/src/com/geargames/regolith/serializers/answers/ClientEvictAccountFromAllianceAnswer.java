package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.serializers.ClientDeSerializedMessage;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.ClientBattleHelper;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleAlliance;
import com.geargames.regolith.units.battle.BattleGroup;
import com.geargames.regolith.units.dictionaries.BattleGroupCollection;

/**
 * User: mkutuzov
 * Date: 05.07.12
 * Сообщение-ответ об исключении/выходе пользователя из битвы/альянса. Рассылается всем слушателям битвы.
 */
public class ClientEvictAccountFromAllianceAnswer extends ClientDeSerializedMessage {
    private Battle battle;
    private boolean isSuccess;
    private BattleAlliance alliance;
    private Account account;

    public ClientEvictAccountFromAllianceAnswer(Battle battle) {
        this.battle = battle;
    }

    public void deSerialize(MicroByteBuffer buffer) {
        isSuccess = SimpleDeserializer.deserializeBoolean(buffer);
        if (isSuccess) {
            try {
                alliance = ClientBattleHelper.findAllianceById(battle, SimpleDeserializer.deserializeInt(buffer));
            } catch (RegolithException e) {
                alliance = null;
                //todo: передать сообщение об ошибке в лог
            }
            int id = SimpleDeserializer.deserializeInt(buffer);
            BattleGroupCollection battleGroups = alliance.getAllies();
            this.account = null;
            for (int i = 0; i < battleGroups.size(); i++) {
                BattleGroup battleGroup = battleGroups.get(i);
                Account account = battleGroup.getAccount();
                if (account.getId() == id) {
                    this.account = account;
                    battleGroup.setAccount(null);
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
