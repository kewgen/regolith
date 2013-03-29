package com.geargames.regolith.awt.components.battles;

import com.geargames.awt.DrawablePPanel;
import com.geargames.common.logging.Debug;
import com.geargames.common.network.MessageDispatcher;
import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.managers.ClientBattleMarketManager;
import com.geargames.regolith.serializers.answers.ClientConfirmationAnswer;
import com.geargames.regolith.serializers.answers.ClientListenToBattleAnswer;

/**
 * User: m.v.kutuzov
 * Date: 28.03.13
 * Окошко верхнего уровня для списка битв.
 * На открытие окошко регистрирует панель списка битв
 */
public class PBattlesPanelWindow extends DrawablePPanel {

    public void onShow() {
        ClientConfiguration configuration = ClientConfigurationFactory.getConfiguration();
        ClientBattleMarketManager battleMarket = configuration.getBattleMarketManager();


        try {
            ClientConfirmationAnswer answer = battleMarket.listenToCreatedBattles();

                if (answer.isConfirm()) {
                    MessageDispatcher dispatcher = configuration.getMessageDispatcher();
                    dispatcher.register(((PBattlesPanel) getElement()).getBattleList());
                } else {
                    Debug.error("Server has not confirmed [listen to created battles]");
                }
        } catch (Exception e) {
            Debug.error("Could not serialize [listen to created battles] answer", e);
        }
    }

    public void onHide() {
        ClientConfiguration configuration = ClientConfigurationFactory.getConfiguration();
        ClientBattleMarketManager battleMarket = configuration.getBattleMarketManager();

        try {
            ClientConfirmationAnswer answer = battleMarket.doNotListenToCreatedBattles();
                if (answer.isConfirm()) {
                    MessageDispatcher dispatcher = ClientConfigurationFactory.getConfiguration().getMessageDispatcher();
                    dispatcher.unregister(((PBattlesPanel) getElement()).getBattleList());
                } else {
                    Debug.error("Server has not confirmed [do not listen to created battles]");
                }
        } catch (Exception e) {
            Debug.error("Could not serialize [do listen to created battles] answer", e);
        }

    }
}
