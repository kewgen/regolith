package com.geargames.regolith.awt.components.main;

import com.geargames.awt.components.PTouchButton;
import com.geargames.common.logging.Debug;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.NotificationBox;
import com.geargames.regolith.awt.components.PRegolithPanelManager;
import com.geargames.regolith.localization.LocalizedStrings;
import com.geargames.regolith.serializers.answers.ClientConfirmationAnswer;

/**
 * Users: mikhail v. kutuzov, abarakov
 * Кнопка для входа в меню битв.
 */
public class PBattlesButton extends PTouchButton {

    public PBattlesButton(PObject prototype) {
        super(prototype);
    }

    /**
     * Вспомогательная функция. Переводит игрока в стейт BattleMarket.
     * @return - true, если переход в стейт BattleMarket произошел успешно.
     */
    private boolean goToBattleMarket() {
        try {
            Debug.debug("The client go to the battle market...");
            ClientConfirmationAnswer answer = ClientConfigurationFactory.getConfiguration().getBaseManager().goBattleMarket();
            if (!answer.isConfirm()) {
                Debug.error("GoToBattleMarket: Request rejected");
                NotificationBox.error(LocalizedStrings.BATTLES_MSG_GO_TO_BATTLE_MARKET_EXCEPTION, this);
                return false;
            }
            Debug.debug("The client moved into a state of battle market");
            return true;
        } catch (Exception e) {
            Debug.error("GoToBattleMarket: Send request and receive answer is failed", e);
            NotificationBox.error(LocalizedStrings.BATTLES_MSG_GO_TO_BATTLE_MARKET_EXCEPTION, this);
            return false;
        }
    }

    public void onClick() {
        if (goToBattleMarket()) {
            PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
            panelManager.getBattlesPanel().showPanel(null, null, panelManager.getMainMenu());
        }
    }

}
