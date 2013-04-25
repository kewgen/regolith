package com.geargames.regolith.awt.components.battle.battleMenu;

import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.NotificationBox;
import com.geargames.regolith.awt.components.PRegolithPanelManager;
import com.geargames.regolith.awt.components.PRootContentPanel;
import com.geargames.regolith.units.BattleUnit;
import com.geargames.regolith.units.battle.BattleAlliance;

/**
 * User: abarakov
 * 17.04.13
 * Панель, на которой располагаются кнопки меню битвы, такие как, выйти из битвы через зону выхода на карте, пропустить
 * ход или открыть чат.
 */
public class PBattleMenuPanel extends PRootContentPanel {
    private POpenChatButton openChatButton;
    private PSkipMoveButton skipMoveButton;
    private PLeaveBattleButton leaveBattleButton;

    public PBattleMenuPanel(PObject prototype) {
        super(prototype);
    }

    @Override
    protected void createSlotElementByIndex(IndexObject index, PObject prototype) {
        switch (index.getSlot()) {
            case 0:
                skipMoveButton = new PSkipMoveButton((PObject) index.getPrototype());
                addActiveChild(skipMoveButton, index);
                break;
            case 1:
                openChatButton = new POpenChatButton((PObject) index.getPrototype());
                addActiveChild(openChatButton, index);
                break;
            case 2:
                leaveBattleButton = new PLeaveBattleButton((PObject) index.getPrototype());
                leaveBattleButton.setVisible(false);
                addActiveChild(leaveBattleButton, index);
                break;
        }
    }

    @Override
    public void onShow() {
        onActiveAllianceChanged(PRegolithPanelManager.getInstance().getBattleScreen().getActiveAlliance());
    }

    @Override
    public void onHide() {

    }

    /**
     * Обработчик события об изменении активного военного союза, того чей, в данный момент, ход.
     */
    public void onActiveAllianceChanged(BattleAlliance alliance) {
        skipMoveButton.setVisible(PRegolithPanelManager.getInstance().getBattleScreen().isMyTurn());
//        ArrayList group = PRegolithPanelManager.getInstance().getBattleScreen().getGroup();
//        boolean exitAllowed = group.size() > 0;
//        for (int i = 0; i < group.size(); i++) {
//            BattleUnit battleUnit = (BattleUnit) group.get(i);
//            if (!battleUnit.getUnit().locatedInExitZone()) { //todo: Как реализовать locatedInExitZone() ?
//                exitAllowed = false;
//                break;
//            }
//        }
//        leaveBattleButton.setVisible(exitAllowed);
    }

    /**
     * Обработчик события изменения активного бойца.
     */
    public void onActiveUnitChanged(BattleUnit activeUnit) {

    }

    /**
     * Обработчик нажатия на кнопку "Передать ход следующему игроку".
     */
    public void onSkipMoveButtonClick() {
        NotificationBox.info("Передать ход следующему игроку", this);
    }

    /**
     * Обработчик нажатия на кнопку "Открыть чат".
     */
    public void onOpenChatButtonClick() {
        NotificationBox.info("Открыть чат", this);
    }

    /**
     * Обработчик нажатия на кнопку "Выйти из битвы".
     */
    public void onLeaveBattleButtonClick() {
        NotificationBox.info("Выйти из битвы", this);
    }

}
