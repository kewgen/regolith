package com.geargames.regolith.awt.components.battles;

import com.geargames.common.logging.Debug;
import com.geargames.common.network.MessageDispatcher;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.application.ObjectManager;
import com.geargames.regolith.awt.components.PRootContentPanel;
import com.geargames.regolith.managers.ClientBattleMarketManager;
import com.geargames.regolith.serializers.answers.ClientConfirmationAnswer;

/**
 * User: mikhail v. kutuzov
 * Панель на которой отображены текущие битвы + кнопка [создать битву].
 */
public class PBattlesPanel extends PRootContentPanel {
    private PBattlesList battleList;
    private PBattleCreateButton createButton;

    public PBattlesPanel(PObject prototype) {
        super(prototype);
    }

    protected void createSlotElementByIndex(IndexObject index, PObject prototype) {
        switch (index.getSlot()) {
            case 0:
                battleList = new PBattlesList((PObject)index.getPrototype());
                addActiveChild(battleList, index);
                break;
            case 1:
                createButton = new PBattleCreateButton((PObject)index.getPrototype());
                addActiveChild(createButton, index);
                break;
        }
    }

    public void onShow() {
        ClientConfiguration configuration = ClientConfigurationFactory.getConfiguration();
        ClientBattleMarketManager battleMarket = configuration.getBattleMarketManager();

        try {
            ClientConfirmationAnswer answer = battleMarket.listenToCreatedBattles();

                if (answer.isConfirm()) {
                    ObjectManager.getInstance().getBattleCollection().getBattles().clear();
                    MessageDispatcher dispatcher = configuration.getMessageDispatcher();
                    dispatcher.register(battleList);
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
                    dispatcher.unregister(battleList);
                } else {
                    Debug.error("Server has not confirmed [do not listen to created battles]");
                }
        } catch (Exception e) {
            Debug.error("Could not serialize [do listen to created battles] answer", e);
        }
    }
}
