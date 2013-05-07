package com.geargames.regolith.awt.components.battle.warriorMenu;

import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.awt.components.PRegolithPanelManager;
import com.geargames.regolith.awt.components.PRootContentPanel;
import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.units.map.ClientWarriorElement;

/**
 * User: abarakov
 * 17.04.13
 * Панель, на которой располагается кнопка переключения положения Стоит/Сидит у активного бойца.
 */
public class PBattleWarriorMenuPanel extends PRootContentPanel {
    private PStandUpButton standUpButton;
    private PSitDownButton sitDownButton;

    public PBattleWarriorMenuPanel(PObject prototype) {
        super(prototype);
//        Region region = getDrawRegion();
//        region.setMinX(-220);
//        region.setMinY(-region.getHeight()/2);
//        region = getTouchRegion();
//        region.setMinX(-220);
//        region.setMinY(-region.getHeight()/2);
    }

    @Override
    protected void createSlotElementByIndex(IndexObject index, PObject prototype) {
        switch (index.getSlot()) {
            case 0:
                standUpButton = new PStandUpButton((PObject) index.getPrototype());
                standUpButton.setVisible(false);
                addActiveChild(standUpButton, index);
                break;
            case 1:
                sitDownButton = new PSitDownButton((PObject) index.getPrototype());
                addActiveChild(sitDownButton, index);
                break;
        }
    }

    @Override
    public void onShow() {

    }

    @Override
    public void onHide() {

    }

    /**
     * Обработчик события изменения активного бойца.
     */
    public void onActiveUnitChanged(ClientWarriorElement activeUnit) {
        if (activeUnit.isSitting()) {
            standUpButton.setVisible(true);
            sitDownButton.setVisible(false);
        } else {
            standUpButton.setVisible(false);
            sitDownButton.setVisible(true);
        }
    }

    /**
     * Обработчик нажатия на кнопку "Посадить бойца".
     */
    public void onSitDownButtonClick() {
//        NotificationBox.info("Посадить бойца", this);
        PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
        ClientWarriorElement unit = panelManager.getBattleScreen().getActiveUnit();
        //todo: Сажать бойца можно только в случае, если сейчас наш ход и он сейчас не выполняет других команд
        if (unit.getLogic().isIdle() &&
                WarriorHelper.maySit(unit, ClientConfigurationFactory.getConfiguration().getBattleConfiguration())) {
            unit.getLogic().doSitDown();
            WarriorHelper.sit(unit, ClientConfigurationFactory.getConfiguration().getBattleConfiguration());
            panelManager.getBattleScreen().doMapReachabilityUpdate(); //todo: пусть это делается в WarriorHelper.sit()
            //todo: Менять видимость кнопок можно только в случае, если боец действительно присел
            standUpButton.setVisible(true);
            sitDownButton.setVisible(false);
        }
    }

    /**
     * Обработчик нажатия на кнопку "Поднять бойца".
     */
    public void onStandUpButtonClick() {
//        NotificationBox.info("Поднять бойца", this);
        PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
        ClientWarriorElement unit = panelManager.getBattleScreen().getActiveUnit();
        //todo: Поднимать бойца можно только в случае, если сейчас наш ход и он сейчас не выполняет других команд
        if (unit.getLogic().isIdle() &&
                WarriorHelper.mayStand(unit, ClientConfigurationFactory.getConfiguration().getBattleConfiguration())) {
            unit.getLogic().doStandUp();
            WarriorHelper.stand(unit, ClientConfigurationFactory.getConfiguration().getBattleConfiguration());
            panelManager.getBattleScreen().doMapReachabilityUpdate(); //todo: пусть это делается в WarriorHelper.stand()
            //todo: Менять видимость кнопок можно только в случае, если боец действительно поднялся
            standUpButton.setVisible(false);
            sitDownButton.setVisible(true);
        }
    }

}
