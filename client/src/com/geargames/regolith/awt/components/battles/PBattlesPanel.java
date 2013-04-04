package com.geargames.regolith.awt.components.battles;

import com.geargames.awt.DrawablePPanel;
import com.geargames.awt.utils.ScrollHelper;
import com.geargames.awt.utils.motions.ElasticInertMotionListener;
import com.geargames.common.logging.Debug;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.application.ObjectManager;
import com.geargames.regolith.awt.components.PRegolithPanelManager;
import com.geargames.regolith.awt.components.PRootContentPanel;
import com.geargames.regolith.managers.ClientBattleMarketManager;
import com.geargames.regolith.serializers.answers.ClientConfirmationAnswer;
import com.geargames.regolith.units.battle.Battle;

/**
 * User: mikhail v. kutuzov, abarakov
 * Панель на которой отображены текущие битвы + кнопка [создать битву].
 */
public class PBattlesPanel extends PRootContentPanel {
    private PBattlesList battleList;
    private PBattleCreateButton createButton;

    public PBattlesPanel(PObject prototype) {
        super(prototype);
    }

    @Override
    protected void createSlotElementByIndex(IndexObject index, PObject prototype) {
        switch (index.getSlot()) {
            case 0:
                battleList = new PBattlesList((PObject)index.getPrototype());
                ElasticInertMotionListener motionListener = new ElasticInertMotionListener();
                battleList.setMotionListener(motionListener);
                ScrollHelper.adjustVerticalInertMotionListener(motionListener, battleList);
                addActiveChild(battleList, index);
                break;
            case 1:
                createButton = new PBattleCreateButton((PObject)index.getPrototype());
                addActiveChild(createButton, index);
                break;
        }
    }

    @Override
    public void onShow() {
        ClientConfiguration configuration = ClientConfigurationFactory.getConfiguration();
        ClientBattleMarketManager battleMarket = configuration.getBattleMarketManager();
        try {
            ClientConfirmationAnswer confirmationAnswer = battleMarket.listenToCreatedBattles();
            if (confirmationAnswer.isConfirm()) {
                ObjectManager.getInstance().getBattleCollection().getBattles().clear();
                configuration.getMessageDispatcher().register(battleList);
            } else {
                Debug.error("Server has not confirmed [listen to created battles]");
            }
        } catch (Exception e) {
            Debug.error("Could not deserialize [listen to created battles] answer", e);
        }
    }

    @Override
    public void onHide() {
        ClientConfiguration configuration = ClientConfigurationFactory.getConfiguration();
        ClientBattleMarketManager battleMarket = configuration.getBattleMarketManager();
        try {
            ClientConfirmationAnswer confirmationAnswer = battleMarket.doNotListenToCreatedBattles();
            if (!confirmationAnswer.isConfirm()) {
                Debug.error("Server has not confirmed [do not listen to created battles]");
            }
            configuration.getMessageDispatcher().unregister(battleList);
        } catch (Exception e) {
            Debug.error("Could not serialize [do listen to created battles] answer", e);
        }
    }

    /**
     * Отобразить панельку.
     * @param listenedBattle - битва созданная данным клиентом, или null если только начали слушать все битвы
     * @param callerPanel    - панелька, из которой перешли на данную панельку
     */
    // activatePanel
    public void showPanel(Battle listenedBattle, DrawablePPanel callerPanel, boolean isModalCallerPanel) {
        Debug.debug("Dialog 'Battles'");
        battleList.setListenedBattle(listenedBattle);
        ScrollHelper.adjustVerticalInertMotionListener((ElasticInertMotionListener)battleList.getMotionListener(), battleList);

        PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
        if (isModalCallerPanel) {
            panelManager.hideModal();
        } else {
            panelManager.hide(callerPanel);
        }
        panelManager.show(panelManager.getBattlesWindow());
    }

    /**
     * Обработчик нажатия на кнопку "Создать бой".
     */
    public void onBattleCreateButtonClick() {
        PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
        panelManager.getBattleCreatePanel().showPanel(panelManager.getBattlesWindow());
    }

}
