package com.geargames.regolith.awt.components.battle.warriorMenu;

import com.geargames.common.Graphics;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.NotificationBox;
import com.geargames.regolith.awt.components.PRegolithPanelManager;
import com.geargames.regolith.awt.components.PRootContentPanel;
import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.units.BattleUnit;
import com.geargames.regolith.units.Unit;

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
     * Обработчик нажатия на кнопку "Посадить бойца".
     */
    public void onSitDownButtonClick() {
//        NotificationBox.info("Посадить бойца", this);
        PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
        BattleUnit battleUnit = panelManager.getBattleScreen().getUser();
        //todo: Сажать бойца можно только в случае, если сейчас наш ход и он сейчас не выполняет других команд
        if (panelManager.getBattleScreen().isMyTurn() &&
                WarriorHelper.maySit(battleUnit.getUnit().getWarrior(), ClientConfigurationFactory.getConfiguration().getBattleConfiguration())) {
            battleUnit.getUnit().stop(); //todo: Нужно ли?
            battleUnit.getUnit().sitDown();
            WarriorHelper.sit(battleUnit.getUnit().getWarrior(), ClientConfigurationFactory.getConfiguration().getBattleConfiguration());
            //todo: Менять кнопки можно только в случае, если боец действительно присел
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
        BattleUnit battleUnit = panelManager.getBattleScreen().getUser();
        //todo: Поднимать бойца можно только в случае, если сейчас наш ход и он сейчас не выполняет других команд
        if (panelManager.getBattleScreen().isMyTurn() &&
                WarriorHelper.mayStand(battleUnit.getUnit().getWarrior(), ClientConfigurationFactory.getConfiguration().getBattleConfiguration())) {
            battleUnit.getUnit().stop(); //todo: Нужно ли?
            battleUnit.getUnit().standUp();
            WarriorHelper.stand(battleUnit.getUnit().getWarrior(), ClientConfigurationFactory.getConfiguration().getBattleConfiguration());
            //todo: Менять кнопки можно только в случае, если боец действительно поднялся
            standUpButton.setVisible(false);
            sitDownButton.setVisible(true);
        }
    }

}
