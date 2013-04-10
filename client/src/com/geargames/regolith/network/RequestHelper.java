package com.geargames.regolith.network;

import com.geargames.awt.components.PObjectElement;
import com.geargames.common.logging.Debug;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.NotificationBox;
import com.geargames.regolith.localization.LocalizedStrings;
import com.geargames.regolith.managers.ClientBattleCreationManager;
import com.geargames.regolith.serializers.answers.ClientCancelBattleAnswer;
import com.geargames.regolith.serializers.answers.ClientCompleteGroupAnswer;
import com.geargames.regolith.serializers.answers.ClientConfirmationAnswer;
import com.geargames.regolith.serializers.answers.ClientEvictAccountFromAllianceAnswer;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleGroup;

/**
 * User: abarakov
 * Date: 10.04.13
 */
public class RequestHelper {

    /**
     * Вспомогательная функция. Исключает игрока из боевой группы.
     * @param battleGroup - боевая группа, которую нужно освободить;
     * @return            - true, если освобождение прошло успешно.
     */
    public static boolean evictAccountFromBattleGroup(BattleGroup battleGroup, PObjectElement element) {
        ClientBattleCreationManager battleCreationManager = ClientConfigurationFactory.getConfiguration().getBattleCreationManager();
        try {
            Debug.debug("The client is trying to get out of the battle (" +
                    "battle id = " + battleGroup.getAlliance().getBattle().getId() + "; battle group id = " + battleGroup.getId() + ")...");
            ClientEvictAccountFromAllianceAnswer evictAccountFromAllianceAnswer = battleCreationManager.evictAccount(
                    battleGroup.getAlliance(), battleGroup.getAccount());
            if (!evictAccountFromAllianceAnswer.isSuccess()) {
                Debug.error("EvictAccount: Request rejected (battle group id = " + battleGroup.getId() + ")");
                NotificationBox.error(LocalizedStrings.BATTLES_MSG_SELF_EVICT_ACCOUNT_EXCEPTION, element);
                return false;
            }
            Debug.debug("The client evicted from the alliance (" +
                    "battle group id = " + battleGroup.getId() +
                    "; account id = " + evictAccountFromAllianceAnswer.getAccount().getId() +
                    "; alliance id = " + evictAccountFromAllianceAnswer.getAlliance().getId() + ")");
            return true;
        } catch (Exception e) {
            Debug.error("EvictAccount: Send request and receive answer is failed (battle group id = " + battleGroup.getId() + ")", e);
            NotificationBox.error(LocalizedStrings.BATTLES_MSG_SELF_EVICT_ACCOUNT_EXCEPTION, element);
            return false;
        }
    }

    /**
     * Вспомогательная функция. Отписывает игрока от получения обновлений битвы.
     * @param battle - битва, от которой нужно отписаться;
     * @return       - true, если отписались от битвы успешно.
     */
    public static boolean doNotListenToBattle(Battle battle, PObjectElement element) {
        ClientBattleCreationManager battleCreationManager = ClientConfigurationFactory.getConfiguration().getBattleCreationManager();
        try {
            Debug.debug("The client is trying to unsubscribe from the battle (battle id = " + battle.getId() + ")...");
            ClientConfirmationAnswer confirmationAnswer = battleCreationManager.doNotListenToBattle(battle);
            if (!confirmationAnswer.isConfirm()) {
                Debug.error("DoNotListenToBattle: Request rejected (battle id = " + battle.getId() + ")");
                NotificationBox.error(LocalizedStrings.BATTLES_MSG_DO_NOT_LISTEN_TO_BATTLE_EXCEPTION, element);
                return false;
            }
            Debug.debug("The client unsubscribe from battle (battle id = " + battle.getId() + ")");
            return true;
        } catch (Exception e) {
            Debug.error("DoNotListenToBattle: Send request and receive answer is failed (battle id = " + battle.getId() + ")", e);
            NotificationBox.error(LocalizedStrings.BATTLES_MSG_DO_NOT_LISTEN_TO_BATTLE_EXCEPTION, element);
            return false;
        }
    }

    /**
     * Вспомогательная функция. Отменяет битву.
     * @param battle - битва, которую нужно отменить;
     * @return       - true, если отмена битвы прошла успешно.
     */
    public static boolean cancelBattle(Battle battle, PObjectElement element) {
        ClientBattleCreationManager battleCreationManager = ClientConfigurationFactory.getConfiguration().getBattleCreationManager();
        try {
            Debug.debug("Trying to cancelation the battle (battle id = " + battle.getId() + ")...");
            ClientCancelBattleAnswer cancelBattleAnswer = battleCreationManager.cancelBattle();
            if (!cancelBattleAnswer.isSuccess()) {
                Debug.error("CancelBattle: Request rejected (battle id = " + battle.getId() + ")");
                NotificationBox.error(LocalizedStrings.BATTLES_MSG_CANCEL_BATTLE_EXCEPTION, element);
                return false;
            }
            Debug.debug("The client canceled battle (battle id = " + battle.getId() + ")");
            return true;
        } catch (Exception e) {
            Debug.error("CancelBattle: Send request and receive answer is failed (battle id = " + battle.getId() + ")", e);
            NotificationBox.error(LocalizedStrings.BATTLES_MSG_CANCEL_BATTLE_EXCEPTION, element);
            return false;
        }
    }

    public static boolean disbandGroup(BattleGroup battleGroup, PObjectElement element) {
        ClientBattleCreationManager battleCreationManager = ClientConfigurationFactory.getConfiguration().getBattleCreationManager();
        try {
            Debug.debug("The client is trying disband the battle group (battle group id = " + battleGroup.getId() + ")...");
            ClientCompleteGroupAnswer completeGroupAnswer = battleCreationManager.disbandGroup(battleGroup);
            if (!completeGroupAnswer.isSuccess()) {
                Debug.error("DisbandGroup: Request rejected (battle group id = " + battleGroup.getId() + ")");
                NotificationBox.error(LocalizedStrings.SELECT_WARRIORS_MSG_DISBAND_GROUP_EXCEPTION, element);
                return false;
            }
            Debug.debug("The client disbanded the battle group (battle group id = " + completeGroupAnswer.getBattleGroup().getId() + ")");
            return true;
        } catch (Exception e) {
            Debug.error("DisbandGroup: Send request and receive answer is failed (battle group id = " + battleGroup.getId() + ")", e);
            NotificationBox.error(LocalizedStrings.SELECT_WARRIORS_MSG_DISBAND_GROUP_EXCEPTION, element);
            return false;
        }
    }

}
